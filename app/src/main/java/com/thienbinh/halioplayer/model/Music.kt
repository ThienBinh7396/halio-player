package com.thienbinh.halioplayer.model

class Music(var id: Int, var title: String, var thumbnail: String, var singer: String, var href: String, var duration: Int, var genre: MutableList<Genre>) {
  companion object{
    private var instance: MutableList<Music>? = null

    fun getInstance(): MutableList<Music> {
      if(instance == null){
        instance = mutableListOf()
      }

      return instance!!
    }

    private fun initializeList(){
      if (instance != null){
        /*List mood music*/

        instance!!.add(Music(
          1,
          "Let me down slowly",
          "https://i.scdn.co/image/ab67616d00001e02459d675aa0b6f3b211357370",
          "Alec Benjamin",
          "let_me_down_slowly",
          169,
          mutableListOf(
            Genre.getGenreById(1)!!
          )
        ))
      }
    }
  }
}