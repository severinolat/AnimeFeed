package com.example.animefeed.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animefeed.R
import com.example.animefeed.databinding.FragmentSearchBinding
import com.example.animefeed.utils.Constants.Companion.SEARCH_TIME_DELAY
import com.example.animefeed.utils.Resource
import com.example.animefeed.view.adapter.AnimeAdapter
import com.example.animefeed.view.ui.MainActivity
import com.example.animefeed.view.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    lateinit var animeViewModel: AnimeViewModel
    lateinit var animeAdapter: AnimeAdapter
    private  var _binding: FragmentSearchBinding?=null
    val TAG = "HomeFragment"


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        hideProgressBar()

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
                R.id.action_searchFragment_to_detailsFragment,bundle
            )
        }


        var job: Job? = null
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_TIME_DELAY)
                    query?.let {
                        if (query.toString().isNotEmpty()){
                            animeViewModel.searchAnimes(query.toString())
                        }
                    }
                }
                return false
            }

        })

        //subscribe to live data
        animeViewModel.searchAnimes.observe(viewLifecycleOwner, Observer { response ->
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