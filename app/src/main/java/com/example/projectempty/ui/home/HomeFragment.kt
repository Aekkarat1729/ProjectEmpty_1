package com.example.projectempty.ui.home

import android.app.ListActivity
import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectempty.MphotoModel
import com.example.projectempty.R
import com.example.projectempty.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    var mAuth: FirebaseAuth? = null
    lateinit var RecyclerViewMphoto:RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var databaseReferenceMphoto: DatabaseReference
    lateinit var responseMphoto:MutableList<MphotoModel>
    private var MphotoAdapter: MphotoAdapter? = null

    private var _binding: FragmentHomeBinding? = null
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



        database = FirebaseDatabase.getInstance()

        RecyclerViewMphoto = view.findViewById<RecyclerView>(R.id.RecyclerView_MPhoto)
        RecyclerViewMphoto.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        databaseReferenceMphoto.database.getReference("Mphoto/")
        responseMphoto = mutableListOf()
        MphotoAdapter = MphotoAdapter(responseMphoto as ArrayList<MphotoModel>)
        RecyclerViewMphoto.adapter = MphotoAdapter
        onBindingFirebase()








        return view
    }

    private fun onBindingFirebase() {
        databaseReferenceMphoto.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                responseMphoto.add(snapshot.getValue(MphotoModel::class.java)!!)
                MphotoAdapter!!.notifyDataSetChanged()
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
}