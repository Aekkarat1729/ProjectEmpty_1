package com.example.projectempty.ui.home

import android.app.ListActivity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var RecyclerViewMphoto: RecyclerView
    var mAuth: FirebaseAuth? = null
    var home_text_wellcome: TextView? = null
    val myref = Firebase.database.reference
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

        RecyclerViewMphoto = view.findViewById<RecyclerView>(R.id.RecyclerView_MPhoto)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")

                home_text_wellcome?.setText("Your Wellcome "+value+" !!")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        var tempMail:String = user?.email.toString()
        val mark = '.'
        var newMail = ""
        for (char in tempMail){
            if(char != mark){
                newMail += char
            }
        }

        // ดึงข้อมูลแบบ Realtime
        myref.child("Account")
            .child(newMail)
            .child("Full name").addValueEventListener(postListener)

        return view
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}