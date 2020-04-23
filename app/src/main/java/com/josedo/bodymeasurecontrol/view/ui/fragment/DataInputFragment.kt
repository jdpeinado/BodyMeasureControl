package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.model.UnitMeasure
import com.josedo.bodymeasurecontrol.util.ImageStorageManager
import com.josedo.bodymeasurecontrol.util.Utils
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.dialog_image.view.*
import kotlinx.android.synthetic.main.fragment_data_input.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DataInputFragment : DialogFragment() {
    private lateinit var viewModel: ShareViewModel
    private val PICK_FRONT_IMAGE = 100
    private val PICK_BACK_IMAGE = 200
    private val PICK_SIDE_IMAGE = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FulllScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        toolbarAddMeasure.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarAddMeasure.setTitleTextColor(Color.WHITE)
        toolbarAddMeasure.setNavigationOnClickListener {
            dismiss()
        }

        viewModel.entryMeasureToModify.observe(viewLifecycleOwner, Observer { entryMeasure ->
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            if (entryMeasure == null) {
                tietDate.setText("")
                tietWeight.setText("")
                tietChest.setText("")
                tietWaist.setText("")
                tietHip.setText("")
                tietBicep.setText("")
                tietLeg.setText("")
            } else {

                if (entryMeasure.bodyWeightValue == 0.0 && entryMeasure.chestValue == 0.0 && entryMeasure.waistValue == 0.0 && entryMeasure.hipValue == 0.0
                    && entryMeasure.bicepValue == 0.0 && entryMeasure.bicepValue == 0.0 && entryMeasure.legValue == 0.0
                ) {
                    tietDate.setText("")
                    tietWeight.setText("")
                    tietChest.setText("")
                    tietWaist.setText("")
                    tietHip.setText("")
                    tietBicep.setText("")
                    tietLeg.setText("")
                } else {
                    tietDate.setText(simpleDateFormat.format(entryMeasure.dateMeasure))
                    tietWeight.setText(UnitMeasure.fromKgToLb(this.context!!, entryMeasure.bodyWeightValue).toString())
                    tietChest.setText(UnitMeasure.fromCmToIn(this.context!!, entryMeasure.chestValue).toString())
                    tietWaist.setText(UnitMeasure.fromCmToIn(this.context!!, entryMeasure.waistValue).toString())
                    tietHip.setText(UnitMeasure.fromCmToIn(this.context!!, entryMeasure.hipValue).toString())
                    tietBicep.setText(UnitMeasure.fromCmToIn(this.context!!, entryMeasure.bicepValue).toString())
                    tietLeg.setText(UnitMeasure.fromCmToIn(this.context!!, entryMeasure.legValue).toString())

                    if (entryMeasure.frontPhotoUrl.isEmpty() && entryMeasure.backPhotoUrl.isEmpty() && entryMeasure.sidePhotoUrl.isEmpty()) {
                        ivFrontImage.visibility = View.GONE
                        ivBackImage.visibility = View.GONE
                        ivSideImage.visibility = View.GONE
                    } else {
                        ivFrontImage.visibility = View.VISIBLE
                        ivBackImage.visibility = View.VISIBLE
                        ivSideImage.visibility = View.VISIBLE
                        if (entryMeasure.frontPhotoUrl.isNotEmpty()) {
                            val bmpFront: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
                                this.context!!,
                                entryMeasure.frontPhotoUrl
                            )
                            ivFrontImage.setImageBitmap(bmpFront)
                        } else {
                            ivFrontImage.setImageBitmap(null)
                        }
                        if (entryMeasure.backPhotoUrl.isNotEmpty()) {
                            val bmpBack: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
                                this.context!!,
                                entryMeasure.backPhotoUrl
                            )
                            ivBackImage.setImageBitmap(bmpBack)
                        } else {
                            ivBackImage.setImageBitmap(null)
                        }
                        if (entryMeasure.sidePhotoUrl.isNotEmpty()) {
                            val bmpSide: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
                                this.context!!,
                                entryMeasure.sidePhotoUrl
                            )
                            ivSideImage.setImageBitmap(bmpSide)
                        } else {
                            ivSideImage.setImageBitmap(null)
                        }
                    }
                }
            }
        })

        val onlyEdit = arguments?.getSerializable(("onlyEdit")) as Boolean

        if (onlyEdit) {
            bAdd.visibility = View.GONE
            tietDate.isEnabled = false

            val entryMeasure = arguments?.getSerializable(("entryMeasure")) as EntryMeasure
            viewModel.entryMeasureToModify.value = entryMeasure
            toolbarAddMeasure.title = getString(R.string.editMeasurement)
        } else {
            bModify.visibility = View.GONE
            toolbarAddMeasure.title = getString(R.string.addMeasurement)
            viewModel.addButtonIsEnabled.observe(viewLifecycleOwner, Observer {
                bAdd.isEnabled = it
            })
            viewModel.modifyButtonIsEnabled.observe(viewLifecycleOwner, Observer {
                bModify.isEnabled = it
            })
            viewModel.dateEditTextIsEnabled.observe(viewLifecycleOwner, Observer {
                tietDate.isEnabled = it
            })
        }

        val selectDateListener = SelectDateListener()
        tietDate.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd: DatePickerDialog = DatePickerDialog.newInstance(
                selectDateListener,
                now[Calendar.YEAR],
                now[Calendar.MONTH],
                now[Calendar.DAY_OF_MONTH]
            )
            dpd.setVersion(DatePickerDialog.Version.VERSION_2)
            var cals: ArrayList<Calendar> = ArrayList<Calendar>()
            viewModel.allEntryMeasures.value?.forEach {
                val calendar = Calendar.getInstance()
                calendar.setTime(it.dateMeasure)
                cals.add(calendar)
            }
            val array: Array<Calendar?> = arrayOfNulls<Calendar>(cals.size)
            cals.toArray(array)
            dpd.setDisabledDays(array)
            dpd.show(this.parentFragmentManager, "Datepickerdialog bodymeasurecontrol")
        }

        bAdd.setOnClickListener {
            if (checkViewEmpty()) {
                val simpleFormat = SimpleDateFormat("dd/MM/yyyy")

                var frontPhotoUrl = getImageUrl(
                    ivFrontImage,
                    tietDate.text.toString().replace('/', '-') + "_front.jpeg"
                )
                var backPhotoUrl = getImageUrl(
                    ivBackImage,
                    tietDate.text.toString().replace('/', '-') + "_back.jpeg"
                )
                var sidePhotoUrl = getImageUrl(
                    ivSideImage,
                    tietDate.text.toString().replace('/', '-') + "_side.jpeg"
                )

                val entryMeasure: EntryMeasure = EntryMeasure(
                    simpleFormat.parse(tietDate.text.toString()),
                    frontPhotoUrl,
                    backPhotoUrl,
                    sidePhotoUrl,
                    UnitMeasure.METRIC,
                    UnitMeasure.fromInToCm(this.context!!, tietChest.text.toString().toDouble()),
                    UnitMeasure.fromInToCm(this.context!!, tietWaist.text.toString().toDouble()),
                    UnitMeasure.fromInToCm(this.context!!, tietHip.text.toString().toDouble()),
                    UnitMeasure.fromInToCm(this.context!!, tietLeg.text.toString().toDouble()),
                    UnitMeasure.fromInToCm(this.context!!, tietBicep.text.toString().toDouble()),
                    UnitMeasure.fromLbTokg(this.context!!, tietWeight.text.toString().toDouble())
                )
                viewModel.insert(entryMeasure)
                Toast.makeText(
                    this.parentFragment?.context,
                    R.string.message_addnewmeasure,
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }
        }

        bModify.setOnClickListener {
            if (checkViewEmpty()) {
                val simpleFormat = SimpleDateFormat("dd/MM/yyyy")
                val date = simpleFormat.parse(tietDate.text.toString())

                var frontPhotoUrl = getImageUrl(
                    ivFrontImage,
                    tietDate.text.toString().replace('/', '-') + "_front.jpeg"
                )
                var backPhotoUrl = getImageUrl(
                    ivBackImage,
                    tietDate.text.toString().replace('/', '-') + "_back.jpeg"
                )
                var sidePhotoUrl = getImageUrl(
                    ivSideImage,
                    tietDate.text.toString().replace('/', '-') + "_side.jpeg"
                )

                viewModel.entryMeasureToModify.value?.frontPhotoUrl = frontPhotoUrl
                viewModel.entryMeasureToModify.value?.backPhotoUrl = backPhotoUrl
                viewModel.entryMeasureToModify.value?.sidePhotoUrl = sidePhotoUrl
                viewModel.entryMeasureToModify.value?.dateMeasure = date
                viewModel.entryMeasureToModify.value?.chestValue = UnitMeasure.fromInToCm(this.context!!, tietChest.text.toString().toDouble())
                viewModel.entryMeasureToModify.value?.waistValue = UnitMeasure.fromInToCm(this.context!!, tietWaist.text.toString().toDouble())
                viewModel.entryMeasureToModify.value?.hipValue = UnitMeasure.fromInToCm(this.context!!, tietHip.text.toString().toDouble())
                viewModel.entryMeasureToModify.value?.legValue = UnitMeasure.fromInToCm(this.context!!, tietLeg.text.toString().toDouble())
                viewModel.entryMeasureToModify.value?.bicepValue = UnitMeasure.fromInToCm(this.context!!, tietBicep.text.toString().toDouble())
                viewModel.entryMeasureToModify.value?.bodyWeightValue = UnitMeasure.fromLbTokg(this.context!!, tietWeight.text.toString().toDouble())
                viewModel.update(viewModel.entryMeasureToModify.value!!)
                Toast.makeText(
                    this.parentFragment?.context,
                    R.string.message_updatenewmeasure,
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }
        }

        bAddFrontImage.setOnClickListener {
            showDialogLoadImage(PICK_FRONT_IMAGE)
        }

        bAddBackImage.setOnClickListener {
            showDialogLoadImage(PICK_BACK_IMAGE)
        }

        bAddSideImage.setOnClickListener {
            showDialogLoadImage(PICK_SIDE_IMAGE)
        }

        ivFrontImage.setOnClickListener {
            if(ivFrontImage.getDrawable() != null)
                showDialogImage(ivFrontImage)
        }

        ivBackImage.setOnClickListener {
            if(ivBackImage.getDrawable() != null)
                showDialogImage(ivBackImage)
        }

        ivSideImage.setOnClickListener {
            if(ivSideImage.getDrawable() != null)
                showDialogImage(ivSideImage)
        }
    }

    private fun showDialogLoadImage(type: Int){
        val mBuilder = AlertDialog.Builder(this.context)
            .setTitle(resources.getString(R.string.choose_gallery_camera))
            .setMessage(resources.getString(R.string.choose_gallery_camera_message))
            .setPositiveButton(
                resources.getString(R.string.gallery)
            ) { dialog, which ->
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, type)
            }
            .setNeutralButton(
                resources.getString(R.string.camera)
            ) { dialog, which ->
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    context?.packageManager?.let {
                        takePictureIntent.resolveActivity(it)?.also {
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                // Error occurred while creating the File
                                Toast.makeText(context, resources.getString(R.string.error_save_image), Toast.LENGTH_LONG).show()
                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {photoFile->
                                val photoURI: Uri = FileProvider.getUriForFile(
                                    this.context!!,
                                    "com.josedo.bodymeasurecontrol.fileprovider",
                                    photoFile
                                )
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(takePictureIntent, type)
                            }
                        }
                    }
                }
            }
            /*.setNeutralButton(
                resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })*/
        mBuilder.show()
    }

    var currentPhotoPath: String = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun showDialogImage(imageView: ImageView) {
        val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_image, null)
        val mBuilder = AlertDialog.Builder(this.context)
            .setView(mDialogView)
            .setPositiveButton(
                resources.getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(true)
        mDialogView.ivPhoto.setImageBitmap((imageView.getDrawable() as BitmapDrawable).bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_FRONT_IMAGE) {
            if (data?.getData()==null) {
                val file: File = File(currentPhotoPath);
                ivFrontImage.setImageURI(Uri.fromFile(file))
                file.delete()
                currentPhotoPath = ""
            }else {
                ivFrontImage.setImageURI(data?.getData())
            }
            ivFrontImage.visibility = View.VISIBLE
            ivBackImage.visibility = View.VISIBLE
            ivSideImage.visibility = View.VISIBLE
        }
        if (resultCode == RESULT_OK && requestCode == PICK_BACK_IMAGE) {
            if (data?.getData()==null) {
                val file: File = File(currentPhotoPath);
                ivBackImage.setImageURI(Uri.fromFile(file))
                file.delete()
                currentPhotoPath = ""
            }else {
                ivBackImage.setImageURI(data?.getData())
            }
            ivFrontImage.visibility = View.VISIBLE
            ivBackImage.visibility = View.VISIBLE
            ivSideImage.visibility = View.VISIBLE
        }
        if (resultCode == RESULT_OK && requestCode == PICK_SIDE_IMAGE) {
            if (data?.getData()==null) {
                val file: File = File(currentPhotoPath);
                ivSideImage.setImageURI(Uri.fromFile(file))
                file.delete()
                currentPhotoPath = ""
            }else {
                ivSideImage.setImageURI(data?.getData())
            }
            ivFrontImage.visibility = View.VISIBLE
            ivBackImage.visibility = View.VISIBLE
            ivSideImage.visibility = View.VISIBLE
        }
    }

    inner class SelectDateListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePickerDialog?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            val date =
                dayOfMonth.toString() + "/" + (monthOfYear + 1).toString() + "/" + year
            tietDate.setText(date)
        }
    }

    private fun checkViewEmpty(): Boolean {
        var isOk: Boolean = true
        if (tietDate.text.toString().isEmpty()) {
            tietDate.setError(context?.getString(R.string.message_date_not_enter))
            isOk = false
        }
        if (tietWeight.text.toString().isEmpty()) {
            tietWeight.setError(context?.getString(R.string.message_weight_not_enter))
            isOk = false
        }
        if (tietChest.text.toString().isEmpty()) {
            tietChest.setError(context?.getString(R.string.message_chest_not_enter))
            isOk = false
        }
        if (tietWaist.text.toString().isEmpty()) {
            tietWaist.setError(context?.getString(R.string.message_waist_not_enter))
            isOk = false
        }
        if (tietHip.text.toString().isEmpty()) {
            tietHip.setError(context?.getString(R.string.message_hip_not_enter))
            isOk = false
        }
        if (tietBicep.text.toString().isEmpty()) {
            tietBicep.setError(context?.getString(R.string.message_biceps_not_enter))
            isOk = false
        }
        if (tietLeg.text.toString().isEmpty()) {
            tietLeg.setError(context?.getString(R.string.message_leg_not_enter))
            isOk = false
        }

        return isOk
    }

    private fun getImageUrl(imageView: ImageView, nameImage: String): String {

        try {
            ImageStorageManager.deleteImageFromInternalStorage(context!!, nameImage)
        } catch (ex: Exception) {
            ex.printStackTrace()
            //image was not saved yet
        }

        var imageUrl = ""
        try {
            val bmp = (imageView.getDrawable() as BitmapDrawable).bitmap
            imageUrl = ImageStorageManager.saveToInternalStorage(
                context!!,
                bmp,
                nameImage
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            imageUrl = ""
        }

        return imageUrl

    }
}
