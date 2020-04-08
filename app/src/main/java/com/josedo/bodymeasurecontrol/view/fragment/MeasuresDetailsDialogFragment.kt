package com.josedo.bodymeasurecontrol.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.josedo.bodymeasurecontrol.R


/**
 * A simple [Fragment] subclass.
 */
class MeasuresDetailsDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measures_details_dialog, container, false)
    }
}
