package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.util.AsteroidAdapter
import com.udacity.asteroidradar.util.AsteroidListener

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        viewModel.getPictureOfTheDay()
        viewModel.getAsteroidList()

        binding.asteroidRecycler.adapter =
            AsteroidAdapter(AsteroidListener { asteroid ->
                val args = Bundle()
                args.putParcelable("selectedAsteroid", asteroid)
                findNavController().navigate(R.id.action_showDetail, args)
            })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.response.observe(viewLifecycleOwner) {responseValue ->
            if(responseValue.isBlank()){
                //TODO vedi cosa fare
            }else{
                if(responseValue.contains("Failure: ")){
                    //TODO vedi cosa fare
                }else{
                    Picasso.with(context).load(responseValue).into(binding.activityMainImageOfTheDay);
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
