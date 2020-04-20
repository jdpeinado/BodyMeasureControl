package com.josedo.bodymeasurecontrol.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.util.Utils
import java.text.SimpleDateFormat

class MeasuresAdapter(val context: Context, val measuresListener: MeasuresListener): RecyclerView.Adapter<MeasuresAdapter.ViewHolder>() {

    var listEntryMeasures = ArrayList<EntryMeasure>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasuresAdapter.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_date_measures, parent, false)
        )

    override fun getItemCount(): Int = listEntryMeasures.size

    override fun onBindViewHolder(holder: MeasuresAdapter.ViewHolder, position: Int) {
        val entryMeasure = listEntryMeasures[position] as EntryMeasure

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        holder.tvMeasureDate.text = simpleDateFormat.format(entryMeasure.dateMeasure)

        holder.tvBodyWegihtValue.text = entryMeasure.bodyWeightValue.toString() + entryMeasure.systemUnit.getWeightFormat(context)

        if(position+1>=listEntryMeasures.size){
            holder.tvBodyweightDiferrence.text = "+0" + entryMeasure.systemUnit.getWeightFormat(context)
            holder.tvBodyweightDiferrence.setCompoundDrawablesWithIntrinsicBounds(
                null, null, null, null
            )
        } else {
            val entryMeasureBefore = listEntryMeasures[position + 1]

            val difference:Double = entryMeasure.bodyWeightValue - entryMeasureBefore.bodyWeightValue
            val v = if(difference>=0.0) "+" else ""
            holder.tvBodyweightDiferrence.text = v + Utils.getRoundNumberDecimal(difference).toString() + entryMeasure.systemUnit.getWeightFormat(context)
            when {
                (difference == 0.0) -> holder.tvBodyweightDiferrence.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null
                )
                (difference > 0.0) -> holder.tvBodyweightDiferrence.setCompoundDrawablesWithIntrinsicBounds(
                    context!!.getDrawable(R.drawable.ic_arrow_up), null, null, null
                )
                (difference < 0.0) -> holder.tvBodyweightDiferrence.setCompoundDrawablesWithIntrinsicBounds(
                    context!!.getDrawable(R.drawable.ic_arrow_down), null, null, null
                )
            }
        }

        holder.itemView.setOnClickListener{
            measuresListener.onMeasureClick(position)
        }
    }

    fun updateData(data: List<EntryMeasure>){
        listEntryMeasures.clear()
        listEntryMeasures.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMeasureDate = itemView.findViewById<TextView>(R.id.tvMeasureDate)
        val tvBodyWegihtValue = itemView.findViewById<TextView>(R.id.tvBodyWegihtValue)
        val tvBodyweightDiferrence = itemView.findViewById<TextView>(R.id.tvBodyweightDiferrence)

    }

}