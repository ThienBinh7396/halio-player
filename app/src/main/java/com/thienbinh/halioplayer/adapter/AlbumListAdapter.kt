package com.thienbinh.halioplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.databinding.AlbumBlockLayoutBinding
import com.thienbinh.halioplayer.databinding.GenreBlockLayoutBinding
import com.thienbinh.halioplayer.model.Album

class AlbumListAdapter : RecyclerView.Adapter<AlbumListAdapter.AlbumListViewHolder>() {
  private var mAlbums = Album.getInstance()

  class AlbumListViewHolder(var binding: AlbumBlockLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: Album) {
      binding.album = data
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListViewHolder =
    AlbumListViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from((parent.context)),
        R.layout.album_block_layout,
        null,
        false
      )
    )

  override fun onBindViewHolder(holder: AlbumListViewHolder, position: Int) {
    holder.bindData(mAlbums[position])
  }

  override fun getItemCount(): Int = mAlbums.size

  fun updateList(newList: MutableList<Album>){
    val diffCallback = AlbumListCallback(mAlbums, newList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    mAlbums = newList

    diffResult.dispatchUpdatesTo(this)
  }

  class AlbumListCallback(var oldList: MutableList<Album>, var newList: MutableList<Album>): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].title == newList[newItemPosition].title
  }
}