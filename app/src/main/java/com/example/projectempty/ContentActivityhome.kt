package com.example.projectempty

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ContentActivityhome : AppCompatActivity() {
    private lateinit var textContentTitle: TextView
    private lateinit var imageViewContent: ImageView
    private lateinit var textContentDetail: TextView
    private lateinit var contentTextViewContact: TextView
    private lateinit var addComment: TextView
    private lateinit var buttonComment: Button
    private lateinit var databaseReferenceComment: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerViewContent: RecyclerView
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_content)

        textContentTitle = findViewById(R.id.textContenttitle)
        imageViewContent = findViewById(R.id.imageViewContent)
        textContentDetail = findViewById(R.id.textContentDetail)
        contentTextViewContact = findViewById(R.id.content_textview_contact)
        addComment = findViewById(R.id.addComment)
        buttonComment = findViewById(R.id.buttonComment)
        recyclerViewContent = findViewById(R.id.RecycleViewContent)

        firebaseAuth = FirebaseAuth.getInstance()

        val intent = intent
        val getkey = intent.getStringExtra("key")
        val tempMail = firebaseAuth.currentUser?.email?.replace(".", "")

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = firebaseDatabase.reference
        val databaseReference = firebaseDatabase.getReference("home/$getkey")

        databaseReferenceComment = firebaseDatabase.getReference("home/$getkey/Comment")

        commentAdapter = CommentAdapter(ArrayList())
        recyclerViewContent.adapter = commentAdapter

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textContentTitle.text = snapshot.child("title").value.toString()
                textContentDetail.text = snapshot.child("detail").value.toString()

                val email = snapshot.child("email").value.toString()
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java)
                        contentTextViewContact.text = value
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        databaseError.toException().let {
                            Toast.makeText(this@ContentActivityhome, "Failed to read value.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                myRef.child("Account")
                    .child(email)
                    .child("Full name").addValueEventListener(postListener)

                Picasso.get().load(snapshot.child("Image").value.toString())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewContent)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().let {
                    Toast.makeText(this@ContentActivityhome, "Failed to read data.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        buttonComment.setOnClickListener {
            val commentText = addComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                val currentUserEmail = firebaseAuth.currentUser?.email?.replace(".", "")
                databaseReferenceComment.push().child(currentUserEmail.toString()).setValue(commentText)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show()
                        addComment.text = ""
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add comment.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
