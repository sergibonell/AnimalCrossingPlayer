package cat.itb.totaplayer

import cat.itb.totaplayer.api.Song

interface OnClickListener {
    fun onClick(songData: Song)
    fun onClickFavorite(songData: Song, position: Int)
}