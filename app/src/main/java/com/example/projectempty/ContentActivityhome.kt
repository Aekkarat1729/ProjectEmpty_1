package com.example.projectempty

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ContentActivityhome : AppCompatActivity() {
    lateinit var textContenttitle:TextView
    lateinit var imageViewContent:ImageView
    lateinit var textContentDetail:TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var content_textview_contact:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_content)


        textContenttitle = findViewById(R.id.textContenttitle)
        imageViewContent = findViewById(R.id.imageViewContent)
        textContentDetail = findViewById(R.id.textContentDetail)
        content_textview_contact = findViewById(R.id.content_textview_contact)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        var intent = intent
        var getkey = intent.getStringExtra("key")
        val myref = Firebase.database.reference

        var databaseReference = firebaseDatabase.getReference("home/$getkey")
        var databaseReferenceAccount = firebaseDatabase.getReference("Account")

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                textContenttitle.text = snapshot.child("title").value.toString()
                textContentDetail.text = snapshot.child("detail").value.toString()

                var tempMail:String = snapshot.child("email").value.toString()
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                        val value = dataSnapshot.getValue(String::class.java)
                        Log.d(TAG, "Value is: $value")
                        content_textview_contact?.text = value
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException())
                    }
                }

                // ดึงข้อมูลแบบ Realtime
                myref.child("Account")
                    .child(tempMail)
                    .child("Full name").addValueEventListener(postListener)


                Picasso.get().load(snapshot.child("Image").value.toString())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewContent)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}