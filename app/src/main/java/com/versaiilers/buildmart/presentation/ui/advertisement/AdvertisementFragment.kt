package com.versaiilers.buildmart.presentation.ui.advertisement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentAdvertisementBinding


class AdvertisementFragment : Fragment() {

    private var _binding: FragmentAdvertisementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding=FragmentAdvertisementBinding.inflate(inflater,container,false)

        // Inflate the layout for this fragment


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_advertisement_to_createAd)
    }

}