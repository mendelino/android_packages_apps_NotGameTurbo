/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.grewal.notgamemode

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity

class GameModeActivity : CollapsingToolbarBaseActivity() {

    companion object {
        private const val TAG = "GameModeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .replace(
                com.android.settingslib.collapsingtoolbar.R.id.content_frame,
                GameModeFragment(),
                TAG,
            )
            .commit()

        startService(Intent(this, GameModeService::class.java))

        checkTouchNodes()
    }

    private fun checkTouchNodes() {
        Thread {
                val available = TouchFeatureManager.isAvailable()
                runOnUiThread {
                    if (!available && !isFinishing) {
                        showTouchNodesError()
                    }
                }
            }
            .start()
    }

    private fun showTouchNodesError() {
        AlertDialog.Builder(this)
            .setTitle(R.string.sysfs_error_title)
            .setMessage(R.string.sysfs_error_message)
            .setCancelable(false)
            .setPositiveButton(R.string.retry) { _, _ -> checkTouchNodes() }
            .setNegativeButton(R.string.exit) { _, _ -> finish() }
            .show()
    }
}
