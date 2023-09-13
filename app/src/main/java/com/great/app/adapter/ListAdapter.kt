package com.great.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.great.app.R
import com.great.app.model.Character
import com.great.app.utils.Formater
import com.squareup.picasso.Picasso

class ListAdapter(
    private val context: Context,
    private var characters: List<Character>
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var characterClickListener: ICharacterClickListener? = null

    fun updateCharacters(
        newCharacters: List<Character>
    ) {
        val oldSize = characters.size
        val countOfInserted = newCharacters.size - characters.size
        characters = newCharacters
        notifyItemRangeInserted(oldSize, countOfInserted)
    }

    fun initCharacterClickListener(listener: ICharacterClickListener) {
        characterClickListener = listener
    }

    override fun onCreateViewHolder(parentLay: ViewGroup, viewType: Int): ListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.lay_character, parentLay, false)
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

    private fun getLayParams(): LinearLayout.LayoutParams {
        val layParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, // width
            LinearLayout.LayoutParams.WRAP_CONTENT  // height
        )
        val dp10 = context.resources.getDimension(R.dimen.dp10).toInt()
        layParams.setMargins(dp10, dp10, dp10, 0)
        return layParams
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val characterImage: ImageView = itemView.findViewById(R.id.img_characterProfile)
        val characterName: TextView = itemView.findViewById(R.id.tv_characterName)

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