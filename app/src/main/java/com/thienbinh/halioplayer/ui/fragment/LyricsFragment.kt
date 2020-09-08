package com.thienbinh.halioplayer.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EFragmentName
import com.thienbinh.halioplayer.databinding.FragmentLyricsBinding
import com.thienbinh.halioplayer.databinding.LyricTextLayoutBinding
import com.thienbinh.halioplayer.model.Lyric
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.state.MusicState
import com.thienbinh.halioplayer.utils.CenterSmpothScroller
import com.thienbinh.halioplayer.utils.Helper
import com.thienbinh.halioplayer.viewModel.FragmentLyricsViewModel
import kotlinx.android.synthetic.main.fragment_lyrics.*
import org.rekotlin.StoreSubscriber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Error
import java.net.URL

class LyricsFragment : Fragment(), StoreSubscriber<MusicState> {
  private var urlString =
    "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599408868/mp3/memories.lrc"

  private val lyrics = mutableListOf<Lyric>()

  private lateinit var mFragmentLyricsBinding: FragmentLyricsBinding

  private var mMusic: Music? = null

  private var mCurrentPosition = 0

  private val handlerThread = HandlerThread("URLConnection")

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    handlerThread.start()

    mFragmentLyricsBinding = DataBindingUtil.inflate<FragmentLyricsBinding>(
      inflater,
      R.layout.fragment_lyrics,
      container,
      false
    ).apply {
      mFragmentLyricsViewModel = FragmentLyricsViewModel()

      goBack.setOnClickListener {
        MainActivity.navigate(R.id.action_lyricsFragment_to_homeFragment)
      }
    }

    return mFragmentLyricsBinding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Log.d("Binh", "Lyric fragment")
    MainActivity.mFragmentName = EFragmentName.LYRIC_FRAGMENT
  }

  private fun handleUpdateMusic() {
    if (mMusic != null && mMusic!!.hasLyric) {
      Handler(handlerThread.looper).postDelayed({
        try {
          Log.d("Binh", "Url : ${mMusic!!.lyricHref}")

          val urlConnection = URL(mMusic!!.lyricHref).openConnection()
          val inputStream = urlConnection.getInputStream()

          val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

          var line: String?

          while (true) {
            line = reader.readLine()

            if (line == null) break

            val lyric = Helper.convertLineFromLrcFileToLyric(line)

            if (lyric.content.trim() != "") {
              lyric.position = lyrics.size
              lyrics.add(lyric)
            }
          }

          mFragmentLyricsBinding.mFragmentLyricsViewModel?.updateLyrics(lyrics)


        } catch (err: Error) {
          Toast.makeText(context, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
        }
      }, 100)
    }
  }

  private var mIndexActive = -1

  private fun handleUpdateCurrentPosition() {
    if (mCurrentPosition != 0 && lyrics.size != 0) {
      var checkFirst = false

      var indexActive = -1

      lyrics.forEachIndexed { index, lyric ->
        if (!checkFirst && index < lyrics.size - 1 && lyric.time < mCurrentPosition && lyrics[index + 1].time > mCurrentPosition) {
          checkFirst = true

          lyric.isActive = true

          indexActive = index
        } else {
          lyric.isActive = false
        }
      }

      if (mIndexActive != indexActive) {
        val oldIndexActive = mIndexActive

        mIndexActive = indexActive

        mFragmentLyricsBinding.mFragmentLyricsViewModel?.updateLyrics(lyrics)

        mFragmentLyricsBinding.rcvLyrics.smoothScrollToPosition(indexActive)
      }
    }
  }

  override fun newState(state: MusicState) {
    state.apply {
      if (mCurrentPosition != currentPosition) {
        mCurrentPosition = currentPosition

        handleUpdateCurrentPosition()
      }

      if (currentMusic != null && (mMusic == null || mMusic?.id != currentMusic!!.id)) {
        mMusic = currentMusic

        handleUpdateMusic()
      }
    }
  }

  override fun onStart() {
    super.onStart()

    store.subscribe(this) {
      it.select {
        it.musicState
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    store.unsubscribe(this)
  }
}