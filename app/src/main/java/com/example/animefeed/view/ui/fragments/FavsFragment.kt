package com.example.animefeed.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animefeed.R
import com.example.animefeed.databinding.FragmentFavsBinding
import com.example.animefeed.view.adapter.AnimeAdapter
import com.example.animefeed.view.ui.MainActivity
import com.example.animefeed.view.ui.viewmodel.AnimeViewModel
import com.google.android.material.snackbar.Snackbar


class FavsFragment : Fragment() {

    lateinit var animeViewModel: AnimeViewModel
    lateinit var animeAdapter: AnimeAdapter
    private  var _binding: FragmentFavsBinding?=null
    val TAG = "FavsFragment"


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavsBinding.inflate(inflater, container, false)
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
                R.id.action_favsFragment_to_detailsFragment,bundle
            )
        }

        //implement actions
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val anime = animeAdapter.differ.currentList[position]
                animeViewModel.deleteAnime(anime)
                Snackbar.make(view,"Successfully delete anime", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        animeViewModel.saveAnime(anime)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }

        animeViewModel.getSavdAnime().observe(viewLifecycleOwner, Observer { animes ->
            animeAdapter.differ.submitList(animes)
        })
    }

    private  fun setUpRecyclerView(){
        animeAdapter = AnimeAdapter()
        binding.recyclerView.apply {
            adapter = animeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }





}