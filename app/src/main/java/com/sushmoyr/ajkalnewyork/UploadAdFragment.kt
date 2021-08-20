package com.sushmoyr.ajkalnewyork

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentUploadAdBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class UploadAdFragment : Fragment() {

    private var _binding: FragmentUploadAdBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainUserViewModel by activityViewModels()

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri: Uri = data?.data!!
                viewModel.uploadedImageUri.value = fileUri
                // HERE YOU USE THE FILEURI AS A URI OBJECT TO SET A IMAGE, SIMPLE!
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadAdBinding.inflate(inflater, container, false)

        setDatePicker()
        setSpinner()
        setImageUploader()

        viewModel.uploadedImageUri.observe(viewLifecycleOwner, {
            binding.adImageView.setImageURI(it)
        })

        return binding.root
    }

    /**
     * Image Upload Area
     */

    private fun setImageUploader() {
        binding.adImageButton.setOnClickListener{
            ImagePicker.with(requireActivity())
                .cropFreeStyle()
                .createIntentFromDialog {
                    startForProfileImageResult.launch(it)
                }
        }
    }

    /**
     * Ui handling area
     */

    private fun setSpinner() {

        val sizeArray: MutableList<String> = mutableListOf()

        viewModel.adSizes.observe(viewLifecycleOwner, { adSizes ->
            viewModel.lastFetchedAdSizeData = adSizes
            adSizes.forEach { item ->
                val content = "${item.name} (W: ${item.width}, H: ${item.height}) \n$${item.amount}"
                sizeArray.add(content)
            }

            sizeArray.forEach {
                print(it)
            }

            val autoCompleteTextView = binding.subscriptionType
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sizeArray)
            autoCompleteTextView.setAdapter(adapter)
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                viewModel.selectedItemPosition = position
            }

        })
    }

    private fun setDatePicker() {
        val datePickerButton = binding.expDate
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(currentTimeInMillis)
                .build()


        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(currentTimeInMillis)
                .setCalendarConstraints(constraintsBuilder)
                .build()

        datePickerButton.setOnClickListener {
            datePicker.show(activity?.supportFragmentManager!!, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener { currentSelectedDate ->
            val dateTime: LocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(currentSelectedDate),
                ZoneId.systemDefault()
            )
            val dateAsFormattedText: String =
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            binding.expDate.setText(dateAsFormattedText)
        }
    }

    //TODO add OnDestroy()

}