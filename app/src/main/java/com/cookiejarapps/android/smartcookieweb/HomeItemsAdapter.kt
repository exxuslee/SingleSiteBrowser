package com.cookiejarapps.android.smartcookieweb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.android.smartcookieweb.databinding.ItemSiteBinding

class HomeItemsAdapter(
    private val items: List<HomeItem>,
    private val onItemClick: (HomeItem) -> Unit,
) : RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemSiteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeItem) {
            val context = binding.root.context
            binding.siteTitle.text = item.title
            binding.subtitle.text = item.description
            val fallback = when (item) {
                is HomeItem.Website -> R.drawable.ic_item_website
                is HomeItem.ExternalApp -> R.drawable.ic_item_app
            }
            binding.itemIcon.setImageDrawable(
                HomeItemIconLoader.load(context, item.icon)
                    ?: ContextCompat.getDrawable(context, fallback),
            )
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }
}
