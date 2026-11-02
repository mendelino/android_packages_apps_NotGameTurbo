/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.grewal.notgamemode

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.preference.Preference
import com.android.settingslib.widget.SettingsBasePreferenceFragment

class DebugFragment : SettingsBasePreferenceFragment() {

    private val rows = mutableListOf<Pair<Preference, Int>>()

    private val modes =
        listOf(
            R.string.touch_test_game_mode to 0,
            R.string.tune_up_threshold to 2,
            R.string.tune_tolerance to 3,
            R.string.tune_aim_sensitivity to 4,
            R.string.tune_tap_stability to 5,
            R.string.expert_mode_title to 6,
            R.string.tune_edge_filter to 7,
            R.string.debug_panel_orientation to 8,
            R.string.super_report_title to 202,
        )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = requireContext()
        val screen = preferenceManager.createPreferenceScreen(context)

        screen.addPreference(
            Preference(context).apply {
                title = getString(R.string.debug_refresh)
                setOnPreferenceClickListener {
                    refresh()
                    true
                }
            }
        )

        modes.forEach { (labelRes, mode) ->
            val pref =
                Preference(context).apply {
                    title = getString(R.string.debug_mode_title, getString(labelRes), mode)
                    summary = getString(R.string.debug_querying)
                    isSelectable = false
                    isIconSpaceReserved = false
                }
            screen.addPreference(pref)
            rows.add(pref to mode)
        }

        preferenceScreen = screen
        refresh()
    }

    private fun refresh() {
        rows.forEach { (pref, _) -> pref.summary = getString(R.string.debug_querying) }
        val handler = Handler(Looper.getMainLooper())
        Thread {
                val results =
                    rows.map { (pref, mode) -> pref to TouchFeatureManager.queryMode(mode) }
                handler.post { results.forEach { (pref, q) -> pref.summary = format(q) } }
            }
            .start()
    }

    private fun format(q: TouchFeatureManager.ModeQuery): String {
        fun s(v: Int?) = v?.toString() ?: "—"
        val values = q.values?.joinToString(",", "[", "]") ?: "—"
        return getString(
            R.string.debug_mode_values,
            s(q.cur),
            s(q.def),
            s(q.min),
            s(q.max),
            values,
        )
    }
}
