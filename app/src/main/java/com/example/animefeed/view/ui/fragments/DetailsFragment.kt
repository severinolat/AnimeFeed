package com.example.animefeed.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.animefeed.R
import com.example.animefeed.databinding.FragmentDetailsBinding
import com.example.animefeed.model.Result
import com.example.animefeed.view.ui.MainActivity
import com.example.animefeed.view.ui.viewmodel.AnimeViewModel
import com.google.android.material.snackbar.Snackbar


class DetailsFragment : Fragment() {

    val args: DetailsFragmentArgs by navArgs()
    private lateinit var anime:Result

    lateinit var animeViewModel: AnimeViewModel
    private  var _binding:FragmentDetailsBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val image_url = binding.imgAnime

        //get the current anime that was passed as arguments to this fragment
        anime = args.selectedAnime

        binding.txtTitle.text = anime.title
        binding.txtSynopsis.text = anime.synopsis
        binding.myRatingBar.rating = ((anime.score/2).toFloat())
        binding.textType.text = anime.type
        binding.txtEpisode.text = anime.episodes.toString()

        val url = anime.image_url
        Glide.with(image_url)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.default_thumb)
            .error(R.drawable.default_thumb)
            .fallback(R.drawable.default_thumb)
            .into(image_url)

        animeViewModel = (activity as MainActivity).animeViewModel

        //save anime
        binding.fab.setOnClickListener {
            animeViewModel.saveAnime(anime)
            Snackbar.make(view, "Anime saved successfully", Snackbar.LENGTH_SHORT).show()
        }


        return view
    }


}