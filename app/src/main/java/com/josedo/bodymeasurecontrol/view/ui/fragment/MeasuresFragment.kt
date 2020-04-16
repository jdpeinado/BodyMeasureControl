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
import androidx.recyclerview.widget.LinearLayoutManager
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.EntryMeasure
import com.josedo.bodymeasurecontrol.view.adapter.MeasuresAdapter
import com.josedo.bodymeasurecontrol.view.adapter.MeasuresListener
import com.josedo.bodymeasurecontrol.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.fragment_measures.*

/**
 * A simple [Fragment] subclass.
 */
class MeasuresFragment : Fragment(), MeasuresListener {

    private lateinit var measuresAdapter: MeasuresAdapter
    private lateinit var viewModel: ShareViewModel

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
            ViewModelProviders.of(this).get(ShareViewModel::class.java)} ?: throw Exception("Invalid Activity")
        viewModel.refresh()

        measuresAdapter = MeasuresAdapter(this.context!!, this)

        rvMeasures.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
            adapter = measuresAdapter
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.allEntryMeasures.observe(viewLifecycleOwner, Observer {
            measuresAdapter.updateData(it)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer<Boolean>{
            if(it!= null){
                if(it)
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

}
