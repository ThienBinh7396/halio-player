package com.thienbinh.halioplayer.constant

enum class EActionMusicTypeSource {
  FILE_FROM_ASSETS,
  FILE_FROM_URI,
  URI
}

enum class EDisplayStyle {
  BLOCK_STYLE,
  LIST_STYLE,
  IN_ALBUM
}

enum class EMusicPlayRepeat {
  NO_REPEAT,
  REPEAT_ONE,
  REPEAT_ALL
}

enum class EFragmentName(val titleFragment: String) {
  LYRIC_FRAGMENT("Lyric"),
  HOME_FRAGMENT("Home"),
  RECENT_FRAGMENT("Recently played")
}