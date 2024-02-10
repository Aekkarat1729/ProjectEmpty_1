package com.example.projectempty.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectempty.MainStart
import com.example.projectempty.R
import com.example.projectempty.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val signoutButton = view.findViewById<Button>(R.id.profile_button_logout)
        signoutButton.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(requireContext(), "Signed out!", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Signed out!")
            val intent = Intent(requireActivity(), MainStart::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
    }
}
