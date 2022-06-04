package cat.itb.totaplayer.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.itb.totaplayer.*
import cat.itb.totaplayer.api.Song
import cat.itb.totaplayer.databinding.FragmentSongListBinding
import cat.itb.totaplayer.room.SongApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SongListFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentSongListBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var recyclerView: RecyclerView
    private var language = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongListBinding.inflate(layoutInflater)
        return binding.root
    }

    // Set ResultListener for language selection dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            // We get the value for language we selected in the dialog fragment
            val result = bundle.getInt("language")
            if (result != language) {
                updateLanguagePrefs(result)

                // Save the state of the RecyclerView layout so it doesn't scroll back to top on language change
                val recyclerViewState = (recyclerView.layoutManager as GridLayoutManager).onSaveInstanceState()
                setupRecyclerView(viewModel.songs.value)
                (recyclerView.layoutManager as GridLayoutManager).onRestoreInstanceState(recyclerViewState)
            }
        }
    }

    // Setup RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recyclerView = binding.recycler
        language = activity?.getPreferences(Context.MODE_PRIVATE)?.getInt("language", 0)!!

        viewModel.songs.observe(viewLifecycleOwner, Observer {
            setupRecyclerView(it)
        })
    }

    private fun setupRecyclerView(songs: List<Song>?) {
        // If we're in landscape mode show more results per row
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
        else
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.adapter = SongAdapter(songs!!, this, language)
    }

    private fun reloadSongs() {
        viewModel.fetchSongs()
        viewModel.songs.observe(viewLifecycleOwner, Observer {
            setupRecyclerView(it)
        })
    }

    // Behaviour when clicking an item, go to song detail
    override fun onClick(songData: Song) {
        val action = SongListFragmentDirections.actionSongListFragmentToSongDetailFragment(songData)
        findNavController().navigate(action)
    }

    // Behaviour when long clicking an item, add song to favourites
    override fun onClickFavorite(songData: Song, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if(SongApplication.database.songDao().checkIfFavourite(songData.id) == 1){
                withContext(Dispatchers.Main){ Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show() }
                SongApplication.database.songDao().deleteSong(songData)
            }else{
                withContext(Dispatchers.Main){ Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show() }
                SongApplication.database.songDao().addSong(songData)
            }
        }

        // Update the RecyclerView individual View with the new colors
        recyclerView.adapter?.notifyItemChanged(position)
    }

    // Inflate options menu with the layout we created
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Add behavior to every option from the options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_all -> selectAll()
            R.id.show_favs -> selectFavorites()
            R.id.language -> openLanguageDialog()
            R.id.about -> openAboutDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectAll(){
        viewModel.favourites = false
        reloadSongs()
    }

    private fun selectFavorites(){
        viewModel.favourites = true
        reloadSongs()
    }

    private fun openLanguageDialog(){
        LanguageDialogFragment().show(childFragmentManager, LanguageDialogFragment.TAG)
    }

    private fun openAboutDialog(){
        AboutDialogFragment().show(childFragmentManager, AboutDialogFragment.TAG)
    }

    // Update current language and settings in SharedPreferences
    private fun updateLanguagePrefs(n : Int){
        language = n
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("language", n)
            apply()
        }
    }
}