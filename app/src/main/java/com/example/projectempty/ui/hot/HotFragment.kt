package com.example.projectempty.ui.hot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectempty.databinding.FragmentHotBinding

class HotFragment : Fragment() {

    private var _binding: FragmentHotBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val hotViewModel =
            ViewModelProvider(this).get(HotViewModel::class.java)

        _binding = FragmentHotBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHot
//        hotViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}