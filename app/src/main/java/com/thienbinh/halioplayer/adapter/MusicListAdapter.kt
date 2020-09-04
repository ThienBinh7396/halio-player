package com.thienbinh.halioplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.databinding.MusicBlockLayoutBinding
import com.thienbinh.halioplayer.databinding.MusicListLayoutBinding
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

enum class EDisplayStyle {
  BLOCK_STYLE,
  LIST_STYLE
}

class MusicListAdapter(
  private var mMusicList: MutableList<Music> = mutableListOf(),
  private val displayStyle: EDisplayStyle = EDisplayStyle.BLOCK_STYLE
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  class MusicListStyleViewHolder(private val binding: MusicListLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindingData(data: Music) {
      binding.music = data

    }
  }

  class MusicBlockStyleViewHolder(private val binding: MusicBlockLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindingData(data: Music) {
      binding.music = data

      if(binding.musicStoreViewModel == null){
        binding.musicStoreViewModel = MusicStoreViewModel()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    when (displayStyle) {
      EDisplayStyle.LIST_STYLE -> {
        return MusicListStyleViewHolder(
          DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.music_list_layout,
            null,
            false
          )
        )
      }
      else ->
        return MusicBlockStyleViewHolder(
          DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.music_block_layout,
            null,
            false
          )
        )
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (displayStyle) {
      EDisplayStyle.LIST_STYLE -> {
        (holder as MusicListStyleViewHolder).bindingData(data = mMusicList[position])
      }
      else ->
        (holder as MusicBlockStyleViewHolder).bindingData(data = mMusicList[position])
    }
  }

  fun getItemAt(position: Int) = if (position < mMusicList.size) mMusicList[position] else null

  override fun getItemCount(): Int = mMusicList.size

  fun updateList(newList: MutableList<Music>) {
    val diffCallback = MusicListDiffCallback(mMusicList, newList)

    val diffResult = DiffUtil.calculateDiff(diffCallback)

    mMusicList.clear()
    mMusicList.addAll(newList)

    diffResult.dispatchUpdatesTo(this)
  }

  class MusicListDiffCallback(
    private val oldList: MutableList<Music>,
    private val newList: MutableList<Music>
  ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].title == newList[newItemPosition].title

  }
}