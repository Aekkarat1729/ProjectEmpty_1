package com.example.projectempty

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CommentAdapter(private val commentList: MutableList<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemcomment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentList[position]
        holder.bind(currentComment)
    }

    override fun getItemCount() = commentList.size

    fun updateComments(newComments: List<Comment>) {
        commentList.clear()
        commentList.addAll(newComments)
        notifyDataSetChanged()
    }

    fun addComment(comment: Comment) {
        commentList.add(comment)
        notifyItemInserted(commentList.size - 1)
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val UserTextView: TextView = itemView.findViewById(R.id.UserTextView)
        private val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        private val itemProfileComment: ImageView = itemView.findViewById(R.id.itemProfileComment)

        fun bind(comment: Comment) {
            lateinit var database: FirebaseDatabase
            var tempMail = comment.email.toString()
            val myref = Firebase.database.reference
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                    val value = dataSnapshot.getValue(String::class.java)
                    Log.d(ContentValues.TAG, "Value is: $value")
                    UserTextView?.text = value
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException())
                }
            }
            // ดึงข้อมูลแบบ Realtime
            myref.child("Account")
                .child(tempMail)
                .child("Full name").addValueEventListener(postListener)

            database = FirebaseDatabase.getInstance()
            val databaseReference = database.reference.child("Account").child(tempMail).child("Profile")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUrl = dataSnapshot.getValue(String::class.java)
                    if (!imageUrl.isNullOrEmpty()) {
                        // โหลดรูปภาพจาก URL ด้วย Glide หรือ Picasso หรือวิธีอื่นๆ
                        Glide.with(itemView.context)
                            .load(imageUrl)
                            .into(itemProfileComment)
                    } else {
                        // ถ้าไม่มี URL ของรูปภาพ ให้ทำการกำหนดรูปภาพเริ่มต้นหรือตัวแทน
                        itemProfileComment.setImageResource(R.drawable.profile) // เปลี่ยน placeholder_image เป็นรูปภาพที่คุณต้องการให้แสดงเมื่อไม่มีรูปใน Realtime Database
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // กรณีเกิดข้อผิดพลาดในการอ่านค่าจาก Realtime Database
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            })

            commentTextView.text = comment.comment
        }
    }
}
