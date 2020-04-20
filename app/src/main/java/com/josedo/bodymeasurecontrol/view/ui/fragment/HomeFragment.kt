package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.util.ImageStorageManager
import com.josedo.bodymeasurecontrol.util.Utils
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.dialog_image.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import net.cachapa.expandablelayout.ExpandableLayout
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: ShareViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer { listEntryMeasure ->
            if (listEntryMeasure.size == 0) {

            } else {
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val entryMeasure = listEntryMeasure[0]
                var entryMeasureBefore = entryMeasure
                if (listEntryMeasure.size >= 2)
                    entryMeasureBefore = listEntryMeasure[listEntryMeasure.size - 1]

                tvDateMeas.text = simpleDateFormat.format(entryMeasure.dateMeasure)

                tvMeasurementValue.text =
                    entryMeasure.bodyWeightValue.toString() + entryMeasure.systemUnit.getWeightFormat(
                        context!!
                    )
                var difference: Double =
                    entryMeasure.bodyWeightValue - entryMeasureBefore.bodyWeightValue
                var v = if (difference >= 0.0) "+" else ""
                tvMeasurementDifference.text = v + Utils.getRoundNumberDecimal(difference)
                    .toString() + entryMeasure.systemUnit.getWeightFormat(context!!)
                setArrow(difference, tvMeasurementDifference)

                tvChestValue.text =
                    entryMeasure.chestValue.toString() + entryMeasure.systemUnit.getSizeFormat(
                        context!!
                    )
                difference = entryMeasure.chestValue - entryMeasureBefore.chestValue
                v = if (difference >= 0.0) "+" else ""
                tvChestDifference.text = v + Utils.getRoundNumberDecimal(difference)
                    .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setArrow(difference, tvChestDifference)

                tvWaistValue.text =
                    entryMeasure.waistValue.toString() + entryMeasure.systemUnit.getSizeFormat(
                        context!!
                    )
                difference = entryMeasure.waistValue - entryMeasureBefore.waistValue
                v = if (difference >= 0.0) "+" else ""
                tvWaistDifference.text = v + Utils.getRoundNumberDecimal(difference)
                    .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setArrow(difference, tvWaistDifference)

                tvHipValue.text =
                    entryMeasure.hipValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.hipValue - entryMeasureBefore.hipValue
                v = if (difference >= 0.0) "+" else ""
                tvHipDifference.text = v + Utils.getRoundNumberDecimal(difference)
                    .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setArrow(difference, tvHipDifference)

                tvBicepValue.text =
                    entryMeasure.bicepValue.toString() + entryMeasure.systemUnit.getSizeFormat(
                        context!!
                    )
                difference = entryMeasure.bicepValue - entryMeasureBefore.bicepValue
                v = if (difference >= 0.0) "+" else ""
                tvBicepDifference.text = v + Utils.getRoundNumberDecimal(difference)
                    .toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                setArrow(difference, tvBicepDifference)

                tvLegValue.text =
                    entryMeasure.legValue.toString() + entryMeasure.systemUnit.getSizeFormat(context!!)
                difference = entryMeasure.legValue - entryMeasureBefore.legValue
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

                val valuesWeight = ArrayList<Entry>()
                val valuesChest = ArrayList<Entry>()
                val valuesWaist = ArrayList<Entry>()
                val valuesHip = ArrayList<Entry>()
                val valuesBicep = ArrayList<Entry>()
                val valuesLeg = ArrayList<Entry>()

                var cont = 0
                for (i in viewModel.allEntryMeasures.value?.size!! - 1 downTo 0) {
                    val entryMeasure = viewModel.allEntryMeasures.value!!.get(i)
                    valuesWeight.add(Entry(cont.toFloat(), entryMeasure.bodyWeightValue.toFloat()))
                    valuesChest.add(Entry(cont.toFloat(), entryMeasure.chestValue.toFloat()))
                    valuesWaist.add(Entry(cont.toFloat(), entryMeasure.waistValue.toFloat()))
                    valuesHip.add(Entry(cont.toFloat(), entryMeasure.hipValue.toFloat()))
                    valuesBicep.add(Entry(cont.toFloat(), entryMeasure.bicepValue.toFloat()))
                    valuesLeg.add(Entry(cont.toFloat(), entryMeasure.legValue.toFloat()))
                    cont++
                }

                setChart(weightChart, valuesWeight)
                setChart(chestChart, valuesChest)
                setChart(waistChart, valuesWaist)
                setChart(hipChart, valuesHip)
                setChart(bicepChart, valuesBicep)
                setChart(legChart, valuesLeg)
            }
        })

        floating_action_button.setOnClickListener {
            viewModel.cleanDataInputFragment()
            val bundle = bundleOf("onlyEdit" to false)
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

        /*cvWeight.setOnClickListener {
            expandOrcollapse(elWeight, ibWeight)
        }

        cvChest.setOnClickListener {
            expandOrcollapse(elChest, ibChest)
        }
        cvWaist.setOnClickListener {
            expandOrcollapse(elWaist, ibWaist)
        }
        cvHip.setOnClickListener {
            expandOrcollapse(elHip, ibHip)
        }
        cvBicep.setOnClickListener {
            expandOrcollapse(elBicep, ibBicep)
        }
        cvLeg.setOnClickListener {
            expandOrcollapse(elLeg, ibLeg)
        }*/
    }

    private fun expandOrcollapse(expandableLayout: ExpandableLayout, imageView: ImageView) {
        if (expandableLayout.isExpanded) {
            imageView.background = resources.getDrawable(R.drawable.ic_expand_more)
            expandableLayout.collapse()
        } else {
            imageView.background = resources.getDrawable(R.drawable.ic_expand_less)
            expandableLayout.expand()
        }
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

    private fun setChart(chart: LineChart, values: ArrayList<Entry>) {
        chart.setBackgroundColor(Color.WHITE)
        chart.getDescription().setEnabled(false)
        chart.setTouchEnabled(false)
        chart.setDragEnabled(false)
        chart.setScaleEnabled(false)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        chart.setPinchZoom(false)

        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setEnabled(false)

        chart.getAxisRight().setEnabled(false)
        chart.getAxisLeft().setEnabled(false)

        chart.getXAxis().spaceMin = 0f
        chart.getXAxis().spaceMax = 0f
        chart.getXAxis().xOffset = 0f
        chart.getXAxis().yOffset = 0f
        chart.getAxisRight().spaceMin = 0f
        chart.getAxisRight().spaceMax = 0f
        chart.getAxisRight().xOffset = 0f
        chart.getAxisRight().yOffset = 0f
        chart.getAxisLeft().spaceMin = 0f
        chart.getAxisLeft().spaceMax = 0f
        chart.getAxisLeft().xOffset = 0f
        chart.getAxisLeft().yOffset = 0f

        setData(this.context!!.getString(R.string.chest), chart, values)
        chart.animateX(1500)
        val l: Legend = chart.getLegend()
        l.isEnabled = false

        chart.setViewPortOffsets(0f, 0f, 0f, 0f);

        chart.invalidate()

    }

    private fun setData(title: String, chart: LineChart, values: ArrayList<Entry>) {


        val set1: LineDataSet
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, title)
            set1.setDrawIcons(false)
            set1.color = resources.getColor(R.color.colorPrimary)
            //set1.setCircleColor(resources.getColor(R.color.colorPrimaryDark))
            set1.lineWidth = 2f
            //set1.circleRadius = 3f
            //set1.setDrawCircleHole(false)
            set1.formLineWidth = 2f
            //set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.valueTextSize = 9f
            //set1.enableDashedHighlightLine(10f, 0f, 0f)
            //set1.setDrawFilled(false)
            /*set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.getAxisLeft().getAxisMinimum() }
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this.context!!, R.drawable.shape_fade)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }*/

            set1.setDrawValues(false)
            set1.setDrawCircles(false)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets
            val data = LineData(dataSets)
            chart.setData(data)
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
}
