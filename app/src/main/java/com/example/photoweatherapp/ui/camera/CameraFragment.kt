package com.example.photoweatherapp.ui.camera

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.photoweatherapp.databinding.FragmentCameraBinding
import com.example.photoweatherapp.model.weatherHistory.WeatherHistoryItem
import com.example.photoweatherapp.shared.CAMERA_PERMISSION_REQUEST_CODE
import com.example.photoweatherapp.shared.checkCameraPermission
import com.example.photoweatherapp.shared.helper.getPhotoFile
import com.example.photoweatherapp.ui.camera.CameraFragmentDirections
import com.google.common.util.concurrent.ListenableFuture
import timber.log.Timber


class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var mFutureCameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var mImageCapture: ImageCapture
    private val viewModel : CameraViewModel by lazy {
        ViewModelProvider(this)[CameraViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater)
        if (requireActivity().checkCameraPermission()) {
            initCameraProvider()
        }
        observeTakeNewPhoto()
        observeNavigateToWeatherInfo()
        observeNavigateToWeatherHistory()
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        return binding.root
    }

    private fun observeNavigateToWeatherHistory() {
        viewModel.navigateToWeatherHistory.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                navigateToWeatherHistory()
            }
        })
    }

    private fun observeNavigateToWeatherInfo() {
        viewModel.navigateToWeatherInfo.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                navigateToWeatherInfo(item)
            }
        })
    }

    private fun observeTakeNewPhoto() {
        viewModel.takeNewPhoto.observe(viewLifecycleOwner, Observer { photoName ->
            photoName?.let {
                takePhoto(it)
            }
        })
    }

    private fun navigateToWeatherHistory() {
        val action = CameraFragmentDirections.actionCameraFragmentToWeatherHistoryFragment()
        findNavController().navigate(action)
    }

    private fun initCameraProvider() {
        mFutureCameraProvider = ProcessCameraProvider.getInstance(requireContext())
        mFutureCameraProvider.addListener(Runnable {
            val cameraProvider = mFutureCameraProvider.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        //get preview surface provider
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.previewView.createSurfaceProvider())
        }


        //set the back camera as the default
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        mImageCapture = ImageCapture.Builder().build()

        try {
            // Unbind use cases before rebinding
            cameraProvider?.unbindAll()

            // Bind use cases to camera
            cameraProvider?.bindToLifecycle(
                this, cameraSelector, preview,mImageCapture
            )

        } catch (exc: Exception) {
            Timber.e("Use case binding failed $exc")
        }
    }


    private fun takePhoto(photoName: String) {

        //create photo file to save photo in
        val photoFile = requireActivity().getPhotoFile(photoName)

        //output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        mImageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Timber.e("Photo capture failed: ${exc.message}")
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Timber.i("Photo capture succeeded: ${photoFile.path}")
                viewModel.navigateToWeatherInfo(photoFile.path)
            }
        })

    }


    private fun navigateToWeatherInfo(item: WeatherHistoryItem) {
        val action = CameraFragmentDirections.actionCameraFragmentToWeatherInfoFragment(item)
        findNavController().navigate(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionDenied = true
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            for (permission in grantResults) {
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    permissionDenied = false
                    break
                }
            }
        }


        if (!permissionDenied) {
            initCameraProvider()
        } else {
            activity?.finish()
        }
    }


}