package com.versaiilers.buildmart.presentation.ui.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentGeneralBinding
import com.versaiilers.buildmart.presentation.viewmodel.GeneralViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GeneralViewModel by viewModels()
    private lateinit var generalFragmentAdapter: GeneralFragmentAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.loadAdvertisements(1)

//        Navigation.findNavController(binding.root)
//            .navigate(R.id.action_general_to_advertisement)
    }

    private fun setupRecyclerView() {
        generalFragmentAdapter = GeneralFragmentAdapter()
        binding.fragmentGeneralRecycleView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            adapter = generalFragmentAdapter
        }
    }

    private fun observeViewModel(){
        viewModel.advertisements.observe(viewLifecycleOwner){advertisements ->
            generalFragmentAdapter.submitList(advertisements)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}