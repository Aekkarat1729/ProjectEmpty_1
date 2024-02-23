package com.example.projectempty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class homefeed : AppCompatActivity() {

    val myref = Firebase.database.reference
    lateinit var RecyclerViewhomefeed: RecyclerView
    lateinit var databaseReferencehomefeed: DatabaseReference
    lateinit var responsehome:MutableList<MphotoModel>
    private var homeAdapter: homeAdapter? = null
    lateinit var fab: FloatingActionButton
    var mAuth: FirebaseAuth? = null
    lateinit var database: FirebaseDatabase


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homefeed)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        RecyclerViewhomefeed = findViewById<RecyclerView>(R.id.RecyclerView_homefeed)


        //home photo database นะครับเตง
        //RecyclerViewhome.layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL, false)
        RecyclerViewhomefeed.layoutManager = GridLayoutManager(this@homefeed,2)
        databaseReferencehomefeed = database.getReference("home")
        responsehome = mutableListOf()
        homeAdapter = homeAdapter(responsehome as ArrayList<MphotoModel>)
        RecyclerViewhomefeed.adapter = homeAdapter

        onBindingFirebase()
    }

    private fun onBindingFirebase() {
        databaseReferencehomefeed.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@homefeed.responsehome.add(snapshot.getValue(MphotoModel::class.java)!!)
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

}