package com.versaiilers.buildmart.presentation.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentConversationBinding
import com.versaiilers.buildmart.presentation.viewmodel.ConversationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConversationFragment : Fragment() {

    private val TAG = "ConversationFragment"
    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConversationViewModel by viewModels()



    private lateinit var conversationAdapter: ConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)

        // Инициализация адаптера для отображения сообщений
        conversationAdapter = ConversationAdapter(1)

        binding.conversationFragmentRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = conversationAdapter
        }

        // Подключение наблюдателей на данные ViewModel
        observeViewModel()

        // Получение ID чата из аргументов или другим способом
        val chatId = arguments?.getLong("chatGlobalId") ?: 0L
        viewModel.setChatId(chatId)

        // Обработка нажатия на кнопку "Назад"
        binding.conversationFragmentButtonGoBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

//        binding.conversationFragmentButtonSendMessage.setOnClickListener {
//            val contentMessage=binding.conversationFragmentEditTextTypeMessage.text
//            if(contentMessage!=null){
//
//            }
//        }





        // Обработка отправки сообщения
        binding.conversationFragmentButtonSendMessage.setOnClickListener {
            val messageContent = binding.conversationFragmentEditTextTypeMessage.text.toString()
            if (messageContent.isNotBlank()) {
                viewModel.sendMessage(messageContent)
                binding.conversationFragmentEditTextTypeMessage.text.clear()  // Очистка поля ввода после отправки
            }
        }

        return binding.root
    }

    private fun observeViewModel() {
        // Наблюдение за списком сообщений
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            conversationAdapter.submitList(messages)
            // Прокрутка вниз после обновления списка сообщений
            binding.conversationFragmentRecycleView.scrollToPosition(messages.size - 1)
        }

        // Наблюдение за новым сообщением
        viewModel.newMessage.observe(viewLifecycleOwner) { newMessage ->
            viewModel.messages.value?.let { conversationAdapter.submitList(it) }  // Обновление адаптера
            binding.conversationFragmentRecycleView.scrollToPosition(viewModel.messages.value?.size?.minus(1) ?: 0)
        }

//        // Наблюдение за состоянием загрузки
//        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
//            // Можно добавить прогресс-бар или индикатор загрузки при необходимости
//            binding.progressBar.isVisible = isLoading
//        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.e(TAG, "Error: $errorMessage")
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Скрываем BottomNavigationView
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        // Показываем BottomNavigationView снова при выходе
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
//        viewModel.disconnectWebSocket()


    }


}