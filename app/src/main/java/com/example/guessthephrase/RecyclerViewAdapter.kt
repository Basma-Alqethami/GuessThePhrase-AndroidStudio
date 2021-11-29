package com.example.guessthephrase

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_row.view.*

class RecyclerViewAdapter(private val phrases: ArrayList<String>): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val Phrase = phrases[position]
        holder.itemView.apply {
            textItem.text = Phrase
        }

        var Holder = holder.itemView.findViewById<TextView>(R.id.textItem)

        Holder.text = Phrase
        if (Phrase.contains("Wrong")) {
            Holder.setTextColor(Color.RED)
        } else if (Phrase.contains("Found") || Phrase.contains("No")) {
            Holder.setTextColor(Color.GREEN)
        } else {
            Holder.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount() = phrases.size
}