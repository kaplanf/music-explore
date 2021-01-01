package com.kaplan.musicexplore.ui.track.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaplan.musicexplore.R
import com.kaplan.musicexplore.binding.BindableAdapter
import com.kaplan.musicexplore.databinding.TrackItemGridBinding
import com.kaplan.musicexplore.ui.track.data.Track
import javax.inject.Inject

class TracksAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<TracksAdapter.ViewHolder>(), BindableAdapter<List<Track>> {

    var trackItems = ArrayList<Track>()
    lateinit var viewModelAdapter: TracksViewModel

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trackItem = getItem(position)
        trackItem?.let {
            holder.apply {
                bind(createOnClickListener(trackItem.trackId, trackItem), trackItem)
                itemView.tag = trackItem
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TrackItemGridBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setViewModel(viewModel: TracksViewModel)
    {
        viewModelAdapter = viewModel
    }

    private fun createOnClickListener(id: Int, track: Track): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.trackLikeIcon -> {
                    if(track.isLiked) viewModelAdapter.deleteTrack(track) else viewModelAdapter.insertTrack(track)
                    track.isLiked = !track.isLiked
                }
            }
        }
    }

    class ViewHolder(private val binding: TrackItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Track) {
            binding.apply {
                clickListener = listener
                trackItem = item
                if (item.contentAdvisoryRating.isNullOrEmpty().not()) {
                    trackExplicitLabel.text = item.contentAdvisoryRating
                }
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int = trackItems.size

    override fun setData(data: List<Track>) {}

    override fun updateData(data: List<Track>) {
        val diffCallback = TrackListDiffCallback(trackItems, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.trackItems.clear()
        this.trackItems.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int): Track {
        return trackItems[position]
    }
}

private class TrackListDiffCallback(
    private val oldList: List<Track>,
    private val newList: List<Track>
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
