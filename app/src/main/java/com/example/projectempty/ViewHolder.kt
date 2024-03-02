package com.example.projectempty

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder (View: View) : RecyclerView.ViewHolder(View){
    //KUB สุดหล่อ
    var textTitleItem: TextView = View.findViewById(R.id.textTitleItem)
    var imageView: ImageView = View.findViewById(R.id.imageView)
    var cardView:CardView = View.findViewById(R.id.cardView)
    var textUseritem: TextView = View.findViewById(R.id.textUseritem)
    var itemUser: ImageView = View.findViewById(R.id.itemUser)
}
