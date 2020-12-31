package com.kaplan.musicexplore.ui.album.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaplan.musicexplore.binding.BindableAdapter
import com.kaplan.musicexplore.databinding.AlbumItemBinding
import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.util.ui.getReadableDate
import javax.inject.Inject

class AlbumsAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<AlbumsAdapter.ViewHolder>(), BindableAdapter<List<Album>> {

    var albumItems = ArrayList<Album>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumItem = getItem(position)
        albumItem?.let {
            holder.apply {
                bind(createOnClickListener(albumItem.collectionId), albumItem)
                itemView.tag = albumItem
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AlbumItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private fun createOnClickListener(id: Int): View.OnClickListener {
        return View.OnClickListener {
            val direction = AlbumsFragmentDirections.actionToTracksFragment(id.toString())
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(private val binding: AlbumItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Album) {
            binding.apply {
                clickListener = listener
                albumItem = item
                //todo fix this
                if (item.contentAdvisoryRating.isNullOrEmpty().not()) {
                    albumExplicitLabel.text = item.contentAdvisoryRating
                }

                albumReleaseDate.text = getReadableDate(item.releaseDate)
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int = albumItems.size

    override fun setData(data: List<Album>) {}

    override fun updateData(data: List<Album>) {
        val diffCallback = AlbumListDiffCallback(albumItems, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.albumItems.clear()
        this.albumItems.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int): Album {
        return albumItems[position]
    }
}

private class AlbumListDiffCallback(
    private val oldList: List<Album>,
    private val newList: List<Album>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].collectionId == newList[newItemPosition].collectionId
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.collectionId == newItem.collectionId
    }
}
