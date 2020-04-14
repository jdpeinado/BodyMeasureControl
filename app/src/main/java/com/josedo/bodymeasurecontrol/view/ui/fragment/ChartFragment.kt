package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {
    private lateinit var viewModel: ShareViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        /*val valuesWeight = ArrayList<Entry>()
        val valuesChest = ArrayList<Entry>()
        val valuesWaist = ArrayList<Entry>()
        val valuesHip = ArrayList<Entry>()
        val valuesBicep = ArrayList<Entry>()
        val valuesLeg = ArrayList<Entry>()
        for(i in viewModel.allEntryMeasures.value?.size!!-1 downTo 0){
            //viewModel.allEntryMeasures.value?.forEachIndexed {i, entryMeasure ->
            val entryMeasure = viewModel.allEntryMeasures.value!!.get(i)
            valuesWeight.add(Entry(i.toFloat(),entryMeasure.bodyWeightValue))
            valuesChest.add(Entry(i.toFloat(),entryMeasure.chestValue))
            valuesWaist.add(Entry(i.toFloat(),entryMeasure.waistValue))
            valuesHip.add(Entry(i.toFloat(),entryMeasure.hipValue))
            valuesBicep.add(Entry(i.toFloat(),entryMeasure.bicepValue))
            valuesLeg.add(Entry(i.toFloat(),entryMeasure.legValue))
        }


        setChart(weightChart,valuesWeight)
        setChart(chestChart,valuesChest)
        setChart(waistChart,valuesWaist)
        setChart(hipChart,valuesHip)
        setChart(bicepChart,valuesBicep)
        setChart(legChart,valuesLeg)*/

        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer { listEntryMeasure ->

            val valuesWeight = ArrayList<Entry>()
            val valuesChest = ArrayList<Entry>()
            val valuesWaist = ArrayList<Entry>()
            val valuesHip = ArrayList<Entry>()
            val valuesBicep = ArrayList<Entry>()
            val valuesLeg = ArrayList<Entry>()

            var cont = 0
            for(i in viewModel.allEntryMeasures.value?.size!!-1 downTo 0){
            //viewModel.allEntryMeasures.value?.forEachIndexed {i, entryMeasure ->
                val entryMeasure = viewModel.allEntryMeasures.value!!.get(i)
                valuesWeight.add(Entry(cont.toFloat(),entryMeasure.bodyWeightValue.toFloat()))
                valuesChest.add(Entry(cont.toFloat(),entryMeasure.chestValue.toFloat()))
                valuesWaist.add(Entry(cont.toFloat(),entryMeasure.waistValue.toFloat()))
                valuesHip.add(Entry(cont.toFloat(),entryMeasure.hipValue.toFloat()))
                valuesBicep.add(Entry(cont.toFloat(),entryMeasure.bicepValue.toFloat()))
                valuesLeg.add(Entry(cont.toFloat(),entryMeasure.legValue.toFloat()))
                cont++
            }

            setChart(weightChart,valuesWeight)
            setChart(chestChart,valuesChest)
            setChart(waistChart,valuesWaist)
            setChart(hipChart,valuesHip)
            setChart(bicepChart,valuesBicep)
            setChart(legChart,valuesLeg)
            viewModel.viewModelScope.launch {

            }
        })
    }

    private fun setChart(chart: LineChart, values: ArrayList<Entry>){
        chart.setBackgroundColor(Color.WHITE)
        chart.getDescription().setEnabled(false)
        chart.setTouchEnabled(false)
        chart.setDragEnabled(false)
        chart.setScaleEnabled(false)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        chart.setPinchZoom(true)

        var xAxis: XAxis = chart.getXAxis()
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.setDrawLabels(false);

        var yAxis: YAxis = chart.getAxisLeft()
        chart.getAxisRight().setEnabled(false)

        yAxis.enableGridDashedLine(10f, 10f, 0f)

        setData(this.context!!.getString(R.string.chest), chart, values)
        chart.animateX(1500)
        val l: Legend = chart.getLegend()
        l.isEnabled = false
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
            set1.enableDashedLine(10f, 5f, 0f)
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.valueTextSize = 9f
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.getAxisLeft().getAxisMinimum() }
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this.context!!, R.drawable.shape_fade)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }

            set1.setDrawFilled(true)
            set1.setDrawValues(false)
            set1.setDrawCircles(false)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets
            val data = LineData(dataSets)
            chart.setData(data)
        }
    }

}
