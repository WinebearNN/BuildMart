package com.versaiilers.buildmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class SignInFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view=inflater.inflate(R.layout.fragment_sign_in, container, false)
        view.findViewById<Button>(R.id.fragment_sign_in_button_go_back).setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_mainAuthFragment)
        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SignInFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
