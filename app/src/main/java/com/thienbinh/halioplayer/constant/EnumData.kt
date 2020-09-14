package com.thienbinh.halioplayer.constant

enum class EActionMusicTypeSource {
  FILE_FROM_ASSETS,
  FILE_FROM_URI,
  URI
}

enum class EDisplayStyle {
  BLOCK_STYLE,
  LIST_STYLE,
  IN_ALBUM,
  IN_PLAYLIST,
  LIST_STYLE_IN_PLAYLIST
}

enum class EMusicPlayRepeat {
  NO_REPEAT,
  REPEAT_ONE,
  REPEAT_ALL
}

enum class EMusicListTypeSort{
  SORT_BY_TITLE,
  SORT_BY_COUNT
}

enum class EFragmentName(val titleFragment: String) {
  LYRIC_FRAGMENT("Lyric"),
  HOME_FRAGMENT("Home"),
  RECENT_FRAGMENT("Recently played"),
  ALBUM_FRAGMENT("Album"),
  GENRE_FRAGMENT("Genre detail"),
  PLAYLIST_FRAGMENT("Playlist")
}

enum class EMusicRecycleView{
  RECENTLY_PLAYED_TAG
}

enum class ETypeWidgetButton{
  REMOVE_FROM_RECENTLY_PLAYED_LIST,
  REMOVE_FROM_PLAYLIST
}