package com.example.projectempty

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
