package com.example.projectempty

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderComment (View: View) : RecyclerView.ViewHolder(View){
    //KUB สุดหล่อ
    var itemUserProfileComment: ImageView = View.findViewById(R.id.itemUserProfileComment)
    var itemComment: TextView = View.findViewById(R.id.itemComment)
    var itemUserComment: TextView = View.findViewById(R.id.itemUserComment)

}
