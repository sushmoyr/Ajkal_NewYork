package com.sushmoyr.ajkalnewyork.fragments.user.userad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.PaymentHistoryLayoutBinding
import com.sushmoyr.ajkalnewyork.models.utility.transactionhistory.TransactionHistory

class PaymentHistoryAdapter: RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder>() {

    private var data = TransactionHistory()
    inner class MyViewHolder(val binding: PaymentHistoryLayoutBinding) : RecyclerView.ViewHolder
    (binding.root) {
        fun bind(position: Int) {
            val item = data.advertisements[position]
            binding.serial.text = (position+1).toString()
            binding.titleHistory.text = item.adTitle
            binding.payingAmountHistory.text = item.amount
            binding.transactionDateHistory.text = item.createdAt
            binding.transactionIdHistory.text = item.balanceTransaction
            val statusV = binding.statusHistory
            val context = binding.root.context

            if(item.paymentStatus=="1"){
                statusV.text = context.resources.getString(R.string.payment_status_paid)
                statusV.setTextColor(ContextCompat.getColor(context, R.color.green))
            }else{
                statusV.text = context.resources.getString(R.string.payment_status_unpaid)
                statusV.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(PaymentHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.advertisements.size
    }

    fun setData(value: TransactionHistory){
        data = value
        notifyDataSetChanged()
    }

}