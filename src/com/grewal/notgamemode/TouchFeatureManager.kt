/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.grewal.notgamemode

import android.util.Log

object TouchFeatureManager {

    private const val TAG = "GameModeTouchFeature"

    const val TOUCH_GAME_MODE = 0
    const val TOUCH_UP_THRESHOLD = 2
    const val TOUCH_TOLERANCE = 3
    const val TOUCH_AIM_SENSITIVITY = 4
    const val TOUCH_TAP_STABILITY = 5
    const val TOUCH_EXPERT_MODE = 6
    const val TOUCH_EDGE_FILTER = 7
    const val TOUCH_PANEL_ORIENTATION = 8
    const val TOUCH_SUPER_REPORT = 202

    data class ModeRange(val min: Int, val max: Int, val def: Int)

    val TUNING_RANGES =
        linkedMapOf(
            TOUCH_UP_THRESHOLD to ModeRange(0, 4, 3),
            TOUCH_TOLERANCE to ModeRange(0, 4, 2),
            TOUCH_AIM_SENSITIVITY to ModeRange(0, 4, 2),
            TOUCH_TAP_STABILITY to ModeRange(0, 4, 2),
            TOUCH_EDGE_FILTER to ModeRange(0, 3, 2),
        )

    val EXPERT_RANGE = ModeRange(1, 3, 1)

    data class ModeQuery(
        val cur: Int?,
        val def: Int?,
        val min: Int?,
        val max: Int?,
        val values: List<Int>?,
    )

    fun isAvailable(): Boolean = SysfsTouchBackend.isAvailable()

    private fun setModeValue(mode: Int, value: Int) = SysfsTouchBackend.setModeValue(mode, value)

    fun setGameMode(enabled: Boolean) {
        Log.i(TAG, "setGameMode: $enabled")
        setModeValue(TOUCH_GAME_MODE, if (enabled) 1 else 0)
        setSuperReport(enabled)
    }

    fun setSuperReport(enabled: Boolean) {
        Log.i(TAG, "setSuperReport: $enabled")
        setModeValue(TOUCH_SUPER_REPORT, if (enabled) 1 else 0)
    }

    fun setPanelOrientation(rotation: Int) {
        Log.i(TAG, "setPanelOrientation: $rotation")
        setModeValue(TOUCH_PANEL_ORIENTATION, rotation)
    }

    fun setTuning(mode: Int, value: Int) = setModeValue(mode, value)

    fun queryMode(mode: Int): ModeQuery = SysfsTouchBackend.queryMode(mode)
}
