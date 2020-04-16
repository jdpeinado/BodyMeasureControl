package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.model.UnitMeasure
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_data_input.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DataInputFragment : DialogFragment() {
    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FulllScreenDialogStyle2)
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
                    tietWeight.setText(entryMeasure.bodyWeightValue.toString())
                    tietChest.setText(entryMeasure.chestValue.toString())
                    tietWaist.setText(entryMeasure.waistValue.toString())
                    tietHip.setText(entryMeasure.hipValue.toString())
                    tietBicep.setText(entryMeasure.bicepValue.toString())
                    tietLeg.setText(entryMeasure.legValue.toString())
                }
            }
        })

        val onlyEdit = arguments?.getSerializable(("onlyEdit")) as Boolean

        if(onlyEdit){
            bAdd.visibility = View.GONE
            bFindDate.visibility = View.GONE
            bClear.visibility = View.GONE
            bModify.isEnabled = true
            tietDate.isEnabled = false

            val entryMeasure = arguments?.getSerializable(("entryMeasure")) as EntryMeasure
            viewModel.entryMeasureToModify.value = entryMeasure
            toolbarAddMeasure.title = getString(R.string.editMeasurement)
        } else {
            toolbarAddMeasure.title = getString(R.string.add_edit_title)
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

        val findDateListener = FindDateListener()
        bFindDate.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd: DatePickerDialog = DatePickerDialog.newInstance(
                findDateListener,
                now[Calendar.YEAR],
                now[Calendar.MONTH],
                now[Calendar.DAY_OF_MONTH]
            )
            dpd.setVersion(DatePickerDialog.Version.VERSION_2)
            var cals: ArrayList<Calendar> = ArrayList<Calendar>()
            viewModel.allEntryMeasures.value?.forEach { entryMeasure ->
                val calendar = Calendar.getInstance()
                calendar.setTime(entryMeasure.dateMeasure)
                cals.add(calendar)
            }
            val array: Array<Calendar?> = arrayOfNulls<Calendar>(cals.size)
            cals.toArray(array)
            dpd.setSelectableDays(array)
            dpd.show(this.parentFragmentManager, "Datepickerdialog bodymeasurecontrol")
        }

        bAdd.setOnClickListener {
            if (checkViewEmpty()) {
                val simpleFormat = SimpleDateFormat("dd/MM/yyyy")
                val entryMeasure: EntryMeasure = EntryMeasure(
                    simpleFormat.parse(tietDate.text.toString()),
                    "",
                    "",
                    "",
                    UnitMeasure.METRIC,
                    tietChest.text.toString().toDouble(),
                    tietWaist.text.toString().toDouble(),
                    tietHip.text.toString().toDouble(),
                    tietLeg.text.toString().toDouble(),
                    tietBicep.text.toString().toDouble(),
                    tietWeight.text.toString().toDouble()
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

                viewModel.entryMeasureToModify.value?.dateMeasure = date
                viewModel.entryMeasureToModify.value?.chestValue =
                    tietChest.text.toString().toDouble()
                viewModel.entryMeasureToModify.value?.waistValue =
                    tietWaist.text.toString().toDouble()
                viewModel.entryMeasureToModify.value?.hipValue = tietHip.text.toString().toDouble()
                viewModel.entryMeasureToModify.value?.legValue = tietLeg.text.toString().toDouble()
                viewModel.entryMeasureToModify.value?.bicepValue =
                    tietBicep.text.toString().toDouble()
                viewModel.entryMeasureToModify.value?.bodyWeightValue =
                    tietWeight.text.toString().toDouble()
                viewModel.update(viewModel.entryMeasureToModify.value!!)
                Toast.makeText(
                    this.parentFragment?.context,
                    R.string.message_updatenewmeasure,
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }
        }

        bClear.setOnClickListener {
            viewModel.cleanDataInputFragment()
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

    inner class FindDateListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePickerDialog?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            val dateString =
                dayOfMonth.toString() + "/" + (monthOfYear + 1).toString() + "/" + year
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val date = simpleDateFormat.parse(dateString)

            viewModel.allEntryMeasures.value?.forEach { entryMeasure ->
                if (entryMeasure.dateMeasure == date) {
                    viewModel.entryMeasureToModify.value = entryMeasure
                    viewModel.dateEditTextIsEnabled.value = false
                    viewModel.modifyButtonIsEnabled.value = true
                    viewModel.addButtonIsEnabled.value = false
                }
            }
        }
    }

    private fun checkViewEmpty(): Boolean {
        var isOk: Boolean = true
        if (tietDate.text.toString().isEmpty()) {
            tietDate.setHint(R.string.message_date_not_enter)
            tietDate.setError(context?.getString(R.string.message_date_not_enter))
            isOk = false
        }
        if (tietWeight.text.toString().isEmpty()) {
            tietWeight.setHint(R.string.message_weight_not_enter)
            tietWeight.setError(context?.getString(R.string.message_weight_not_enter))
            isOk = false
        }
        if (tietChest.text.toString().isEmpty()) {
            tietChest.setHint(R.string.message_chest_not_enter)
            tietChest.setError(context?.getString(R.string.message_chest_not_enter))
            isOk = false
        }
        if (tietWaist.text.toString().isEmpty()) {
            tietWaist.setHint(R.string.message_waist_not_enter)
            tietWaist.setError(context?.getString(R.string.message_waist_not_enter))
            isOk = false
        }
        if (tietHip.text.toString().isEmpty()) {
            tietHip.setHint(R.string.message_hip_not_enter)
            tietHip.setError(context?.getString(R.string.message_hip_not_enter))
            isOk = false
        }
        if (tietBicep.text.toString().isEmpty()) {
            tietBicep.setHint(R.string.message_biceps_not_enter)
            tietBicep.setError(context?.getString(R.string.message_biceps_not_enter))
            isOk = false
        }
        if (tietLeg.text.toString().isEmpty()) {
            tietLeg.setHint(R.string.message_leg_not_enter)
            tietLeg.setError(context?.getString(R.string.message_leg_not_enter))
            isOk = false
        }

        return isOk
    }
}
