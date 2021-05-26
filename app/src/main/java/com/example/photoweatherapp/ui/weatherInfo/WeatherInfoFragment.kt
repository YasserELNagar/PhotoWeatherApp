package com.example.photoweatherapp.ui.weatherInfo

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.photoweatherapp.R
import com.example.photoweatherapp.databinding.FragmentWeatherInfoBinding
import com.example.photoweatherapp.db.AppDB
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import com.example.photoweatherapp.shared.LOCATION_PERMISSION_REQUEST_CODE
import com.example.photoweatherapp.shared.Validations
import com.example.photoweatherapp.shared.apiWrapper.Error
import com.example.photoweatherapp.shared.apiWrapper.Status
import com.example.photoweatherapp.shared.checkLocationPermissions
import com.example.photoweatherapp.shared.helper.Location
import com.example.photoweatherapp.shared.helper.checkGps
import com.example.photoweatherapp.ui.weatherInfo.WeatherInfoFragmentArgs
import com.example.photoweatherapp.ui.weatherInfo.WeatherInfoFragmentDirections
import com.google.android.material.snackbar.Snackbar


class WeatherInfoFragment : Fragment() {

    private lateinit var binding: FragmentWeatherInfoBinding

    private lateinit var viewModel: WeatherInfoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherInfoBinding.inflate(inflater)
        initViewModel()
        observeCurrentWeatherApi()
        observeValidateInput()
        observeOpenSharingDialog()
        observeGoBack()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (requireActivity().checkGps()&&requireActivity().checkLocationPermissions()) {
            observeGettingCurrentLocation()
        }
    }
    private fun initViewModel() {
        val args by navArgs<WeatherInfoFragmentArgs>()
        val weatherDao = AppDB(requireContext()).weatherDao()
        val repository = WeatherInfoRepository(requireContext(), weatherDao)
        val viewModelFactory =
            WeatherInfoViewModelFactory(requireActivity().application, repository, args.weatherItem)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherInfoViewModel::class.java]
    }

    private fun observeGettingCurrentLocation() {
        Location.getMyLocation().observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                viewModel.fetchCurrentWeatherData(location.latitude,location.longitude)
            }
        })
    }

    private fun observeOpenSharingDialog() {
        viewModel.openShareDialog.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                navigateToSharingDialog(it)
            }
        })
    }

    private fun observeValidateInput() {
        viewModel.validateInput.observe(viewLifecycleOwner, Observer { validation ->
            validation?.let {
                when (it) {
                    Validations.LOCATION_EMPTY -> {
                        binding.edtCity.error = getString(R.string.required)
                    }
                    Validations.TEMP_EMPTY -> {
                        binding.edtTemp.error = getString(R.string.required)
                    }
                    Validations.WEATHER_CONDITION_EMPTY -> {
                        binding.edtCondition.error = getString(R.string.required)
                    }
                }
            }
        })
    }

    private fun observeGoBack() {
        viewModel.goBack.observe(viewLifecycleOwner, Observer { goBack ->
            if (goBack) {
                activity?.onBackPressed()
            }
        })
    }


    private fun observeCurrentWeatherApi() {
        viewModel.currentWeatherApiWrapper.observe(viewLifecycleOwner, Observer { Resource ->
            Resource?.let {
                when (Resource.status) {
                    Status.ERROR -> {
                        hideLoading()
                        handleError(Resource.error)
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                    }
                }
            }
        })
    }

    private fun navigateToSharingDialog(item: WeatherHistoryItem) {
        val action =
            WeatherInfoFragmentDirections.actionWeatherInfoFragmentToShareBottomSheetFragment(item)
        findNavController().navigate(action)

    }




    private fun handleError(error: Error?) {
        error?.let {
            Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun hideLoading() {
        binding.isLoading=false
    }

    private fun showLoading() {
        binding.isLoading=true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionDenied = true
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            for (permission in grantResults) {
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    permissionDenied = false
                    break
                }
            }
        }


        if (!permissionDenied) {
            observeGettingCurrentLocation()
        }
    }

}