package cat.itb.totaplayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.totaplayer.api.ApiInterface
import cat.itb.totaplayer.api.Song
import cat.itb.totaplayer.room.SongApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel: ViewModel() {
    var songs = MutableLiveData<List<Song>>()
    var favourites = false

    init {
        fetchSongs()
    }

    fun fetchSongs(){
        if(favourites){
            CoroutineScope(Dispatchers.IO).launch { songs.postValue(SongApplication.database.songDao().getFavouriteSongs()) }
        }
        else {
            val apiInterface = ApiInterface.create()
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO) { apiInterface.getSongs() }
                if (response.isSuccessful)
                    songs.postValue(response.body())
                else
                    Log.e("ERROR: ", response.message())
            }
        }
    }
}
