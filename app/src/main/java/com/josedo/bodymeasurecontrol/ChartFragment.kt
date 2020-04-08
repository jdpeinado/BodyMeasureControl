package com.josedo.bodymeasurecontrol

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_chart.*


/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val data = BarData(getXAxisValues(), getDataSet())
        val data = BarData(getDataSet())
        chart.data = data
        val description = Description()
        description.text = "My Chart"
        chart.setDescription(description)
        chart.animateXY(2000, 2000)
        chart.invalidate()
    }

    private fun getDataSet(): List<BarDataSet>? {
        var dataSets: ArrayList<BarDataSet>?
        val valueSet1: MutableList<BarEntry> = mutableListOf<BarEntry>()
        val v1e1 = BarEntry(110.000f, 0f) // Jan
        valueSet1.add(v1e1)
        val v1e2 = BarEntry(40.000f, 1f) // Feb
        valueSet1.add(v1e2)
        val v1e3 = BarEntry(60.000f, 2f) // Mar
        valueSet1.add(v1e3)
        val v1e4 = BarEntry(30.000f, 3f) // Apr
        valueSet1.add(v1e4)
        val v1e5 = BarEntry(90.000f, 4f) // May
        valueSet1.add(v1e5)
        val v1e6 = BarEntry(100.000f, 5f) // Jun
        valueSet1.add(v1e6)
        val valueSet2 = mutableListOf<BarEntry>()
        val v2e1 = BarEntry(150.000f, 0f) // Jan
        valueSet2.add(v2e1)
        val v2e2 = BarEntry(90.000f, 1f) // Feb
        valueSet2.add(v2e2)
        val v2e3 = BarEntry(120.000f, 2f) // Mar
        valueSet2.add(v2e3)
        val v2e4 = BarEntry(60.000f, 3f) // Apr
        valueSet2.add(v2e4)
        val v2e5 = BarEntry(20.000f, 4f) // May
        valueSet2.add(v2e5)
        val v2e6 = BarEntry(80.000f, 5f) // Jun
        valueSet2.add(v2e6)
        val barDataSet1 = BarDataSet(valueSet1, "Brand 1")
        barDataSet1.color = Color.rgb(0, 155, 0)
        val barDataSet2 = BarDataSet(valueSet2, "Brand 2")
        barDataSet2.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSets = ArrayList()
        dataSets.add(barDataSet1)
        dataSets.add(barDataSet2)
        return dataSets
    }

    private fun getXAxisValues(): ArrayList<Any>? {
        val xAxis = ArrayList<Any>()
        xAxis.add("JAN")
        xAxis.add("FEB")
        xAxis.add("MAR")
        xAxis.add("APR")
        xAxis.add("MAY")
        xAxis.add("JUN")
        return xAxis
    }

}
