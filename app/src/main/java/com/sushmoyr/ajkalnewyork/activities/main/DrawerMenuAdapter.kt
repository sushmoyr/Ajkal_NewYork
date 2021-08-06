package com.sushmoyr.ajkalnewyork.activities.main

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.CategoryViewBinding
import com.sushmoyr.ajkalnewyork.models.core.Category

class DrawerMenuAdapter: RecyclerView.Adapter<DrawerMenuAdapter.MyViewHolder>() {

    var itemClickListener: ((data: String, type: Int) -> Unit)? = null

    private var catData:List<Category> = emptyList()
    class MyViewHolder(private val binding: CategoryViewBinding) : RecyclerView.ViewHolder
        (binding.root){

        var itemClickListener: ((data: String, type: Int) -> Unit)? = null

            fun bind(category: Category) {
                binding.categoryName.text = category.categoryName
                for(i in 0..4){
                    val view = TextView(binding.root.context)
                    view.text = "Item $i"
                    view.setOnClickListener {
                        Log.d("subCat", view.text.toString())
                    }
                    binding.subCategoryRoot.addView(view)
                }
                binding.dropDownIcon.setOnClickListener {
                    cycleView()
                }

                binding.categoryName.setOnClickListener {
                    itemClickListener?.invoke(binding.categoryName.text.toString(), 1)
                }
            }

        private fun cycleView() {
            val expandable = binding.subCategoryRoot
            val icon = binding.dropDownIcon

            if(expandable.visibility == View.GONE){
                icon.setImageResource(R.drawable.ic_up)
                TransitionManager.beginDelayedTransition(binding.categoryViewLayoutRoot,
                    AutoTransition())
                expandable.visibility = View.VISIBLE
            }
            else{
                icon.setImageResource(R.drawable.ic_down)
                expandable.visibility = View.GONE
                TransitionManager.beginDelayedTransition(binding.categoryViewLayoutRoot,
                    AutoTransition())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(CategoryViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        holder.bind(catData[position])
    }

    override fun getItemCount(): Int {
        return catData.size
    }

    fun setData(data: List<Category>){
        catData = data
        notifyDataSetChanged()
    }
}