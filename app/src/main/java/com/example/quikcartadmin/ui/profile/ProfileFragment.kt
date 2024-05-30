package com.example.quikcartadmin.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentProfileBinding
import com.example.quikcartadmin.helpers.AuthHelper
import com.example.quikcartadmin.ui.auth.view.AuthenticationActivity


class ProfileFragment : Fragment() {

    private lateinit var profileBinding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileBinding.logout.setOnClickListener {

            AuthHelper.setUserSignedIn(requireContext(), false)

            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}