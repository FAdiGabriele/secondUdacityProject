package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.util.AsteroidAdapter
import com.udacity.asteroidradar.util.AsteroidListener

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        viewModel.getPictureOfTheDay()
        viewModel.getAsteroidList()

        val adapter = AsteroidAdapter(AsteroidListener{ asteroid ->
            val args = Bundle()
            args.putParcelable("selectedAsteroid", asteroid)
            findNavController().navigate(R.id.action_showDetail, args)
        })
        //TODO: aggiorna con i valori presi dalla cache
        adapter.submitList(listOf())

        binding.asteroidRecycler.adapter = adapter
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pictureResponse.observe(viewLifecycleOwner, Observer {responseValue ->
            if(responseValue.isBlank() || responseValue.contains("Failure: ")){
                //TODO vedi cosa fare
            }else{
                    Picasso.get().load(responseValue).into(binding.activityMainImageOfTheDay)
            }
        })

        viewModel.asteroidsResponse.observe(viewLifecycleOwner, Observer {responseValue ->
            if(responseValue[0].codename.contains("Failure") || responseValue.isEmpty()){
                    //todo vedi cosa fare
            }else{
                    (binding.asteroidRecycler.adapter as AsteroidAdapter).submitList(responseValue)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
