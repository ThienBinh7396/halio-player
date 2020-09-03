package com.thienbinh.halioplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.databinding.GenreBlockLayoutBinding
import com.thienbinh.halioplayer.model.Genre

class GenreListAdapter : RecyclerView.Adapter<GenreListAdapter.GenreListViewHolder>() {
  private val genres = Genre.getInstance().filter { it.id > 0 }

  class GenreListViewHolder(val binding: GenreBlockLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: Genre) {
      binding.genre = data
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreListViewHolder =
    GenreListViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.genre_block_layout,
        null,
        false
      )
    )

  override fun onBindViewHolder(holder: GenreListViewHolder, position: Int) {
    holder.bindData(genres[position])
  }

  override fun getItemCount(): Int = genres.size
}