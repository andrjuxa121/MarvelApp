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

    private var characterClickListener: ICharacterClickListener? = null

    fun initCharacterClickListener(listener: ICharacterClickListener) {
        characterClickListener = listener
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

    override fun onCreateViewHolder(parentLay: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.
            from(context).inflate(R.layout.lay_character, parentLay, false)
        itemView.layoutParams = getLayParams()
        return ListViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ListViewHolder, id: Int) {
        val character = characters[id]
        Picasso.get().load(Formater.getImageUrl(character.thumbnail)).into(holder.characterImage)
        holder.characterName.text = character.name
        holder.initCharacterClickListener(character)
    }
    override fun getItemCount(): Int {
        return characters.size
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val characterImage: ImageView = itemView.findViewById(R.id.image)
        val characterName: TextView = itemView.findViewById(R.id.name)

        fun initCharacterClickListener(character: Character) {
            itemView.setOnClickListener {
                characterClickListener?.onClick(character)
            }
        }
    }

    interface ICharacterClickListener {
        fun onClick(character: Character)
    }
}