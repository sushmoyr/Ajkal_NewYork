package com.sushmoyr.ajkalnewyork.fragments.user.userad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentPaymentHistoryBinding
import com.sushmoyr.ajkalnewyork.utils.getUserState

class PaymentHistoryFragment : Fragment() {

    private var _binding: FragmentPaymentHistoryBinding?= null
    private val binding get() = _binding!!
    private val viewModel: MainUserViewModel by viewModels()
    private val historyAdapter: PaymentHistoryAdapter by lazy{
        PaymentHistoryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)

        val user = getUserState(requireActivity())
        user.user?.let { viewModel.getTransactionHistory(it.id.toString()) }

        binding.paymentHistoryRv.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.transactionHistory.observe(viewLifecycleOwner, {
            historyAdapter.setData(it)
        })

        return binding.root
    }

}