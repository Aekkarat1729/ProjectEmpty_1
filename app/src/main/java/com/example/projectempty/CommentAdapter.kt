package com.example.projectempty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(val commentList: MutableList<String>) : RecyclerView.Adapter<ViewHolderComment>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderComment {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemcomment, parent, false)
        return ViewHolderComment(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderComment, position: Int) {
        val currentItem = commentList[position]
        holder.itemComment.text = currentItem
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    // เมทอดสำหรับอัปเดตข้อมูลใน commentList
    fun updateData(newList: List<String>) {
        commentList.clear()
        commentList.addAll(newList)
        notifyDataSetChanged()
    }
}
