package cat.itb.totaplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cat.itb.totaplayer.api.Song
import cat.itb.totaplayer.databinding.SongViewBinding
import cat.itb.totaplayer.room.SongApplication
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongAdapter(songList: List<Song>, onClickListener: OnClickListener, titleLanguage: Int): RecyclerView.Adapter<SongAdapter.MainViewHolder>() {
    private val songs = songList
    private val listener = onClickListener
    private val language = titleLanguage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = SongViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(songs[position])
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_item)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class MainViewHolder(binding: SongViewBinding): RecyclerView.ViewHolder(binding.root) {
        private var coverImageView : ImageView
        private var nameTextView : TextView

        // Get views
        init {
            coverImageView = binding.songCover
            nameTextView = binding.songTitle
        }

        // Set view values and click behaviour
        fun bindData(songData: Song){
            Picasso.get().load(songData.imageUri).into(coverImageView)
            nameTextView.text = SongUtilities.getNameFromLang(language, songData.name)

            CoroutineScope(Dispatchers.Main).launch{
                var favourite = false
                withContext(Dispatchers.IO){favourite = SongApplication.database.songDao().checkIfFavourite(songData.id) == 1}

                if(favourite){
                    coverImageView.setBackgroundColor((ContextCompat.getColor(itemView.context, R.color.secondaryColor)))
                    nameTextView.setBackgroundColor((ContextCompat.getColor(itemView.context, R.color.secondaryDarkColor)))
                }else{
                    coverImageView.setBackgroundColor((ContextCompat.getColor(itemView.context, R.color.primaryColor)))
                    nameTextView.setBackgroundColor((ContextCompat.getColor(itemView.context, R.color.primaryDarkColor)))
                }
            }

            itemView.setOnClickListener {
                listener.onClick(songData)
            }

            itemView.setOnLongClickListener {
                listener.onClickFavorite(songData, absoluteAdapterPosition)
                true
            }
        }

    }
}