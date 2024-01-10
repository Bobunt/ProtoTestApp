package com.example.prototestapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.prototestapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.sumBeepMessage.observe(this) {
            binding.textView.text = "sum beep: $it"
        }
        viewModel.pollingButton.observe(this) {
            binding.cardLook.text = it
        }
        viewModel.pollingMessage.observe(this) {
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
        binding.beepBoot.setOnClickListener {
            viewModel.addBeep()
        }
        binding.cardLook.setOnClickListener {
            viewModel.startLookCard()
        }
    }
}