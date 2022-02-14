package cat.itb.totaplayer.room

import androidx.room.TypeConverter
import cat.itb.totaplayer.api.Name
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun StringToName(value: String): Name {
        return Gson().fromJson(value, Name::class.java)
    }

    @TypeConverter
    fun NameToString(value: Name): String {
        return Gson().toJson(value)
    }
}