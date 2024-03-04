package com.example.projectempty

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class homeAdapter(val MphotoList: List<MphotoModel>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemhome, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = MphotoList[position]
        lateinit var database: FirebaseDatabase
        val tempMail = dataModel.email
        var mAuth: FirebaseAuth? = null
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        val tempMailUser:String = user?.email.toString().replace(".", "") // ทำการลบจุดออก

        holder.textTitleItem.text = dataModel.title

        //user
        val myref = Firebase.database.reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(ContentValues.TAG, "Value is: $value")
                holder.textUseritem?.text = value
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException())
            }
        }
        // ดึงข้อมูลแบบ Realtime
        myref.child("Account")
            .child(tempMail.toString())
            .child("Full name").addValueEventListener(postListener)

        Picasso.get().load(dataModel.Image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)

        //Profile kub
        val databaseReferenceAccount = Firebase.database.reference.child("Account").child(tempMail.toString())
        databaseReferenceAccount.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileUrl = snapshot.child("Profile").value.toString()
                Picasso.get().load(profileUrl)
                    .error(R.drawable.profile)
                    .placeholder(R.drawable.profile)
                    .into(holder.itemUser)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("homeAdapter", "Failed to retrieve user profile image: ${error.message}")
            }
        })

        holder.cardView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ContentActivityhome::class.java)
            intent.putExtra("key", dataModel.key) // ส่ง key ของโพสไปยัง ContentActivityhome
            holder.itemView.context.startActivity(intent)
        }

        // itemLike, currentUserLikeRef, likeCountRef ไม่ได้เปลี่ยนแปลงจากเดิม เรียกใช้เหมือนเดิม

    // itemLike
        holder.itemLike.setOnClickListener {
            if (dataModel.isLiked) {
                // ถ้าไลค์อยู่แล้วให้ยกเลิกไลค์
                dataModel.isLiked = false

                // ลบโหนด tempMailUser ออก
                val likeRefHome = Firebase.database.reference
                    .child("home")
                    .child(dataModel.key.toString())
                    .child("Like")
                    .child(tempMailUser)

                val likeRefAccount = Firebase.database.reference
                    .child("Account")
                    .child(tempMail.toString())
                    .child("Posts")
                    .child(dataModel.key.toString())
                    .child("Like")
                    .child(tempMailUser)

                likeRefHome.removeValue()
                likeRefAccount.removeValue()
            } else {
                // ถ้ายังไม่ได้ไลค์ให้ทำการไลค์
                dataModel.isLiked = true

                // เพิ่มโหนด tempMailUser เข้าไป
                val likeRefHome = Firebase.database.reference
                    .child("home")
                    .child(dataModel.key.toString())
                    .child("Like")
                    .child(tempMailUser)

                val likeRefAccount = Firebase.database.reference
                    .child("Account")
                    .child(tempMail.toString())
                    .child("Posts")
                    .child(dataModel.key.toString())
                    .child("Like")
                    .child(tempMailUser)

                likeRefHome.setValue("Like")
                likeRefAccount.setValue("Like")

                database = FirebaseDatabase.getInstance()
                val databaseReference = database.reference.child("Account")
                    .child(tempMailUser)
                    .child("Posts")
                    .child(dataModel.key.toString())
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var Image = dataSnapshot.child("Image").value.toString()
                        var detail = dataSnapshot.child("detail").value.toString()
                        var email = dataSnapshot.child("email").value.toString()
                        var key = dataSnapshot.child("key").value.toString()
                        var title = dataSnapshot.child("title").value.toString()

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // กรณีเกิดข้อผิดพลาดในการอ่านค่าจาก Realtime Database
                        Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                    }
                })
            }

            // เปลี่ยนรูปภาพของ itemLike ตามสถานะปัจจุบันของไลค์
            val currentImageResource = if (dataModel.isLiked) {
                R.drawable.fullheart // ถ้าถูกใจแล้วใช้รูปภาพเต็มหัวใจ
            } else {
                R.drawable.icon_heart // ถ้ายังไม่ถูกใจใช้รูปภาพหัวใจว่างเปล่า
            }
            holder.itemLike.setImageResource(currentImageResource)

        }

    // ตรวจสอบว่าผู้ใช้เป็นส่วนหนึ่งของ Like หรือไม่
        val currentUserLikeRef = Firebase.database.reference
            .child("Account")
            .child(tempMail.toString())
            .child("Posts")
            .child(dataModel.key.toString())
            .child("Like")
            .child(tempMailUser)

        currentUserLikeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // ถ้ามี email ของผู้ใช้อยู่ใน Like
                    holder.itemLike.setImageResource(R.drawable.fullheart) // ใช้ใจเต็ม
                } else {
                    // ถ้าไม่มี email ของผู้ใช้อยู่ใน Like
                    holder.itemLike.setImageResource(R.drawable.icon_heart) // ใช้ใจว่าง
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("homeAdapter", "Failed to check user like: ${error.message}")
            }
        })

// ดึงข้อมูลจาก Firebase Realtime Database เพื่อนับจำนวนไลค์
        val likeCountRef = Firebase.database.reference
            .child("Account")
            .child(tempMail.toString())
            .child("Posts")
            .child(dataModel.key.toString())
            .child("Like")

        likeCountRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likeCount = snapshot.childrenCount.toInt() // นับจำนวนโหนดทั้งหมดภายใน "Like"
                holder.itemcountLike.text = likeCount.toString() + " Like" // แสดงจำนวนไลค์ใน TextView
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("homeAdapter", "Failed to retrieve like count: ${error.message}")
            }
        })

    }

    override fun getItemCount(): Int {
        return MphotoList.size
    }
}
