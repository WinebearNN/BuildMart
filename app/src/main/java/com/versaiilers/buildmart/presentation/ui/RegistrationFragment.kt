package com.versaiilers.buildmart.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentRegistrationBinding
import com.versaiilers.buildmart.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    // ViewModel инициализируется лениво
    private val viewModel: RegistrationViewModel by viewModels() // Hilt автоматически инжектирует зависимости


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация binding с помощью метода inflate
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        setupUI()
        setupObservers()

        return binding.root
    }

    private fun setupUI() {
        // Обработчик кнопки "Назад"
        binding.fragmentRegistrationButtonGoBack.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_registrationFragment_to_mainAuthFragment)
        }

        // Обработчик кнопки "Регистрация"
        binding.fragmentRegistrationButtonRegister.setOnClickListener {
            // Очистка предыдущих ошибок
            clearErrors()

            val email = binding.fragmentRegistrationTextEditEmail.text.toString()
            val password = binding.fragmentRegistrationTextEditPassword.text.toString()
            val phoneNumber = binding.fragmentRegistrationTextEditPhoneNumber.text.toString()

            // Запуск регистрации пользователя
            viewModel.registerUser(email, password, phoneNumber)
        }
    }

    private fun setupObservers() {
        // Наблюдаем за ошибками валидации
        viewModel.globalErrors.observe(viewLifecycleOwner) { errors ->
            errors?.let {
                for (error in it) {
                    when (error.code) {
                        100 -> {
                            binding.fragmentRegistrationTextEditEmail.error = error.description
                            binding.fragmentRegistrationTextEditEmail.setBackgroundResource(R.drawable.edit_text_background_error)
                        }

                        101 -> {
                            binding.fragmentRegistrationTextEditPassword.error = error.description
                            binding.fragmentRegistrationTextEditPassword.setBackgroundResource(R.drawable.edit_text_background_error)
                        }

                        102 -> {
                            binding.fragmentRegistrationTextEditPhoneNumber.error =
                                error.description
                            binding.fragmentRegistrationTextEditPhoneNumber.setBackgroundResource(R.drawable.edit_text_background_error)
                        }
                    }
                }
            }
        }

        // Наблюдаем за результатом регистрации
        viewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT)
                    .show()
                Log.i("Registration Fragment", "Registration successful")
            }.onFailure { exception ->
                Log.e("Registration Fragment", exception.message.toString());
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun clearErrors() {
        binding.fragmentRegistrationTextEditPassword.error = null
        binding.fragmentRegistrationTextEditEmail.error = null
        binding.fragmentRegistrationTextEditPhoneNumber.error = null
        binding.fragmentRegistrationTextEditPassword.setBackgroundResource(R.drawable.edit_text_background)
        binding.fragmentRegistrationTextEditEmail.setBackgroundResource(R.drawable.edit_text_background)
        binding.fragmentRegistrationTextEditPhoneNumber.setBackgroundResource(R.drawable.edit_text_background)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Освобождаем binding при уничтожении view
    }
}


//        // Наблюдение за результатами регистрации
//        viewModel!!.registrationResult.observe(viewLifecycleOwner) { result ->
//            result.onSuccess {
//                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
//            }.onFailure { error ->
//                Log.d("Registration Fragment Errors validation", error.toString())
//                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//                when (error.message) {
//                    ErrorCode.CODE_101.description -> passwordField!!.setBackgroundResource(R.drawable.edit_text_background_error)
//                    ErrorCode.CODE_100.description -> emailField!!.setBackgroundResource(R.drawable.edit_text_background_error)
//                    ErrorCode.CODE_102.description -> phoneNumberField!!.setBackgroundResource(R.drawable.edit_text_background_error)
//                }
//            }
//        }