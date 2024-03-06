package com.example.projectempty

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContentActivityhome : AppCompatActivity() {
    private lateinit var textContentTitle: TextView
    private lateinit var imageViewContent: ImageView
    private lateinit var textContentDetail: TextView
    private lateinit var contentTextViewContact: TextView
    private lateinit var addComment: TextView
    private lateinit var content_image_profile: ImageView
    private lateinit var buttonComment: Button
    private lateinit var databaseReferenceComment: DatabaseReference
    private lateinit var databaseReferenceCommentAccount: DatabaseReference
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
        content_image_profile = findViewById(R.id.content_image_profile)

        firebaseAuth = FirebaseAuth.getInstance()

        val intent = intent
        val tempMail = firebaseAuth.currentUser?.email?.replace(".", "")

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = firebaseDatabase.reference


        commentAdapter = CommentAdapter(ArrayList())
        recyclerViewContent.adapter = commentAdapter

        val getkey = intent.getStringExtra("key")
        val databaseReference = firebaseDatabase.getReference("home/$getkey")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textContentTitle.text = snapshot.child("title").value.toString()
                textContentDetail.text = snapshot.child("detail").value.toString()
                var ImageUrl:String = snapshot.child("Image").value.toString()

                if (!ImageUrl.isNullOrEmpty()) {
                    // โหลดรูปภาพจาก URL ด้วย Glide หรือ Picasso หรือวิธีอื่นๆ
                    Glide.with(this@ContentActivityhome)
                        .load(ImageUrl)
                        .into(imageViewContent)
                } else {
                    // ถ้าไม่มี URL ของรูปภาพ ให้ทำการกำหนดรูปภาพเริ่มต้นหรือตัวแทน
                   imageViewContent.setImageResource(R.drawable.placeholder) // เปลี่ยน placeholder_image เป็นรูปภาพที่คุณต้องการให้แสดงเมื่อไม่มีรูปใน Realtime Database
                }

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

                val postListener1 = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java)
                        if (!ImageUrl.isNullOrEmpty()) {
                            // โหลดรูปภาพจาก URL ด้วย Glide หรือ Picasso หรือวิธีอื่นๆ
                            Glide.with(this@ContentActivityhome)
                                .load(value.toString())
                                .into(content_image_profile)
                        } else {
                            // ถ้าไม่มี URL ของรูปภาพ ให้ทำการกำหนดรูปภาพเริ่มต้นหรือตัวแทน
                            content_image_profile.setImageResource(R.drawable.profile) // เปลี่ยน placeholder_image เป็นรูปภาพที่คุณต้องการให้แสดงเมื่อไม่มีรูปใน Realtime Database
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        databaseError.toException().let {
                            Toast.makeText(this@ContentActivityhome, "Failed to read value.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                myRef.child("Account")
                    .child(email)
                    .child("Profile").addValueEventListener(postListener1)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().let {
                    Toast.makeText(this@ContentActivityhome, "Failed to read data.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        databaseReferenceComment = firebaseDatabase.getReference("home/$getkey/Comment")


        buttonComment.setOnClickListener {
            val commentText = addComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                val currentUserEmail = firebaseAuth.currentUser?.email?.replace(".", "")
                val databaseReferenceCommentPush = databaseReferenceComment.push()
                databaseReferenceCommentPush.child("email").setValue(currentUserEmail.toString())
                    .addOnSuccessListener {
                        databaseReferenceCommentPush.child("comment").setValue(commentText)
                        Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show()
                        addComment.text = ""

                        // เพิ่ม comment ลงในโครงสร้างข้อมูลของผู้ใช้
                        val accountRef = firebaseDatabase.getReference("Account").child(tempMail.toString())
                        val userPostRef = accountRef.child("Posts").child(getkey.toString()).child("Comments")
                            .child(databaseReferenceCommentPush.key.toString())
                        userPostRef.child("email").setValue(currentUserEmail.toString())
                        userPostRef.child("Comment").setValue(commentText)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add comment.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }


        val commentList = mutableListOf<Comment>()
        commentAdapter = CommentAdapter(commentList)
        recyclerViewContent.layoutManager = GridLayoutManager(this@ContentActivityhome, 2)
        recyclerViewContent.adapter = commentAdapter

        databaseReferenceComment.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = mutableListOf<Comment>()
                for (commentSnapshot in snapshot.children) {
                    val email = commentSnapshot.child("email").getValue(String::class.java)
                    val comment = commentSnapshot.child("comment").getValue(String::class.java)
                    email?.let { email ->
                        comment?.let { comment ->
                            val newComment = Comment(email, comment)
                            comments.add(newComment)
                        }
                    }
                }
                commentAdapter.updateComments(comments)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ContentActivityhome, "Failed to read comments.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
