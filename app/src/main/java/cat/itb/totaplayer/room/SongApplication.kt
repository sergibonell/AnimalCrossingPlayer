package cat.itb.totaplayer.room

import android.app.Application
import androidx.room.Room

class SongApplication: Application() {
    companion object {
        lateinit var database: SongDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,
            SongDatabase::class.java,
            "SongDatabase").build()
    }
}