package com.example.prototestapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.prototestapp.databinding.FragmentMainBinding
import java.util.concurrent.atomic.AtomicBoolean

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.message.observe(this) {
            binding.textView.text = it
        }
        viewModel.messageCard.observe(this) {
            binding.textView2.text = "result: $it"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.initPlatformSpecificLib()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textView.text = "Кол/во Beeep = 0"
        binding.beepBoot.setOnClickListener {
            viewModel.addBeep()
        }
        binding.cardLook.setOnClickListener {
            viewModel.startLookCard()
        }
    }
}