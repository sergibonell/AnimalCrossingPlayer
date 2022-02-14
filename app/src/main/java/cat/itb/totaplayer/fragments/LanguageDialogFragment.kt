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
import cat.itb.totaplayer.databinding.FragmentLanguageDialogBinding

class LanguageDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentLanguageDialogBinding
    private lateinit var cancelButton: Button
    private lateinit var acceptButton: Button
    private lateinit var languageSpinner: Spinner

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        binding = FragmentLanguageDialogBinding.inflate(layoutInflater)

        // Inflate view with the custom dialog layout
        val view = layoutInflater.inflate(R.layout.fragment_language_dialog, null)

        // Find views
        cancelButton = binding.cancelButton
        acceptButton = binding.okayButton
        languageSpinner = binding.languageSpinner

        // Add button behaviour
        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }

        acceptButton.setOnClickListener {
            updateValue()
            dialog?.dismiss()
        }

        // Initialize Spinner with Adapter from languages array
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = adapter
        }

        // Set selected value to current language
        val currentLanguage = activity?.getPreferences(Context.MODE_PRIVATE)?.getInt("language", 0)
        languageSpinner.setSelection(currentLanguage!!)

        // Build dialog
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }
    
    companion object{
        const val TAG = "LanguageSelectionDialog"
    }

    // Get value from spinner and send it back to parent fragment
    private fun updateValue(){
        val newLanguage = languageSpinner.selectedItemPosition
        setFragmentResult("requestKey", bundleOf("language" to newLanguage))
    }

}