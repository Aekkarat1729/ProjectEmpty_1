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
import com.example.projectempty.databinding.FragmentHomeBinding
import com.example.projectempty.databinding.FragmentProfileBinding
import com.example.projectempty.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //ดึงข้อมูล user
        var mAuth: FirebaseAuth? = null
        mAuth = FirebaseAuth.getInstance()
        val myref = Firebase.database.reference
        val user = mAuth!!.currentUser
        var profile_text_name:TextView? = view.findViewById<TextView>(R.id.profile_text_name)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")
                profile_text_name?.setText(value)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        //ลบจุด
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
            .child("User name").addValueEventListener(postListener)


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


