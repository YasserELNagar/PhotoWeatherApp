package com.example.robustatask.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.robustatask.R
import com.example.robustatask.databinding.FragmentShareBottomSheetBinding
import com.example.robustatask.model.SharedItem
import com.example.robustatask.shared.helper.shareImage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShareBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding :FragmentShareBottomSheetBinding
    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShareBottomSheetBinding.inflate(inflater)
        initViewModel()
        observeShareImage()
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        return binding.root
    }

    private fun observeShareImage() {
        viewModel.shareImage.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                shareImage(it)
            }
        })
    }

    private fun shareImage(sharedItem: SharedItem) {
        requireActivity().shareImage(sharedItem.imagePath,sharedItem.targetAppPackageName)
    }

    private fun initViewModel() {
        val args by navArgs<ShareBottomSheetFragmentArgs>()
        val viewModelFactory = ShareViewModelFactory(args.weatherItem)
        viewModel = ViewModelProvider(this,viewModelFactory)[ShareViewModel::class.java]
    }

}