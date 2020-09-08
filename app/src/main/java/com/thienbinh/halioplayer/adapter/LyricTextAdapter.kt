package com.thienbinh.halioplayer.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.databinding.LyricTextLayoutBinding
import com.thienbinh.halioplayer.model.Lyric

class LyricTextAdapter : RecyclerView.Adapter<LyricTextAdapter.LyricTextViewHolder>() {
  private var mLyrics: MutableList<Lyric> = mutableListOf()

  class LyricTextViewHolder(var binding: LyricTextLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: Lyric) {
      binding.lyric = data

      binding.executePendingBindings()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricTextViewHolder =
    LyricTextViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.lyric_text_layout,
        null,
        false
      )
    )

  override fun onBindViewHolder(holder: LyricTextViewHolder, position: Int) {
    Log.d("Binh", "POSITION: $position, ${mLyrics[position].isActive}")

    holder.bindData(mLyrics[position])
  }

  override fun getItemCount(): Int = mLyrics.size

  fun updateList(newList: MutableList<Lyric>) {
    val diffCallback = LyricTextDiffCallback(mLyrics, newList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    mLyrics = newList

    diffResult.dispatchUpdatesTo(this)
  }

  class LyricTextDiffCallback(var oldList: MutableList<Lyric>, var newList: MutableList<Lyric>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

      return oldList[oldItemPosition].time == newList[newItemPosition].time
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

      return oldList[oldItemPosition].isActive == newList[newItemPosition].isActive
    }

  }
}