package com.thienbinh.halioplayer.model

import android.graphics.Bitmap
import androidx.constraintlayout.solver.widgets.Helper
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Music(
  @SerializedName("id")
  var id: Int,
  @SerializedName("title")
  var title: String,
  @SerializedName("thumbnail")
  var thumbnail: String? = null,
  @SerializedName("singer")
  var singer: String,
  @SerializedName("href")
  var href: String = "",
  @SerializedName("duration")
  var duration: Int,
  @SerializedName("genre")
  var genre: MutableList<Genre> = mutableListOf(),
  @SerializedName("albums")
  var albums: MutableList<Album> = mutableListOf(),
  @SerializedName("count_play")
  var count_play: Int = 0,
  @SerializedName("localHref")
  var localHref: String? = null,
  @SerializedName("localThumbnail")
  var localThumbnail: String? = null,
  @SerializedName("hasLyric")
  var hasLyric: Boolean = false,
  @SerializedName("lyricHref")
  var lyricHref: String? = null,
  @SerializedName("isFromDevice")
  var isFromDevice: Boolean = false
) : Serializable {
  companion object {
    private var instance: MutableList<Music>? = null

    private val gson = Gson()

    fun getInstance(): MutableList<Music> {
      initializeList()

      return instance!!
    }

    fun checkInstanceIsNull() = instance == null

    fun getMusicById(id: Int): Music? {
      if (instance == null) getInstance()

      return instance!!.find { it.id == id }
    }

    fun initializeList() {
      if (instance == null) {
        instance = mutableListOf()

        instance!!.apply {
          /*List Made for you music*/
          add(
            Music(
              size,
              "Different World",
              "https://avatar-nct.nixcdn.com/playlist/2018/12/13/2/1/5/3/1544707823259_500.jpg",
              "Alan Walker",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599107938/mp3/DifferentWorld-AlanWalkerK391SofiaCarsonCORSAK-5815054_ti6gva.mp3",
              202,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(0)!!
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599408603/mp3/different_world.lrc"
            )
          )

          add(
            Music(
              size,
              "Lost Control",
              "https://i1.sndcdn.com/artworks-000493539870-kx7790-t500x500.jpg",
              "Alan Walker",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599539721/mp3/LostControl-AlanWalkerSorana-5815023_lzybap.mp3",
              222,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(0)!!
                )
              ),
              albums = Album.deepCloneAlbumList(
                mutableListOf(
                  Album.getAlbumById(0)
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599546050/mp3/lost_control.lrc"
            )
          )

          add(
            Music(
              size,
              "All Fall Down",
              "https://avatar-nct.nixcdn.com/song/2017/10/27/9/d/8/d/1509093543890_640.jpg",
              "Alan Walker",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599546259/mp3/AllFallsDown-AlanWalkerNoahCyrusDigitalFarmAnimalsJuliander-5817723_jj6d33.mp3",
              197,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(1)!!
                )
              ),
              albums = Album.deepCloneAlbumList(
                mutableListOf(
                  Album.getAlbumById(0)
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599546442/mp3/all_fall_down.lrc"
            )
          )


          /*List mood music*/

          add(
            Music(
              size,
              "Let me down slowly",
              "https://i.scdn.co/image/ab67616d00001e02459d675aa0b6f3b211357370",
              "Alec Benjamin",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599104385/mp3/let_me_down_slowly_l7moih.mp3",
              169,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(1)!!,
                  Genre.getGenreById(0)!!
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599535225/mp3/let_me_down_slowly.lrc"
            )
          )

          add(
            Music(
              size,
              "Memories",
              "https://i.scdn.co/image/ab67616d00001e02b8c0135a218de2d10a8435f5",
              "Maroon 5",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599106966/mp3/Memories-Maroon5-6091839_vwr1va.mp3",
              190,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(1)!!,
                  Genre.getGenreById(0)!!
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599408868/mp3/memories.lrc"
            )
          )

          add(
            Music(
              size,
              "Sing Me To Sleep",
              "https://avatar-nct.nixcdn.com/song/2017/10/20/a/9/1/8/1508495764582.jpg",
              "Aland Walker",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599108050/mp3/SingMetoSleep-AlanWalker-5815065_jmshmd.mp3",
              187,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(1)!!,
                  Genre.getGenreById(0)!!
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599535936/mp3/sing_me_to_sleep.lrc"
            )
          )
          /*List life sucks*/
          add(
            Music(
              size,
              "Believe",
              "https://i.scdn.co/image/ab67616d00001e0259099e2755405c06543f6ed0",
              "Imagine Dragon",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599104385/mp3/believer_kzu21c.mp3",
              204,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(2)!!
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599535573/mp3/beliver.lrc"
            )
          )

          add(
            Music(
              size,
              "Hate u hate u",
              "https://i.scdn.co/image/ab67616d00001e02f9a776c0d4ec78052c92882b",
              "Olivia O'Brien",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599104385/mp3/hate_u_hate_u_vj6ilg.mp3",
              178,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(2)!!,
                  Genre.getGenreById(0)!!
                )
              )
            )
          )

          /*Peaceful Piano*/

          add(
            Music(
              size,
              "Shape of You",
              "https://i.scdn.co/image/ab67616d0000b273ba5db46f4b838ef6027e6f96",
              "Music Lab Collective",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599107296/mp3/ShapeOfYou-MusicLabCollective-4964499_xiqrbi.mp3",
              178,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(3)!!,
                  Genre.getGenreById(0)!!
                )
              )
            )
          )

          add(
            Music(
              size,
              "I Took A Pill In Ibiza",
              "https://avatar-nct.nixcdn.com/singer/avatar/2017/05/10/2/7/6/e/1494386279547.jpg",
              "Music Lab Collective",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599107686/mp3/ITookAPillInIbiza-MusicLabCollective-4964524_t7rhlf.mp3",
              196,
              Genre.deepCloneGenreList(
                mutableListOf(
                  Genre.getGenreById(3)!!
                )
              )
            )
          )

          /*EDM Album*/

          add(
            Music(
              size,
              "Nonstop Edm",
              "https://avatar-nct.nixcdn.com/singer/avatar/2018/02/06/c/8/f/4/1517890221437.jpg",
              "DJ",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599546608/mp3/NonstopEDM-DJ-3160413_uedcge.mp3",
              196,
              albums = Album.deepCloneAlbumList(
                mutableListOf(
                  Album.getAlbumById(1)
                )
              )
            )
          )

          add(
            Music(
              size,
              "Thu Cuối (EDM)",
              "https://avatar-nct.nixcdn.com/singer/avatar/2018/02/06/c/8/f/4/1517890221437.jpg",
              "Hằng Bingbong",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599546734/mp3/ThuCuoiEDM-HangBingBoong-3907592_irvkkr.mp3",
              275,
              albums = Album.deepCloneAlbumList(
                mutableListOf(
                  Album.getAlbumById(1)
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599555278/mp3/thu_cuoi_edm.lrc"
            )
          )

          add(
            Music(
              size,
              "EDM through night",
              "https://image.shutterstock.com/image-vector/letter-va-logo-av-modern-260nw-1285803499.jpg",
              "V.A",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599547066/mp3/EDM-DangCapNhat_35xne_yxx60h.mp3",
              2757,
              albums = Album.deepCloneAlbumList(
                mutableListOf(
                  Album.getAlbumById(1)
                )
              )
            )
          )

          /*Acountic hit*/

          add(
            Music(
              size,
              "Beautiful in white",
              "https://images.genius.com/0f06b9613b5cc3e314625ccf9a143996.630x640x1.jpg",
              "Shayne Ward",
              "https://res.cloudinary.com/do1xjyyru/video/upload/v1599547405/mp3/BeautifulInWhile-Dangcapnhat_sexc_e2vstg.mp3",
              233,
              albums = Album.deepCloneAlbumList(
                mutableListOf(
                  Album.getAlbumById(2)
                )
              ),
              hasLyric = true,
              lyricHref = "https://res.cloudinary.com/do1xjyyru/raw/upload/v1599547678/mp3/beautify_in_white.lrc"
            )
          )

        }
      }
    }

    fun checkMusicListAreTheSame(
      listOne: MutableList<Music>,
      listSecond: MutableList<Music>
    ): Boolean {
      if (listOne.size != listSecond.size) return false

      var check = true

      listOne.forEachIndexed { index, music ->
        if (music.id != listSecond[index].id || music.count_play != listSecond[index].count_play || music.title != listSecond[index].title) {
          check = false
          return@forEachIndexed
        }
      }

      return check
    }

    fun deepCloneMusicList(musicList: MutableList<Music>): MutableList<Music> {
      return gson.fromJson(gson.toJson(musicList), Array<Music>::class.java).toMutableList()
    }
  }
}