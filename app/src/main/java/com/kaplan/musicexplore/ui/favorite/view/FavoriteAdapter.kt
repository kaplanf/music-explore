package com.kaplan.musicexplore.ui.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaplan.musicexplore.binding.BindableAdapter
import com.kaplan.musicexplore.databinding.FavoriteItemBinding
import com.kaplan.musicexplore.ui.favorite.data.Favorite
import com.kaplan.musicexplore.ui.track.view.TracksViewModel
import javax.inject.Inject

class FavoriteAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(), BindableAdapter<List<Favorite>> {

    var favoriteItems = ArrayList<Favorite>()
    lateinit var viewModelAdapter: FavoriteViewModel
    var isPlaying: Boolean = false

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteItem = getItem(position)
        favoriteItem?.let {
            holder.apply {
                bind(createOnClickListener(favoriteItem.previewUrl), favoriteItem)
                itemView.tag = favoriteItem
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavoriteItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setViewModel(viewModel: FavoriteViewModel)
    {
        viewModelAdapter = viewModel
    }

    private fun createOnClickListener(previewUrl: String): View.OnClickListener {
        return View.OnClickListener {
            isPlaying = !isPlaying
            viewModelAdapter.sendSongPlayStatus(previewUrl, isPlaying)
        }
    }

    class ViewHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listener: View.OnClickListener, item: Favorite
        ) {
            binding.apply {
                clickListener = listener
                favoriteItem = item
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int = favoriteItems.size

    override fun setData(data: List<Favorite>) {}

    override fun updateData(data: List<Favorite>) {
        val diffCallback = FavoriteListDiffCallback(favoriteItems, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.favoriteItems.clear()
        this.favoriteItems.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int): Favorite {
        return favoriteItems[position]
    }
}

private class FavoriteListDiffCallback(
    private val oldList: List<Favorite>,
    private val newList: List<Favorite>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.trackId == newItem.trackId
    }
}
