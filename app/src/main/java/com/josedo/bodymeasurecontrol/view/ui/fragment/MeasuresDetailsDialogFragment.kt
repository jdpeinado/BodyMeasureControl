package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
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
            ViewModelProviders.of(this).get(ShareViewModel::class.java)} ?: throw Exception("Invalid Activity")

        toolbarMeasure.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarMeasure.setTitleTextColor(Color.WHITE)
        toolbarMeasure.setNavigationOnClickListener{
            dismiss()
        }
        toolbarMeasure.title = ""

        val position = arguments?.getSerializable(("position")) as Int
        val entryMeasure = viewModel.allEntryMeasures.value?.get(position)
        var entryMeasureBefore = entryMeasure
        if(position+1 < viewModel.allEntryMeasures.value!!.size)
            entryMeasureBefore = viewModel.allEntryMeasures.value?.get(position+1)

        setData(entryMeasure, entryMeasureBefore)

        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer { entryMeasure ->
            val entryMeasure = viewModel.allEntryMeasures.value?.get(position)
            var entryMeasureBefore = entryMeasure
            if(position+1 < viewModel.allEntryMeasures.value!!.size)
                entryMeasureBefore = viewModel.allEntryMeasures.value?.get(position+1)

            setData(entryMeasure, entryMeasureBefore)
        })

        floating_action_button_edit.setOnClickListener{
            viewModel.cleanDataInputFragment()

            val bundle = bundleOf("onlyEdit" to true, "entryMeasure" to entryMeasure)
            findNavController().navigate(R.id.dataInputFragment, bundle)
        }

    }

    private fun setBackgroundColor(difference: Float, view: View){
        when{
            (difference==0f) -> view.background = context!!.getDrawable(R.drawable.shape_measurement_blue)
            (difference>0f) -> view.background = context!!.getDrawable(R.drawable.shape_measurement_red)
            (difference<0f) -> view.background = context!!.getDrawable(R.drawable.shape_measurement_green)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun setData(entryMeasure: EntryMeasure?, entryMeasureBefore: EntryMeasure?){
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        tvDateMeas.text = simpleDateFormat.format(entryMeasure!!.dateMeasure)

        tvMeasurementValue.text = entryMeasure.bodyWeightValue.toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
        var difference:Float = entryMeasure.bodyWeightValue - entryMeasureBefore!!.bodyWeightValue
        var v = if(difference>=0f) "+" else "-"
        tvMeasurementDifference.text = v + difference.toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
        setBackgroundColor(difference, tvMeasurementDifference)

        tvChestValue.text = entryMeasure.chestValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.chestValue - entryMeasureBefore!!.chestValue
        v = if(difference>=0.0f) "+" else "-"
        tvChestDifference.text = v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setBackgroundColor(difference, tvChestDifference)

        tvWaistValue.text = entryMeasure.waistValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.waistValue - entryMeasureBefore!!.waistValue
        v = if(difference>=0.0f) "+" else "-"
        tvWaistDifference.text = v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setBackgroundColor(difference, tvWaistDifference)

        tvHipValue.text = entryMeasure.hipValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.hipValue - entryMeasureBefore!!.hipValue
        v = if(difference>=0.0f) "+" else "-"
        tvHipDifference.text = v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setBackgroundColor(difference, tvHipDifference)

        tvBicepValue.text = entryMeasure.bicepValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.bicepValue - entryMeasureBefore!!.bicepValue
        v = if(difference>=0.0f) "+" else "-"
        tvBicepDifference.text = v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setBackgroundColor(difference, tvBicepDifference)

        tvLegValue.text = entryMeasure.legValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        difference = entryMeasure.legValue - entryMeasureBefore!!.legValue
        v = if(difference>=0.0f) "+" else "-"
        tvLegDifference.text = v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
        setBackgroundColor(difference, tvLegDifference)
    }
}
