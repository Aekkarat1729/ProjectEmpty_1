package com.example.projectempty

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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

        // การเปลี่ยนรูปภาพของ itemLike เมื่อมีการคลิก
        holder.itemLike.setOnClickListener {
            // ตรวจสอบสถานะปัจจุบันของไลค์
            val currentLikes = dataModel.likes ?: 0 // หากไม่มีค่าให้เริ่มต้นที่ 0

            if (dataModel.isLiked) {
                // ถ้าไลค์อยู่แล้วให้ยกเลิกไลค์
                dataModel.isLiked = false
                dataModel.likes = currentLikes - 1
            } else {
                // ถ้ายังไม่ได้ไลค์ให้ทำการไลค์
                dataModel.isLiked = true
                dataModel.likes = currentLikes + 1
            }

            // อัปเดตข้อมูลไลค์ใน Firebase Realtime Database
            val likeRef = Firebase.database.reference
                .child("Account")
                .child(tempMail.toString())
                .child("Posts")
                .child(dataModel.key.toString())
                .child("Like")
                .child("Total User").push()

            likeRef.setValue(tempMailUser)

            // เปลี่ยนรูปภาพของ itemLike ตามสถานะปัจจุบันของไลค์
            val currentImageResource = if (dataModel.isLiked) {
                R.drawable.fullheart // ถ้าถูกใจแล้วใช้รูปภาพเต็มหัวใจ
            } else {
                R.drawable.icon_heart // ถ้ายังไม่ถูกใจใช้รูปภาพหัวใจว่างเปล่า
            }
            holder.itemLike.setImageResource(currentImageResource)
        }


        holder.cardView.setOnClickListener(View.OnClickListener { view ->
            val readActivity = Intent(view.context, ContentActivityhome::class.java)
            readActivity.putExtra("key", dataModel.key)
            view.context.startActivity(readActivity)
            Log.d("M planner", dataModel.title.toString())
        })
    }


    override fun getItemCount(): Int {
        return MphotoList.size
    }
}
