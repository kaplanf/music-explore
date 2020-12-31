package com.kaplan.musicexplore.ui.artist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaplan.musicexplore.binding.BindableAdapter
import com.kaplan.musicexplore.databinding.ArtistItemBinding
import com.kaplan.musicexplore.ui.artist.data.Artist
import javax.inject.Inject

class ArtistAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<ArtistAdapter.ViewHolder>(), BindableAdapter<List<Artist>> {

    var artistItems = ArrayList<Artist>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artistItem = getItem(position)
        artistItem?.let {
            holder.apply {
                bind(createOnClickListener(artistItem.artistId), artistItem)
                itemView.tag = artistItem
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ArtistItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private fun createOnClickListener(id: Int): View.OnClickListener {
        return View.OnClickListener {
            val direction = ArtistsFragmentDirections.actionToAlbumsFragment(id.toString())
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(private val binding: ArtistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listener: View.OnClickListener, item: Artist
        ) {
            binding.apply {
                clickListener = listener
                artistItem = item
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int = artistItems.size

    override fun setData(data: List<Artist>) {}

    override fun updateData(data: List<Artist>) {
        val diffCallback = ArtistListDiffCallback(artistItems, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.artistItems.clear()
        this.artistItems.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int): Artist {
        return artistItems[position]
    }
}

private class ArtistListDiffCallback(
    private val oldList: List<Artist>,
    private val newList: List<Artist>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].artistId == newList[newItemPosition].artistId
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.artistId == newItem.artistId
    }
}
