package com.sushmoyr.ajkalnewyork

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.databinding.CategoryViewBinding
import com.sushmoyr.ajkalnewyork.models.Category

class DrawerMenuAdapter: RecyclerView.Adapter<DrawerMenuAdapter.MyViewHolder>() {

    private var cat_data:List<Category> = emptyList()
    class MyViewHolder(private val binding: CategoryViewBinding) : RecyclerView.ViewHolder
        (binding.root){
            fun bind(category: Category) {
                binding.categoryName.text = category.category_name
                for(i in 0..4){
                    val view = TextView(binding.root.context)
                    view.text = "Item $i"
                    binding.subCategoryRoot.addView(view)
                }
                binding.dropDownIcon.setOnClickListener {
                    cycleView()
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
        holder.bind(cat_data[position])
    }

    override fun getItemCount(): Int {
        return cat_data.size
    }

    fun setData(data: List<Category>){
        cat_data = data
        notifyDataSetChanged()
    }
}