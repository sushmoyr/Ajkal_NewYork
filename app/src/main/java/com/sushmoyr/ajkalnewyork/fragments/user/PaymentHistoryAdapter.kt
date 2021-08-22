package com.sushmoyr.ajkalnewyork.fragments.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.databinding.PaymentHistoryLayoutBinding
import com.sushmoyr.ajkalnewyork.models.utility.transactionhistory.TransactionHistory

class PaymentHistoryAdapter: RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder>() {

    private val data : MutableList<TransactionHistory> = mutableListOf()
    class MyViewHolder(val binding: PaymentHistoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transactionHistory: TransactionHistory, position: Int) {
            //TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(PaymentHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}