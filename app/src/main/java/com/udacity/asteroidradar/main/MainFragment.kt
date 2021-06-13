package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.util.AsteroidAdapter
import com.udacity.asteroidradar.util.AsteroidListener
import com.udacity.asteroidradar.util.Constants.DEFAULT_FAKE_VALUE
import com.udacity.asteroidradar.util.Constants.FAKE1_CODENAME
import com.udacity.asteroidradar.util.Constants.FAKE1_DATE
import com.udacity.asteroidradar.util.Constants.FAKE1_ID
import com.udacity.asteroidradar.util.Constants.FAKE2_CODENAME
import com.udacity.asteroidradar.util.Constants.FAKE2_DATE
import com.udacity.asteroidradar.util.Constants.FAKE2_ID
import com.udacity.asteroidradar.util.Constants.FAKE3_CODENAME
import com.udacity.asteroidradar.util.Constants.FAKE3_DATE
import com.udacity.asteroidradar.util.Constants.FAKE3_ID
import com.udacity.asteroidradar.util.Constants.FAKE4_CODENAME
import com.udacity.asteroidradar.util.Constants.FAKE4_DATE
import com.udacity.asteroidradar.util.Constants.FAKE4_ID
import com.udacity.asteroidradar.util.Constants.FAKE5_CODENAME
import com.udacity.asteroidradar.util.Constants.FAKE5_DATE
import com.udacity.asteroidradar.util.Constants.FAKE5_ID

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        viewModel.getPictureOfTheDay()

        val adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            val args = Bundle()
            args.putParcelable("selectedAsteroid", asteroid)
            findNavController().navigate(R.id.action_showDetail, args)
        })

        /* This is a list of 5 fake asteroids that are shown before is loaded data from cache
         or data is missing and it needs to download from API */
        adapter.submitList(listOf(
                Asteroid(FAKE1_ID, FAKE1_CODENAME, FAKE1_DATE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, true),
                Asteroid(FAKE2_ID, FAKE2_CODENAME, FAKE2_DATE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, false),
                Asteroid(FAKE3_ID, FAKE3_CODENAME, FAKE3_DATE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, true),
                Asteroid(FAKE4_ID, FAKE4_CODENAME, FAKE4_DATE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, false),
                Asteroid(FAKE5_ID, FAKE5_CODENAME, FAKE5_DATE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, DEFAULT_FAKE_VALUE, false)
        ))

        binding.statusLoadingWheel.visibility = View.VISIBLE
        binding.asteroidRecycler.adapter = adapter
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pictureResponse.observe(viewLifecycleOwner, Observer { responseValue ->
            if (responseValue == null || !responseValue.mediaType.contains("image")) {
                binding.statusLoadingWheel.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.image_of_the_day_not_loaded), Toast.LENGTH_SHORT).show()
                binding.activityMainImageOfTheDay.contentDescription = resources.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
            } else {
                Picasso.get().load(responseValue.url).into(binding.activityMainImageOfTheDay)
                binding.statusLoadingWheel.visibility = View.GONE
                binding.activityMainImageOfTheDay.contentDescription = String.format(resources.getString(R.string.nasa_picture_of_day_content_description_format), responseValue.title)
            }
        })

        viewModel.asteroidsResponse.observe(viewLifecycleOwner, Observer { responseValue ->
            if (responseValue.isNullOrEmpty()) {
                Toast.makeText(requireContext(), resources.getString(R.string.asteroid_list_not_loaded), Toast.LENGTH_LONG).show()
            } else {
                (binding.asteroidRecycler.adapter as AsteroidAdapter).submitList(responseValue)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_asteroids -> {
                viewModel.getDailyAsteroids()
            }
            R.id.show_week_asteroids -> {
                viewModel.getWeeklyAsteroids()
            }
        }
        return true
    }
}
