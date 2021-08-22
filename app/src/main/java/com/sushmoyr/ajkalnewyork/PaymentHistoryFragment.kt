package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentPaymentHistoryBinding
import com.sushmoyr.ajkalnewyork.utils.getUserState

class PaymentHistoryFragment : Fragment() {

    private var _binding: FragmentPaymentHistoryBinding?= null
    private val binding get() = _binding!!
    private val viewModel: MainUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = getUserState(requireActivity())
        user.user?.let { viewModel.getTransactionHistory(it.id.toString()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)



        return binding.root
    }

}