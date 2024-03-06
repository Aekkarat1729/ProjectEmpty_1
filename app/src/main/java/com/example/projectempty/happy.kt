package com.example.projectempty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class happy : AppCompatActivity() {
    lateinit var databaseReferenceheart: DatabaseReference
    private var homeAdapter: homeAdapter? = null
    lateinit var RecyclerViewsad: RecyclerView
    lateinit var responseheart:MutableList<MphotoModel>
    lateinit var database: FirebaseDatabase
    lateinit var sad_text_wellcome: TextView
    lateinit var mAuth: FirebaseAuth
    var home_happy: Button? = null
    var home_sad: Button? = null
    var home_love: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_happy)
        init()

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        val myref = Firebase.database.reference
        val tempMailUser:String = user?.email.toString().replace(".", "") // ทำการลบจุดออก

        home_sad?.setOnClickListener {
            startActivity(Intent(this@happy, sad::class.java))
        }
        home_happy?.setOnClickListener {
            startActivity(Intent(this@happy, happy::class.java))
        }
        home_love?.setOnClickListener {
            startActivity(Intent(this@happy, love::class.java))
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("happy", "Value is: $value")

                sad_text_wellcome?.setText("Hi! "+value+".")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("happy", "Failed to read value.", databaseError.toException())
            }
        }
        // ดึงข้อมูลแบบ Realtime
        myref.child("Account")
            .child(tempMailUser)
            .child("User name").addValueEventListener(postListener)

        database = FirebaseDatabase.getInstance()
        RecyclerViewsad.layoutManager = GridLayoutManager(this,2)
        databaseReferenceheart = database.getReference("Happy")
        responseheart = mutableListOf()
        homeAdapter = homeAdapter(responseheart as ArrayList<MphotoModel>)
        RecyclerViewsad.adapter = homeAdapter

        onBindingFirebase()
    }
    private fun onBindingFirebase() {
        databaseReferenceheart.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@happy.responseheart.add(snapshot.getValue(MphotoModel::class.java)!!)
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
    fun init(){
        RecyclerViewsad = findViewById(R.id.RecyclerView_happy)
        sad_text_wellcome = findViewById(R.id.happy_text_wellcome)
        home_happy = findViewById(R.id.home_happy)
        home_sad = findViewById(R.id.home_sad)
        home_love = findViewById(R.id.home_love)
    }
}