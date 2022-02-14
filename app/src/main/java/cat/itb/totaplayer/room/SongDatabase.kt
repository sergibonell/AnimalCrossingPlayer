package cat.itb.totaplayer.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cat.itb.totaplayer.api.Song

@Database(entities = arrayOf(Song::class), version = 1)
@TypeConverters(Converters::class)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
}