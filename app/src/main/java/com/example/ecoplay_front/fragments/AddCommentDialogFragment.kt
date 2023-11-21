package com.example.ecoplay_front.fragments

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.ecoplay_front.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddCommentDialogFragment : DialogFragment() {


    interface AddCommentDialogListener {
        fun onAttemptToAddComment(title: String, description: String, imageUri: Uri?, onResult: (Boolean) -> Unit)
    }


    private var imageUri: Uri? = null
    var listener: AddCommentDialogListener? = null
    private lateinit var imageView: ImageView
    private lateinit var pickImage: ActivityResultLauncher<String>

    private lateinit var takePicture: ActivityResultLauncher<Uri>


    fun setAddCommentListener(listener: AddCommentDialogListener) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageViewSelected)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialize pickImage here to ensure it's done before the fragment is created
        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                if (::imageView.isInitialized) {
                    imageView.visibility = View.VISIBLE
                    Glide.with(this).load(it).into(imageView)
                }
            }
        }
        listener = context as? AddCommentDialogListener

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                imageView.visibility = View.VISIBLE
                Glide.with(this).load(imageUri).into(imageView)
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {



        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_dialog_add_comment, null)
        imageView = view.findViewById(R.id.imageViewSelected)

        val dialogProgressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val dialogProgressBar2: ProgressBar = view.findViewById(R.id.progressBar2)

        val buttonSelectImage: Button = view.findViewById(R.id.buttonSelectImage)
        buttonSelectImage.setOnClickListener {
            showImageChoiceDialog()
        }

        val dialog = builder.setView(view)
            .setPositiveButton("Post", null)
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val title = view.findViewById<EditText>(R.id.editTextTitle).text.toString()
                val description = view.findViewById<EditText>(R.id.editTextDescription).text.toString()

                // Show the progress bar here
                dialogProgressBar.visibility = View.VISIBLE
                dialogProgressBar2.visibility = View.VISIBLE

                listener?.onAttemptToAddComment(title, description, imageUri) { success ->
                    dialogProgressBar.visibility = View.GONE
                    dialogProgressBar2.visibility = View.GONE

                    if (!success) {
                        // If the comment post was not successful, show the error but don't dismiss the dialog
                        showAlert("Inappropriate Content", "Your comment contains bad words. Please keep it cool & try again.")
                    } else {
                        // If the comment post was successful, dismiss the dialog
                        dialog.dismiss()
                    }
                }
            }
        }

        return dialog
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false) // Prevents the dialog from being canceled by back button or touches outside
            .show()
    }

    companion object {
        const val TAG = "AddCommentDialog"
    }

    private fun showImageChoiceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> takePhoto()

                    1 -> pickImage.launch("image/*")
                }
            }
            .show()
    }

    private fun takePhoto() {
        val photoUri = createImageUri()
        imageUri = photoUri
        Log.d("AddCommentDialogFragment", "Photo Uri: $photoUri")
        takePicture.launch(photoUri)
    }

    private fun createImageUri(): Uri? {
        val imagesDir = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_images").apply {
            if (!exists()) mkdirs()
        }
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(imagesDir, "JPEG_${timeStamp}_")

        return FileProvider.getUriForFile(
            requireContext(),
            "${context?.packageName}.provider",
            imageFile
        )
    }






}
