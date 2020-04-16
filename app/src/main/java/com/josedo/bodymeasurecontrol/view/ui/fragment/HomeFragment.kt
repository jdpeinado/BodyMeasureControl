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
import com.josedo.bodymeasurecontrol.util.Utils
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.math.BigDecimal
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
                    entryMeasureBefore = listEntryMeasure[listEntryMeasure.size-1]

                tvDateMeas.text = simpleDateFormat.format(entryMeasure.dateMeasure)

                tvMeasurementValue.text = entryMeasure.bodyWeightValue.toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
                var difference: Double = entryMeasure.bodyWeightValue - entryMeasureBefore.bodyWeightValue
                var v = if (difference >= 0.0) "+" else ""
                tvMeasurementDifference.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
                setBackgroundColor(difference, tvMeasurementDifference)
                setBackgroundColor(difference, vDetailWieght)

                tvChestValue.text = entryMeasure.chestValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.chestValue - entryMeasureBefore.chestValue
                v = if (difference >= 0.0) "+" else ""
                tvChestDifference.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvChestDifference)
                setBackgroundColor(difference, vDetailChest)

                tvWaistValue.text = entryMeasure.waistValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.waistValue - entryMeasureBefore.waistValue
                v = if (difference >= 0.0) "+" else ""
                tvWaistDifference.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvWaistDifference)
                setBackgroundColor(difference, vDetailWaist)

                tvHipValue.text = entryMeasure.hipValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.hipValue - entryMeasureBefore.hipValue
                v = if (difference >= 0.0) "+" else ""
                tvHipDifference.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvHipDifference)
                setBackgroundColor(difference, vDetailHip)

                tvBicepValue.text = entryMeasure.bicepValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.bicepValue - entryMeasureBefore.bicepValue
                v = if (difference >= 0.0) "+" else ""
                tvBicepDifference.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvBicepDifference)
                setBackgroundColor(difference, vDetailBicep)

                tvLegValue.text = entryMeasure.legValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.legValue - entryMeasureBefore.legValue
                v = if (difference >= 0.0) "+" else ""
                tvLegDifference.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setBackgroundColor(difference, tvLegDifference)
                setBackgroundColor(difference, vDetailLeg)
            }
        })

        floating_action_button.setOnClickListener{
            viewModel.cleanDataInputFragment()
            val bundle = bundleOf("onlyEdit" to false)
            findNavController().navigate(R.id.dataInputFragment, bundle)
        }
    }

    private fun setBackgroundColor(difference: Double, view: View){
        when{
            (difference==0.0) -> view.background = context!!.getDrawable(R.drawable.shape_measurement_blue)
            (difference>0.0) -> view.background = context!!.getDrawable(R.drawable.shape_measurement_red)
            (difference<0.0) -> view.background = context!!.getDrawable(R.drawable.shape_measurement_green)
        }
    }
}
