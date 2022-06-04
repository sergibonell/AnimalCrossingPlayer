package cat.itb.totaplayer.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SongEntity")
data class Song(
    @SerializedName("buy-price")
    val buyPrice: Int,
    @SerializedName("file-name")
    val fileName: String,
    @PrimaryKey
    val id: Int,
    @SerializedName("image_uri")
    val imageUri: String,
    val isOrderable: Boolean,
    @SerializedName("music_uri")
    val musicUri: String,
    val name: Name,
    @SerializedName("sell-price")
    val sellPrice: Int
) : Parcelable