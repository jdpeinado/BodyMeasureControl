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

        setData(entryMeasure, entryMeasureBefore)

        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer { entryMeasure ->
            val entryMeasure = viewModel.allEntryMeasures.value?.get(position)
            var entryMeasureBefore = entryMeasure
            if (position + 1 < viewModel.allEntryMeasures.value!!.size)
                entryMeasureBefore = viewModel.allEntryMeasures.value?.get(position + 1)

            setData(entryMeasure, entryMeasureBefore)
        })

        floating_action_button_edit.setOnClickListener {
            viewModel.cleanDataInputFragment()

            val bundle = bundleOf("onlyEdit" to true, "entryMeasure" to entryMeasure)
            findNavController().navigate(R.id.dataInputFragment, bundle)
        }

        ivFrontImage.setOnClickListener {
            showDialogImage(ivFrontImage)
        }

        ivBackImage.setOnClickListener {
            showDialogImage(ivBackImage)
        }
        ivSideImage.setOnClickListener {
            showDialogImage(ivSideImage)
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

    private fun showDialogImage(imageView: ImageView){
        val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_image, null)
        val mBuilder = AlertDialog.Builder(this.context)
            .setView(mDialogView)
            .setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
        val  mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(true)
        mDialogView.ivPhoto.setImageBitmap((imageView.getDrawable() as BitmapDrawable).bitmap)
    }

    private fun setData(entryMeasure: EntryMeasure?, entryMeasureBefore: EntryMeasure?) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        tvDateMeas.text = simpleDateFormat.format(entryMeasure!!.dateMeasure)

        tvMeasurementValue.text =
            entryMeasure.bodyWeightValue.toString() + entryMeasure.systemUnit.getWeightFormat(
                context!!
            )
        var difference: Double = entryMeasure.bodyWeightValue - entryMeasureBefore!!.bodyWeightValue
        var v = if (difference >= 0.0) "+" else ""
        tvMeasurementDifference.text = v + Utils.getRoundNumberDecimal(difference)
            .toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
        setArrow(difference, tvMeasurementDifference)

        tvChestValue.text =
            entryMeasure.chestValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.chestValue - entryMeasureBefore!!.chestValue
        v = if (difference >= 0.0) "+" else ""
        tvChestDifference.text = v + Utils.getRoundNumberDecimal(difference)
            .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setArrow(difference, tvChestDifference)

        tvWaistValue.text =
            entryMeasure.waistValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.waistValue - entryMeasureBefore!!.waistValue
        v = if (difference >= 0.0) "+" else ""
        tvWaistDifference.text = v + Utils.getRoundNumberDecimal(difference)
            .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setArrow(difference, tvWaistDifference)

        tvHipValue.text =
            entryMeasure.hipValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.hipValue - entryMeasureBefore!!.hipValue
        v = if (difference >= 0.0) "+" else ""
        tvHipDifference.text = v + Utils.getRoundNumberDecimal(difference)
            .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setArrow(difference, tvHipDifference)

        tvBicepValue.text =
            entryMeasure.bicepValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.bicepValue - entryMeasureBefore!!.bicepValue
        v = if (difference >= 0.0) "+" else ""
        tvBicepDifference.text = v + Utils.getRoundNumberDecimal(difference)
            .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setArrow(difference, tvBicepDifference)

        tvLegValue.text =
            entryMeasure.legValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.legValue - entryMeasureBefore!!.legValue
        v = if (difference >= 0.0) "+" else ""
        tvLegDifference.text = v + Utils.getRoundNumberDecimal(difference)
            .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setArrow(difference, tvLegDifference)

        if (entryMeasure.frontPhotoUrl.isEmpty() && entryMeasure.backPhotoUrl.isEmpty() && entryMeasure.sidePhotoUrl.isEmpty()) {
            lyImages.visibility = View.GONE
        } else {
            lyImages.visibility = View.VISIBLE
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
