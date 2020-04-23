package com.josedo.bodymeasurecontrol.view.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.text.InputType
import androidx.preference.*
import com.josedo.bodymeasurecontrol.R
import com.josedo.bodymeasurecontrol.model.UnitMeasure
import com.josedo.bodymeasurecontrol.util.Utils


class PreferencesFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_preferences, rootKey)

        val editTextPreference =
            preferenceManager.findPreference<EditTextPreference>("height")
        editTextPreference!!.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        PreferenceManager.setDefaultValues(
            this.context, R.xml.fragment_preferences,
            false
        )
        initSummary(preferenceScreen)
    }

    override fun onResume() {
        super.onResume()
        // Set up a listener whenever a key changes
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the listener whenever a key changes
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun initSummary(p: Preference) {
        if (p is PreferenceGroup) {
            val pGrp: PreferenceGroup = p
            for (i in 0 until pGrp.preferenceCount) {
                initSummary(pGrp.getPreference(i))
            }
        } else {
            updatePrefSummary(p)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, keyString: String?) {
        val pref: Preference? = keyString?.let { findPreference(it) }

        updatePrefSummary(pref)

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val metric_system = prefs.getString(
            context!!.getString(R.string.metric_system_key),
            UnitMeasure.METRIC.value.toString()
        )
        val height = prefs.getString(
            context?.getString(R.string.height_key),
            null
        )

        if (metric_system != null && height!=null) {
            if (metric_system.toInt().equals(UnitMeasure.METRIC.value)){
                if (pref is ListPreference) {
                    val key = resources.getString(R.string.height_key)
                    val value = Utils.getRoundNumberDecimal(height.toDouble()/3.28084, 2).toString()
                    val prefHeight : Preference? = key.let { findPreference(key) }
                    if (prefHeight is EditTextPreference) {
                        prefHeight.text = value
                        prefHeight.setSummary(value)
                    }
                    val editor: Editor = prefs.edit()
                    editor.putString(key, value)
                    editor.apply()
                }
            }else {
                if (pref is ListPreference) {
                    val key = resources.getString(R.string.height_key)
                    val value = Utils.getRoundNumberDecimal(height.toDouble()*3.28084, 2).toString()
                    val prefHeight : Preference? = key.let { findPreference(key) }
                    if (prefHeight is EditTextPreference) {
                        prefHeight.text = value
                        prefHeight.setSummary(value)
                    }
                    val editor: Editor = prefs.edit()
                    editor.putString(key, value)
                    editor.apply()
                }
            }
        }
    }

    private fun updatePrefSummary(pref: Preference?) {
        if (pref is ListPreference) {
            val listPref: ListPreference = pref
            pref.setSummary(listPref.entry)
        }
        if (pref is EditTextPreference) {
            val editTextPref = pref
            pref.setSummary(editTextPref.text)
        }
    }

}