package com.example.projectempty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ContentActivityMphoto : AppCompatActivity() {
    lateinit var textContenttitle:TextView
    lateinit var imageViewContent:ImageView
    lateinit var textContentDetail:TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_content)


        textContenttitle = findViewById(R.id.textContenttitle)
        imageViewContent = findViewById(R.id.imageViewContent)
        textContentDetail = findViewById(R.id.textContentDetail)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        var intent = intent
        var getkey = intent.getStringExtra("key")


        var databaseReference = firebaseDatabase.getReference("Mphoto/$getkey")
        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                textContenttitle.text = snapshot.child("title").value.toString()
                textContentDetail.text = snapshot.child("detail").value.toString()
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