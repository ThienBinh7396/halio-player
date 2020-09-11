package com.thienbinh.halioplayer.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EDisplayStyle
import com.thienbinh.halioplayer.customInterface.IMusicBlockEventListener
import com.thienbinh.halioplayer.databinding.MusicBlockLayoutBinding
import com.thienbinh.halioplayer.databinding.MusicInAlbumLayoutBinding
import com.thienbinh.halioplayer.databinding.MusicInPlaylistLayoutBinding
import com.thienbinh.halioplayer.databinding.MusicListLayoutBinding
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

class MusicListAdapter(
  private var mMusicList: MutableList<Music> = mutableListOf(),
  private val displayStyle: EDisplayStyle = EDisplayStyle.BLOCK_STYLE,
  private val isShowWidgetButton: Boolean = false,
  private val eventListener: IMusicBlockEventListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var mRecyclerView: RecyclerView? = null

  class MusicListStyleViewHolder(private val binding: MusicListLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindingData(data: Music) {
      binding.music = data

      if (binding.musicStoreViewModel == null) {
        binding.musicStoreViewModel = MusicStoreViewModel()
      }
    }
  }

  class MusicBlockStyleViewHolder(
    private val binding: MusicBlockLayoutBinding,
    isShowWidgetButton: Boolean,
    mEventListener: IMusicBlockEventListener? = null
  ) :
    RecyclerView.ViewHolder(binding.root) {

    init {
      binding.isShowWidgetButton = isShowWidgetButton

      if (mEventListener != null) {
        binding.apply {
          eventListener = mEventListener
        }
      }
    }

    fun bindingData(data: Music) {
      binding.apply {
        music = data

        if (musicStoreViewModel == null) {
          musicStoreViewModel = MusicStoreViewModel()
        }
      }

    }
  }

  class MusicInAlbumStyleViewHolder(private val binding: MusicInAlbumLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindingData(data: Music, position: Int) {
      binding.music = data

      binding.position = position

      if (binding.musicStoreViewModel == null) {
        binding.musicStoreViewModel = MusicStoreViewModel()
      }
    }
  }

  class MusicInPlaylistStyleViewHolder(private val binding: MusicInPlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
      binding.isActive = false
    }

    fun bindingData(data: Music, position: Int) {
      binding.music = data

      if (binding.musicStoreViewModel == null) {
        binding.musicStoreViewModel = MusicStoreViewModel()
      }
    }
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)

    mRecyclerView = recyclerView

    Log.d("Binh", "Tag: ${recyclerView.tag}")
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    when (displayStyle) {
      EDisplayStyle.LIST_STYLE -> return MusicListStyleViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.music_list_layout,
          null,
          false
        )
      )
      EDisplayStyle.IN_ALBUM -> return MusicInAlbumStyleViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.music_in_album_layout,
          null,
          false
        )
      )

      EDisplayStyle.IN_PLAYLIST -> return MusicInPlaylistStyleViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.music_in_playlist_layout,
          null,
          false
        )
      )

      else ->
        return MusicBlockStyleViewHolder(
          DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.music_block_layout,
            null,
            false
          ),
          isShowWidgetButton,
          eventListener
        )
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (displayStyle) {
      EDisplayStyle.LIST_STYLE -> {
        (holder as MusicListStyleViewHolder).bindingData(data = mMusicList[position])
      }
      EDisplayStyle.IN_ALBUM -> {
        (holder as MusicInAlbumStyleViewHolder).bindingData(
          data = mMusicList[position],
          position + 1
        )
      }
      EDisplayStyle.IN_PLAYLIST -> {
        (holder as MusicInPlaylistStyleViewHolder).bindingData(
          data = mMusicList[position],
          position + 1
        )
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
      oldList[oldItemPosition].count_play == newList[newItemPosition].count_play

  }
}