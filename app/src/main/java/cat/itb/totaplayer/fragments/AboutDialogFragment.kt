package cat.itb.totaplayer.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import cat.itb.totaplayer.R
import cat.itb.totaplayer.databinding.FragmentAboutDialogBinding
import cat.itb.totaplayer.databinding.FragmentLanguageDialogBinding

class AboutDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAboutDialogBinding
    private lateinit var acceptButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        binding = FragmentAboutDialogBinding.inflate(layoutInflater)

        // Find views
        acceptButton = binding.okayButton

        // Add button behaviour
        acceptButton.setOnClickListener {
            dialog?.dismiss()
        }

        // Build dialog
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }
    
    companion object{
        const val TAG = "AboutDialog"
    }

}