package com.example.projectempty.ui.hot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectempty.databinding.FragmentHeartBinding

class HeartFragment : Fragment() {

    private var _binding: FragmentHeartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val heartViewModel =
            ViewModelProvider(this).get(HeartViewModel::class.java)

        _binding = FragmentHeartBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHeart
//        heartViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}