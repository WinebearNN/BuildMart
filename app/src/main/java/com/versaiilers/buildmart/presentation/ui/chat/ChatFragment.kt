package com.versaiilers.buildmart.presentation.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.versaiilers.buildmart.databinding.FragmentChatBinding
import com.versaiilers.buildmart.presentation.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val TAG="ChatFragment"

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter


    //TODO решить с передачей юзера в параметрах

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        chatAdapter=ChatAdapter()


        binding.chatFragmentRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }


        observeViewModel()

        // Загружаем чаты при открытии фрагмента
        viewModel.loadChats("1")

        // Подключаем WebSocket для получения новых сообщений
        viewModel.connectWebSocket()


        return binding.root
    }



    private fun observeViewModel() {
        Log.d(TAG,"Зашел в observeViewModel")
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatAdapter.submitList(chats)
        }

        viewModel.newMessage.observe(viewLifecycleOwner) { newMessage ->
            Log.d(TAG,"Пришло новое сообщение $newMessage")
            Toast.makeText(requireContext(), "New message: ${newMessage.content}", Toast.LENGTH_SHORT).show()
        }

//        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.isVisible = isLoading
//        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.e(TAG,"$TAG error $errorMessage")
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.disconnectWebSocket()
    }

    // В вашем фрагменте
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.connectWebSocket()  // Запуск WebSocket соединения
    }


}