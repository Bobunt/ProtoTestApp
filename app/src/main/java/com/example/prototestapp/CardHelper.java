package com.example.prototestapp;

import androidx.annotation.NonNull;

import com.google.protobuf.ByteString;
import com.termt.intellireader.api.errors.IRMessageFormatException;
import com.termt.intellireader.api.errors.IRMessageIDMismatchException;
import com.termt.intellireader.modules.ContactlessL1;
import com.termt.intellireader.modules.ContactlessL2;
import com.termt.intellireader.modules.Miscellaneous;
import com.termt.intellireader.modules.Unspecified;
import com.termt.intellireader.transfer.IRTransfer;
import com.termt.intellireader.transport.IRTransportSerial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import contactless.poll.PollForTokenOuterClass;
import contactless.token.TokenOuterClass;
import contactless.token_type.TokenTypeOuterClass;
import misc.buzzer.Buzzer;

public class CardHelper {

    private final AtomicBoolean isPollingThreadRunning = new AtomicBoolean(false);
    private IRTransfer transfer;
    private IRTransportSerial transport;
    private Miscellaneous misc;
    private ContactlessL1 piccReader;
    private ContactlessL2 emvProcessor;
    private Unspecified unspecified;

    /**
     * polling flag, used by PICC_Thread in order to check whether to continue polling or stop it
     */
    private final AtomicBoolean continueSearch = new AtomicBoolean(true);
    public boolean pollingStopped() {
        return !continueSearch.get();
    }
    public void pollingStarted() {
        continueSearch.set(true);
    }
    void searchRFCard(SDKService.CardCallback<String> cardCallback) {
        SDKService.RfSearchCallback<Integer> rfSearchCallback = new SDKService.RfSearchCallback<Integer>() {
            @Override
            public void onError(@NonNull Exception e) {
                cardCallback.onError(e);
            }

            @Override
            public void  onSuccess() {
                cardCallback.onSuccess();
            }

            @Override
            public void onNotFound() {
                cardCallback.onNotFound();
            }
        };
        pollingStarted();
        PICC_Thread piccThread = new PICC_Thread(rfSearchCallback, 10000);
        piccThread.start();
    }

    private class PICC_Thread extends Thread {

        @SuppressWarnings("FieldCanBeLocal")
        private final SDKService.RfSearchCallback<Integer> callback;

        private final long timeout;

        public PICC_Thread(SDKService.RfSearchCallback<Integer> rfSearchCallback, long searchTimeout) {
            callback = rfSearchCallback;
            timeout = searchTimeout;
        }

        public void run() {
            try {
                isPollingThreadRunning.set(true);

                PollForTokenOuterClass.PollForToken.Builder builder = PollForTokenOuterClass.PollForToken.newBuilder();
                builder.setTimeout((int)timeout);
                PollForTokenOuterClass.PollingMode pollingMode = PollForTokenOuterClass.PollingMode.LOW_POWER_POLLING;
                builder.setPollingMode(pollingMode);
                PollForTokenOuterClass.PollForToken token = builder.build();
                TokenOuterClass.Token pollResult = piccReader.pollForToken(token).unwrap();

                TokenTypeOuterClass.TokenType tokenType = pollResult.getType();

                isPollingThreadRunning.set(false);
                callback.onSuccess();
            } catch (IRMessageFormatException
                     | IRMessageIDMismatchException
                     | IllegalStateException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void initPiccReader() {
        if (piccReader == null) {
            piccReader = new ContactlessL1(transfer);
        }
    }

    public void initEmvProcessor() {
        if (emvProcessor == null) {
            emvProcessor = new ContactlessL2(transfer);
        }
    }

    void beep() {
        ArrayList<Buzzer.Note> notes = new ArrayList<>();
        notes.add(Buzzer.Note.newBuilder().setDurationMs(250).setFrequencyHz(523).setSilenceDurationMs(10).build());
        misc.makeSound(notes);
    }

    private void initTransfer() {
        if (transfer == null) {
            if (transport == null) {
                String addr = getProp("persist.sys.irdev");
                String baud = getProp("sys.irbaudrate");
                transport = new IRTransportSerial(addr, Integer.parseInt(baud), 10000);
            }
            transfer = new IRTransfer(transport);
        }
    }

    void initPlatformSpecificLib() {
        initTransfer();
        unspecified = new Unspecified(transfer);
        misc = new Miscellaneous(transfer);
        initPiccReader();
        initEmvProcessor();
    }

    String getProp(String prop) {
        Process process = null;
        BufferedReader bufferedReader = null;
        String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";

        try {
            process = new ProcessBuilder().command(GETPROP_EXECUTABLE_PATH, prop).redirectErrorStream(true).start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
            if (line == null)
                line = ""; //prop not set
            return line;
        }

        catch (Exception e) { return ""; }
        finally {
            if (bufferedReader != null) {
                try { bufferedReader.close(); }
                catch (IOException e) { }
            }
            if (process != null)
                process.destroy();
        }
    }

}
