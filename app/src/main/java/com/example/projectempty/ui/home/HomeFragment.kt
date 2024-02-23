package com.example.projectempty.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectempty.AddActivity
import com.example.projectempty.MphotoModel
import com.example.projectempty.R
import com.example.projectempty.databinding.FragmentHomeBinding
import com.example.projectempty.homeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var mAuth: FirebaseAuth? = null
    lateinit var database: FirebaseDatabase
    var home_text_wellcome: TextView? = null
    val myref = Firebase.database.reference
    lateinit var RecyclerViewhome:RecyclerView
    lateinit var databaseReferencehome: DatabaseReference
    lateinit var responsehome:MutableList<MphotoModel>
    private var homeAdapter: homeAdapter? = null


    lateinit var fab:FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        val user = mAuth!!.currentUser
        home_text_wellcome = view.findViewById<TextView>(R.id.home_text_wellcome)


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")

                home_text_wellcome?.setText("Hello "+value+".")
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
            .child("User name").addValueEventListener(postListener)


        RecyclerViewhome = view.findViewById<RecyclerView>(R.id.RecyclerView_home)

        fab = view.findViewById(R.id.fab)

        //home photo database นะครับเตง
        //RecyclerViewhome.layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL, false)
        RecyclerViewhome.layoutManager = GridLayoutManager(context,2)
        databaseReferencehome = database.getReference("home")
        responsehome = mutableListOf()
        homeAdapter = homeAdapter(responsehome as ArrayList<MphotoModel>)
        RecyclerViewhome.adapter = homeAdapter

        fab.setOnClickListener{
            val intent = Intent(context,AddActivity::class.java)
            startActivity(intent)
        }
        onBindingFirebase()
        return view
    }

    private fun onBindingFirebase() {
        databaseReferencehome.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@HomeFragment.responsehome.add(snapshot.getValue(MphotoModel::class.java)!!)
                homeAdapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val TAG = "HomeFragment"
    }
}
