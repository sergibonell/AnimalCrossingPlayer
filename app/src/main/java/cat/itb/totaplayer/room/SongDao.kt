package cat.itb.totaplayer.room

import androidx.room.*
import cat.itb.totaplayer.api.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM SongEntity")
    fun getFavouriteSongs(): List<Song>

    @Query("SELECT COUNT(*) FROM SongEntity WHERE id=:id")
    fun checkIfFavourite(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSong(songEntity: Song)

    @Delete
    fun deleteSong(songEntity: Song)
}