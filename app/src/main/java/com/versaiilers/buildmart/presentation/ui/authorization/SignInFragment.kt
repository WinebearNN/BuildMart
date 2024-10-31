package com.versaiilers.buildmart.presentation.ui.authorization

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
import com.versaiilers.buildmart.databinding.FragmentSignInBinding
import com.versaiilers.buildmart.presentation.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {


    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    // ViewModel инициализируется лениво
    private val viewModel: SignInViewModel by  viewModels() // Hilt автоматически инжектирует зависимости

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)


        setupUI()

        setupObservers()






        return binding.root
    }

    private fun setupUI() {
        // Обработчик кнопки "Назад"
        binding.fragmentSignInButtonGoBack.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_signInFragment_to_mainAuthFragment)
        }

        // Обработчик кнопки "Регистрация"
        binding.fragmentSignInButtonConfirm.setOnClickListener {
            // Очистка предыдущих ошибок
            clearErrors()

            val email = binding.fragmentSignInTextEditEmail.text.toString()
            val password = binding.fragmentSignInTextEditPassword.text.toString()

            // Запуск регистрации пользователя
            viewModel.signInUser(email, password)
        }
    }


    private fun setupObservers() {
        // Наблюдаем за ошибками валидации
        viewModel.globalErrors.observe(viewLifecycleOwner) { errors ->


            // Отображение ошибок
            for (error in errors) {
                when (error.code) {

                    100 -> {
                        binding.fragmentSignInTextEditEmail.error = error.description
                        binding.fragmentSignInTextEditEmail.setBackgroundResource(R.drawable.edit_text_background_error)
                    }

                    101 -> {
                        binding.fragmentSignInTextEditPassword.error = error.description
                        binding.fragmentSignInTextEditPassword.setBackgroundResource(R.drawable.edit_text_background_error)
                    }


                }
            }
        }

        // Наблюдаем за результатом регистрации
        viewModel.signInResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                // Успешная регистрация
                Log.i("SignIn Fragment", "Sign in Successful")
                Toast.makeText(requireContext(), "Sign in Successful", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(binding.root).navigate(
                    R.id.action_signInFragment_to_profileFragment
                )
            }.onFailure { exception ->
                // Ошибка регистрации (не связанная с валидацией)
                Log.e("SignIn Fragment", exception.message.toString())
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun clearErrors() {
        binding.fragmentSignInTextEditEmail.error = null
        binding.fragmentSignInTextEditEmail.error = null
        binding.fragmentSignInTextEditEmail.setBackgroundResource(R.drawable.edit_text_background)
        binding.fragmentSignInTextEditPassword.setBackgroundResource(R.drawable.edit_text_background)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Очищаем binding, чтобы избежать утечек памяти
        _binding = null
    }
}
