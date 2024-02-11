package com.example.projectempty.ui.home

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.projectempty.R
import com.example.projectempty.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var RecyclerViewMphoto:RecyclerView
    var mAuth: FirebaseAuth? = null
    var home_text_wellcome:TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        home_text_wellcome = view.findViewById<TextView>(R.id.home_text_wellcome)
        home_text_wellcome?.setText("Hello "+user?.email+" !!")

        RecyclerViewMphoto = view.findViewById<RecyclerView>(R.id.RecyclerView_MPhoto)



        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}