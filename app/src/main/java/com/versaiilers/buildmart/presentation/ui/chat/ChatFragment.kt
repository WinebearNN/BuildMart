package com.versaiilers.buildmart.presentation.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.versaiilers.buildmart.R
import com.versaiilers.buildmart.databinding.FragmentChatBinding
import com.versaiilers.buildmart.presentation.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.refreshChats("1")  // Загружаем чаты с сервера при отображении
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter { chatGlobalId ->
            val bundle = Bundle().apply {
                putLong("chatGlobalId", chatGlobalId)
            }
            findNavController().navigate(R.id.action_chat_to_conversation,bundle)
        }
        binding.chatFragmentRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatAdapter.submitList(chats)
        }

        viewModel.newMessage.observe(viewLifecycleOwner) { newMessage ->
            Toast.makeText(requireContext(), "New message: ${newMessage.content}", Toast.LENGTH_SHORT).show()
        }
        //TODO проверить

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}