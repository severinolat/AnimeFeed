package com.example.animefeed.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animefeed.model.Result
import com.example.animefeed.R
import com.example.animefeed.databinding.ItemAnimeBinding


class AnimeAdapter : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    inner class AnimeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)


    //diffUtil for check difference before update instead of notifydatasetChanged everytime
    private val differCallback = object  : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            //each element have unique mal_id
            return oldItem.mal_id == newItem.mal_id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    //take list and compare by calculate difference
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        return AnimeViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_anime,
            parent,false
        ))
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = differ.currentList[position]
        val binding = ItemAnimeBinding.bind(holder.itemView)
        val title = binding.textName
        val type = binding.textType
        val episode = binding.textEpisode
        val image_url = binding.image
        val rate = binding.myRatingBar
        holder.itemView.apply {
            title.text = anime.title
            type.text = anime.type
            episode.text = anime.episodes.toString()
            rate.rating = ((anime.score/2).toFloat())
            val url = anime.image_url
            Glide.with(image_url)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_thumb)
                .error(R.drawable.default_thumb)
                .fallback(R.drawable.default_thumb)
                .into(image_url)

           setOnClickListener {
               onItemClickListener?.let { it(anime) }
           }
        }
    }

    override fun getItemCount(): Int {
        //get itemcount from listDiffer
        return differ.currentList.size
    }


    //For clic to open detailsFragment
    //take anime and return nothing so Unit
    private var onItemClickListener:((Result) -> Unit)? = null

    fun setOnItemClickListener(listener:(Result)-> Unit){
        onItemClickListener = listener
    }

}