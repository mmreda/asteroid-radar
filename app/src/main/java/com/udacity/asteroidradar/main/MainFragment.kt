package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.MainViewModel.NasaApiStatus

@Suppress("DEPRECATION")
class MainFragment : Fragment() {

    private lateinit var adapter: AsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            ""
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(
                activity.application
            )
        )[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener{
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToDetails.observe(viewLifecycleOwner){
            if (it != null) {
                findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsDone()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_today -> {
                viewModel.getTodayAsteroids().observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }

            R.id.show_next_week -> {
                viewModel.getWeekAsteroids().observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }

            R.id.show_all_asteroids -> {
                viewModel.asteroidList.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        }
        return true
    }
}
