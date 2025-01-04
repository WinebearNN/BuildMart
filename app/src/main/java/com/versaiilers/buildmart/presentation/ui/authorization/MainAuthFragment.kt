package com.versaiilers.buildmart.presentation.ui.authorization

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentMainAuthBinding
import com.versaiilers.buildmart.databinding.FragmentRegistrationBinding
import com.versaiilers.buildmart.databinding.FragmentSignInBinding
import com.versaiilers.buildmart.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels() // Hilt автоматически инжектирует зависимости

    private var _binding: FragmentMainAuthBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainAuthBinding.inflate(inflater, container, false)


        viewModel.authUser()
        setupObservers(binding.root)

        binding.fragmentMainAuthButtonSignIn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(
                R.id.action_mainAuthFragment_to_signInFragment
            )
        }
        binding.fragmentMainAuthButtonRegister.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_mainAuthFragment_to_registrationFragment)
        }
        return binding.root
    }

    private fun setupObservers(view:View) {
        // Наблюдаем за результатом авторизации
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
//                Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show()
                Log.i("MainAuthFragment", "Authentication successful: ${user.displayName}")
                Navigation.findNavController(view)
                    .navigate(R.id.action_mainAuthFragment_to_profileFragment)

            }.onFailure { exception ->
                Log.e("MainAuthFragment", exception.message.toString())
//                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()


            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}