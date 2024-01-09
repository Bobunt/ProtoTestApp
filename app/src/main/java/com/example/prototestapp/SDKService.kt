package com.example.prototestapp

import javax.security.auth.callback.Callback

interface SDKService {


    interface CardCallback<T> : Callback {
        fun onSuccess()

        fun onError(e: Exception)

        fun onNotFound()
    }

    interface RfSearchCallback<T> : Callback {
        fun onSuccess()

        fun onNotFound()

        fun onError(e: Exception)
    }

}