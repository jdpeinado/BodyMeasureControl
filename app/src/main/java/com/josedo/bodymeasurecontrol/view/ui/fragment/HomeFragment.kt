package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment()
{
    private lateinit var viewModel: ShareViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
        ViewModelProviders.of(this).get(ShareViewModel::class.java)} ?: throw Exception("Invalid Activity")

        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer {listEntryMeasure ->
            if (listEntryMeasure.size == 0) {

            }else{
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val entryMeasure = listEntryMeasure[0]
                var entryMeasureBefore = entryMeasure
                if (listEntryMeasure.size >= 2)
                    entryMeasureBefore = listEntryMeasure[1]

                tvDateMeas.text = simpleDateFormat.format(entryMeasure.dateMeasure)

                tvMeasurementValue.text =
                    entryMeasure.bodyWeightValue.toString() + entryMeasure.systemUnit.getWeightFormat(
                        context!!
                    )
                var difference: Float =
                    entryMeasure.bodyWeightValue - entryMeasureBefore.bodyWeightValue
                var v = if (difference >= 0f) "+" else "-"
                tvMeasurementDifference.text =
                    v + difference.toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
                setBackgroundColor(difference, tvMeasurementDifference)

                tvChestValue.text =
                    entryMeasure.chestValue.toString() + entryMeasure.systemUnit.getSizeFormat(
                        context!!
                    )
                difference = entryMeasure.chestValue - entryMeasureBefore.chestValue
                v = if (difference >= 0.0f) "+" else "-"
                tvChestDifference.text =
                    v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvChestDifference)

                tvWaistValue.text =
                    entryMeasure.waistValue.toString() + entryMeasure.systemUnit.getSizeFormat(
                        context!!
                    )
                difference = entryMeasure.waistValue - entryMeasureBefore.waistValue
                v = if (difference >= 0.0f) "+" else "-"
                tvWaistDifference.text =
                    v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvWaistDifference)

                tvHipValue.text =
                    entryMeasure.hipValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.hipValue - entryMeasureBefore.hipValue
                v = if (difference >= 0.0f) "+" else "-"
                tvHipDifference.text =
                    v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvHipDifference)

                tvBicepValue.text =
                    entryMeasure.bicepValue.toString() + entryMeasure.systemUnit.getSizeFormat(
                        context!!
                    )
                difference = entryMeasure.bicepValue - entryMeasureBefore.bicepValue
                v = if (difference >= 0.0f) "+" else "-"
                tvBicepDifference.text =
                    v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvBicepDifference)

                tvLegValue.text =
                    entryMeasure.legValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.legValue - entryMeasureBefore.legValue
                v = if (difference >= 0.0f) "+" else "-"
                tvLegDifference.text =
                    v + difference.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvLegDifference)
            }
        })

        floating_action_button.setOnClickListener{
            viewModel.cleanDataInputFragment()
            val bundle = bundleOf("onlyEdit" to false)
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
}
