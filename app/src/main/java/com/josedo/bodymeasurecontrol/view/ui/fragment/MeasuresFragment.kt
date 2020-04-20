package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.view.adapter.MeasuresAdapter
import com.josedo.bodymeasurecontrol.view.adapter.MeasuresListener
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_measures.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class MeasuresFragment : Fragment(), MeasuresListener {

    private lateinit var measuresAdapter: MeasuresAdapter
    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val findDateListener = FindDateListener()
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

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.refresh()

        measuresAdapter = MeasuresAdapter(this.context!!, this)

        rvMeasures.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = measuresAdapter
        }

        observeViewModel()

        floating_action_button.setOnClickListener {
            viewModel.cleanDataInputFragment()
            val bundle = bundleOf("onlyEdit" to false)
            findNavController().navigate(R.id.dataInputFragment, bundle)
        }
    }

    fun observeViewModel() {
        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer {
            measuresAdapter.updateData(it)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it != null) {
                if (it)
                    rlBaseMeasures.visibility = View.VISIBLE
                else
                    rlBaseMeasures.visibility = View.INVISIBLE
            }
        })
    }

    override fun onMeasureClick(position: Int) {
        val bundle = bundleOf("position" to position)
        findNavController().navigate(R.id.measuresDetailsDialogFragment, bundle)
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

            for (i in viewModel.allEntryMeasures.value?.size!! - 1 downTo 0) {
                val entryMeasure = viewModel.allEntryMeasures.value!!.get(i)
                if (entryMeasure.dateMeasure == date) {
                    val bundle = bundleOf("position" to i)
                    findNavController().navigate(R.id.measuresDetailsDialogFragment, bundle)
                    break
                }
            }
        }
    }
}
