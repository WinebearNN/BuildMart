package com.versaiilers.buildmart.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.versaiilers.buildmart.R


class MainAuthFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_main_auth, container, false)
        view.findViewById<Button>(R.id.fragment_main_auth_button_signIn).setOnClickListener{Navigation.findNavController(view).navigate(
            R.id.action_mainAuthFragment_to_signInFragment
        )}
        view.findViewById<Button>(R.id.fragment_main_auth_button_register).setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_mainAuthFragment_to_registrationFragment)
            onDestroy()
        }
        return view
    }


}