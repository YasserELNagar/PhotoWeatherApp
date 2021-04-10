package com.example.robustatask.ui.weatherHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.robustatask.R
import com.example.robustatask.databinding.FragmentWeatherHistoryBinding
import com.example.robustatask.databinding.FragmentWeatherInfoBinding
import com.example.robustatask.db.AppDB
import com.example.robustatask.model.weatherHistory.WeatherHistoryItem
import com.example.robustatask.shared.SimpleItemClickListener
import com.example.robustatask.shared.helper.zoom
import com.example.robustatask.ui.weatherHistory.adapter.WeatherHistoryItemsAdapter
import com.example.robustatask.ui.weatherInfo.WeatherInfoFragmentDirections
import java.text.FieldPosition

class WeatherHistoryFragment : Fragment() {


    private lateinit var binding: FragmentWeatherHistoryBinding
    private lateinit var viewModel: WeatherHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherHistoryBinding.inflate(inflater)
        initViewModel()
        observeOpenImageInFullScreen()
        observeOpenSharingDialog()
        setupHistoryItemsRv()
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        return binding.root
    }



    private fun initViewModel() {
        val weatherDao = AppDB(requireContext()).weatherDao()
        val repository = WeatherHistoryRepository(weatherDao)
        val viewModelFactory= WeatherHistoryViewModelFactory(requireActivity().application,repository)
        viewModel = ViewModelProvider(this,viewModelFactory)[WeatherHistoryViewModel::class.java]
    }


    private fun observeOpenImageInFullScreen() {
        viewModel.openImageInFullScreen.observe(viewLifecycleOwner, Observer { position ->
            position?.let {
                openImageInFullScreen(it)
            }
        })
    }

    private fun openImageInFullScreen(position: Int) {
        val imageUrls = viewModel.weatherItemsList.value!!.map {
            it.photoPath
        }
        requireActivity().zoom(position, imageUrls as List<String>)
    }

    private fun observeOpenSharingDialog() {
        viewModel.openShareDialog.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                navigateToSharingDialog(it)
            }
        })
    }

    private fun navigateToSharingDialog(item: WeatherHistoryItem) {
        val action = WeatherHistoryFragmentDirections.actionWeatherHistoryFragmentToShareBottomSheetFragment(item)
        findNavController().navigate(action)

    }

    private fun setupHistoryItemsRv() {
        binding.rvWeatherListItem.apply {
            adapter = WeatherHistoryItemsAdapter(object :WeatherHistoryItemsAdapter.WeatherHistoryItemClickListener{
                override fun onItemClick(position: Int) {
                    viewModel.onWeatherItemClick(position)
                }

                override fun onShareClick(position: Int) {
                    viewModel.onShareClick(position)
                }

            })
        }
    }

}