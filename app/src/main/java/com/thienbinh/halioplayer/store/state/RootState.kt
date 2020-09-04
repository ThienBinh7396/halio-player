package com.thienbinh.halioplayer.store.state

import org.rekotlin.StateType

class RootState(
  val musicState: MusicState,
  val genreState: GenreState
): StateType