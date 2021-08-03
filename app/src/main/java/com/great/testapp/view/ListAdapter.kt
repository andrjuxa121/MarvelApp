package com.great.testapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.great.testapp.R
import com.great.testapp.model.Character
import com.squareup.picasso.Picasso

class ListAdapter(private val context: Context, private val characters: MutableList<Character>):
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val Image: ImageView = itemView.findViewById(R.id.Image)
        val Text: TextView = itemView.findViewById(R.id.Text)

        fun setListener() {
            itemView.setOnClickListener {
                Toast.makeText(it.context, Text.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parentLay: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.
            from(context).inflate(R.layout.lay_character, parentLay, false)
        itemView.layoutParams = getLayParams()
        return ListViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ListViewHolder, id: Int) {
        val character = characters[id]
        Picasso.get().load(character.imageurl).into(holder.Image)
        holder.Text.setText(character.name)
        holder.setListener()
    }
    override fun getItemCount(): Int {
        return characters.size
    }

    private fun getLayParams(): LinearLayout.LayoutParams {
        val layParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, // width
            LinearLayout.LayoutParams.WRAP_CONTENT  // height
        )
        val dp20 = context.resources.getDimension(R.dimen.dp20).toInt()
        layParams.setMargins(dp20, 0, dp20, dp20)
        return layParams
    }
}