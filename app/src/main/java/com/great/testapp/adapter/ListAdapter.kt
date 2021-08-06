package com.great.testapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.great.testapp.R
import com.great.testapp.model.Character
import com.great.testapp.utils.Formater
import com.squareup.picasso.Picasso

class ListAdapter(private val context: Context, private val characters: List<Character>):
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    interface iListener {
        fun onClick(character: Character)
    }
    private var Listener: iListener? = null
    fun setListener(listener: iListener) {
        Listener = listener
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val Image: ImageView = itemView.findViewById(R.id.Image)
        val Text: TextView = itemView.findViewById(R.id.Text)

        fun setListener(character: Character) {
            itemView.setOnClickListener {
                Listener?.onClick(character)
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
        Picasso.get().load(Formater.getImageUrl(character.thumbnail)).into(holder.Image)
        holder.Text.setText(character.name)
        holder.setListener(character)
    }
    override fun getItemCount(): Int {
        return characters.size
    }

    private fun getLayParams(): LinearLayout.LayoutParams {
        val layParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, // width
            LinearLayout.LayoutParams.WRAP_CONTENT  // height
        )
        val dp10 = context.resources.getDimension(R.dimen.dp10).toInt()
        layParams.setMargins(dp10, dp10, dp10, 0)
        return layParams
    }
}