package com.example.animefeed.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animefeed.R
import com.example.animefeed.databinding.FragmentHomeBinding
import com.example.animefeed.utils.Resource
import com.example.animefeed.view.adapter.AnimeAdapter
import com.example.animefeed.view.ui.MainActivity
import com.example.animefeed.view.ui.viewmodel.AnimeViewModel


class HomeFragment : Fragment() {

    lateinit var animeViewModel: AnimeViewModel
    lateinit var animeAdapter: AnimeAdapter
    private  var _binding: FragmentHomeBinding?=null
    val TAG = "HomeFragment"


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //set ViewModel to our fragment

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set viewModels to fragment activity
        //and we cast that as MainActivity so that we can have access to the view model created at MainActivity
        animeViewModel = (activity as MainActivity).animeViewModel

        setUpRecyclerView()

        animeAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                //adding parcelable anime object class to the bundle
                putParcelable("selectedAnime", it)
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment,bundle
            )
        }


        //subscribe to live data
        animeViewModel.animes.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    //check null
                    response.data?.let { animesResponse ->
                        animeAdapter.differ.submitList(animesResponse.results)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"An error occured: $message", Toast.LENGTH_LONG ).show()

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private  fun setUpRecyclerView(){
        animeAdapter = AnimeAdapter()
        binding.recyclerView.apply {
            adapter = animeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }


    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }






}