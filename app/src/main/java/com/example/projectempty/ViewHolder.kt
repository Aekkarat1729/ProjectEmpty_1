package com.example.projectempty

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectempty.R

class ViewHolder (View: View) : RecyclerView.ViewHolder(View){
    var textTitleItem: TextView = View.findViewById(R.id.textTitleItem)
    var imageView: ImageView = View.findViewById(R.id.imageView)
    var cardView:CardView = View.findViewById(R.id.cardView)
}
