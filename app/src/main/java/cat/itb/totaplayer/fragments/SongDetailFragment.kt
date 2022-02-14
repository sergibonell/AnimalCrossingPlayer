package cat.itb.totaplayer.fragments

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import cat.itb.totaplayer.R
import cat.itb.totaplayer.SongUtilities
import cat.itb.totaplayer.api.Song
import cat.itb.totaplayer.databinding.FragmentSongDetailBinding
import com.squareup.picasso.Picasso

class SongDetailFragment : Fragment() {
    private lateinit var binding: FragmentSongDetailBinding
    lateinit var data: Song
    lateinit var playButton: ImageButton
    lateinit var seekBar: SeekBar
    lateinit var mediaPlayer: MediaPlayer
    lateinit var songTitle: TextView
    lateinit var songCover: ImageView
    lateinit var runnable: Runnable
    var language = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as AppCompatActivity).supportActionBar?.hide()

        // Initialize variables and views
        data = arguments?.getParcelable<Song>("songData")!!
        playButton = binding.playButton
        seekBar = binding.seekbar
        songTitle = binding.songTitleDetail
        songCover = binding.songCoverDetail

        language = activity?.getPreferences(Context.MODE_PRIVATE)?.getInt("language", 0)!!
        songTitle.text = SongUtilities.getNameFromLang(language, data.name)
        Picasso.get().load(data.imageUri).into(songCover)

        // Create MediaPlayer object
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            setDataSource(data.musicUri)
            prepare()
        }

        // Add functionality to the play button
        playButton.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
                playButton.setImageResource(R.drawable.ic_baseline_pause_24)
            }else{
                mediaPlayer.pause()
                playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }

        // Set up SeekBar to move correctly and change song when dragged
        seekBar.progress = 0
        seekBar.max = mediaPlayer.duration
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if(changed){
                    mediaPlayer.seekTo(pos)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        // Create and start coroutine to move the SeekBar every second
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)

        // When the SeekBar reaches the end move it back to the beginning and reset the button
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekBar.progress = 0
        }
    }

    override fun onPause() {
        // Stop player when we leave the fragment
        if (mediaPlayer.isPlaying)
            mediaPlayer.stop()
        super.onPause()
    }
}