package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.model.UnitMeasure
import com.josedo.bodymeasurecontrol.util.ImageStorageManager
import com.josedo.bodymeasurecontrol.util.Utils
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.dialog_image.view.*
import kotlinx.android.synthetic.main.fragment_measures_details_dialog.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 */
class MeasuresDetailsDialogFragment : DialogFragment() {

    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FulllScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measures_details_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        toolbarMeasure.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarMeasure.setTitleTextColor(Color.WHITE)
        toolbarMeasure.setNavigationOnClickListener {
            dismiss()
        }
        toolbarMeasure.title = getString(R.string.measureDetails)

        val position = arguments?.getSerializable(("position")) as Int
        val entryMeasure = viewModel.allEntryMeasures.value?.get(position)
        var entryMeasureBefore = entryMeasure
        if (position + 1 < viewModel.allEntryMeasures.value!!.size)
            entryMeasureBefore = viewModel.allEntryMeasures.value?.get(position + 1)

        setData(entryMeasure, entryMeasureBefore!!)

        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer { listEntryMeasures ->
            val entryMeasureChanged = listEntryMeasures.get(position)
            var entryMeasureBeforeChanged = entryMeasureChanged
            if (position + 1 < listEntryMeasures.size)
                entryMeasureBeforeChanged = listEntryMeasures.get(position + 1)

            setData(entryMeasureChanged, entryMeasureBeforeChanged)
        })

        floating_action_button_edit.setOnClickListener {
            viewModel.cleanDataInputFragment()

            val bundle = bundleOf("onlyEdit" to true, "entryMeasure" to entryMeasure)
            findNavController().navigate(R.id.dataInputFragment, bundle)
        }

        ivFrontImage.setOnClickListener {
            if (ivFrontImage.getDrawable() != null)
                viewModel.allEntryMeasures.value?.get(position)?.frontPhotoUrl?.let { it1 ->
                    showDialogImage(
                        it1
                    )
                }
        }

        ivBackImage.setOnClickListener {
            if (ivBackImage.getDrawable() != null)
                viewModel.allEntryMeasures.value?.get(position)?.backPhotoUrl?.let { it1 ->
                    showDialogImage(
                        it1
                    )
                }
        }

        ivSideImage.setOnClickListener {
            if (ivSideImage.getDrawable() != null)
                viewModel.allEntryMeasures.value?.get(position)?.sidePhotoUrl?.let { it1 ->
                    showDialogImage(
                        it1
                    )
                }
        }

    }

    private fun setArrow(difference: Double, view: TextView) {
        when {
            (difference == 0.0) -> view.setCompoundDrawablesWithIntrinsicBounds(
                null, null, null, null
            )
            (difference > 0.0) -> view.setCompoundDrawablesWithIntrinsicBounds(
                context!!.getDrawable(R.drawable.ic_arrow_up), null, null, null
            )
            (difference < 0.0) -> view.setCompoundDrawablesWithIntrinsicBounds(
                context!!.getDrawable(R.drawable.ic_arrow_down), null, null, null
            )
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun showDialogImage(urlImage: String) {
        val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_image, null)
        val mBuilder = AlertDialog.Builder(this.context)
            .setView(mDialogView)
            .setPositiveButton(
                resources.getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(true)
        val bmpFront: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
            urlImage
        )
        mDialogView.ivPhoto.setImageBitmap(bmpFront)
    }

    private fun setData(entryMeasure: EntryMeasure?, entryMeasureBefore: EntryMeasure) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        tvDateMeas.text = simpleDateFormat.format(entryMeasure!!.dateMeasure)

        tvMeasurementValue.text =
            UnitMeasure.fromKgToLbString(this.context!!, entryMeasure.bodyWeightValue)
        var difference: Double =
            entryMeasure.bodyWeightValue - entryMeasureBefore.bodyWeightValue
        var v = if (difference >= 0.0) "+" else ""
        tvMeasurementDifference.text =
            v + UnitMeasure.fromKgToLbString(this.context!!, difference)
        setArrow(difference, tvMeasurementDifference)

        tvChestValue.text = UnitMeasure.fromCmToInString(this.context!!, entryMeasure.chestValue)
        difference = entryMeasure.chestValue - entryMeasureBefore.chestValue
        v = if (difference >= 0.0) "+" else ""
        tvChestDifference.text = v + UnitMeasure.fromCmToInString(this.context!!, difference)
        setArrow(difference, tvChestDifference)

        tvWaistValue.text = UnitMeasure.fromCmToInString(this.context!!, entryMeasure.waistValue)
        difference = entryMeasure.waistValue - entryMeasureBefore.waistValue
        v = if (difference >= 0.0) "+" else ""
        tvWaistDifference.text = v + UnitMeasure.fromCmToInString(this.context!!, difference)
        setArrow(difference, tvWaistDifference)

        tvHipValue.text = UnitMeasure.fromCmToInString(this.context!!, entryMeasure.hipValue)
        difference = entryMeasure.hipValue - entryMeasureBefore.hipValue
        v = if (difference >= 0.0) "+" else ""
        tvHipDifference.text = v + UnitMeasure.fromCmToInString(this.context!!, difference)
        setArrow(difference, tvHipDifference)

        tvBicepValue.text = UnitMeasure.fromCmToInString(this.context!!, entryMeasure.bicepValue)
        difference = entryMeasure.bicepValue - entryMeasureBefore.bicepValue
        v = if (difference >= 0.0) "+" else ""
        tvBicepDifference.text = v + UnitMeasure.fromCmToInString(this.context!!, difference)
        setArrow(difference, tvBicepDifference)

        tvLegValue.text = UnitMeasure.fromCmToInString(this.context!!, entryMeasure.legValue)
        difference = entryMeasure.legValue - entryMeasureBefore.legValue
        v = if (difference >= 0.0) "+" else ""
        tvLegDifference.text = v + UnitMeasure.fromCmToInString(this.context!!, difference)
        setArrow(difference, tvLegDifference)

        if (entryMeasure.frontPhotoUrl.isEmpty() && entryMeasure.backPhotoUrl.isEmpty() && entryMeasure.sidePhotoUrl.isEmpty()) {
            lyImages.visibility = View.GONE
        } else {
            lyImages.visibility = View.VISIBLE
            if (entryMeasure.frontPhotoUrl.isNotEmpty()) {
                try {
                    val bmpFront: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
                        ImageStorageManager.getThumbnailFilename(entryMeasure.frontPhotoUrl)
                    )
                    ivFrontImage.setImageBitmap(bmpFront)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                if (ivFrontImage.drawable != null)
                    ivFrontImage.setImageResource(0)
            }
            if (entryMeasure.backPhotoUrl.isNotEmpty()) {
                try {
                    val bmpBack: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
                        ImageStorageManager.getThumbnailFilename(entryMeasure.backPhotoUrl)
                    )
                    ivBackImage.setImageBitmap(bmpBack)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                if (ivBackImage.drawable != null)
                    ivBackImage.setImageResource(0)
            }
            if (entryMeasure.sidePhotoUrl.isNotEmpty()) {
                try {
                    val bmpSide: Bitmap? = ImageStorageManager.getImageFromInternalStorage(
                        ImageStorageManager.getThumbnailFilename(entryMeasure.sidePhotoUrl)
                    )
                    ivSideImage.setImageBitmap(bmpSide)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                if (ivSideImage.drawable != null)
                    ivSideImage.setImageResource(0)
            }
        }
    }
}
