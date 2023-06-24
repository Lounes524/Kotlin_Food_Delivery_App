package com.example.tdm.loginApp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.tdm.R
import com.example.tdm.databinding.FragmentRegisterBinding



class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val requestCode = 400
    private lateinit var setProfilePictureImageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setProfilePictureImageView.setOnClickListener(){
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGalleryIntent()
            } else {
                checkPermission()
            }
        }
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val imageUri = result.data?.data
                    if (imageUri != null) {
                        val imageBitmap =
                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                        val resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 300, 300, true)
                        binding.setProfilePictureImageView.setImageBitmap(resizedBitmap)
                    }
                }
            }
        binding.confirmSignUpBtn.setOnClickListener(){
                view: View ->view.findNavController().navigate(R.id.action_registerFragment_to_mainActivity)
        }


    }
    private fun openGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    private fun checkPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGalleryIntent()
        }
    }

}