package com.pembelajar.musicrecommendation.commons

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.pembelajar.musicrecommendation.showErrorDialog

object Utilities {
    fun showErrorDialog(context: Context?, e: Throwable, okListener: (() -> Unit)?): AlertDialog? {
        return context?.showErrorDialog(e, okListener)
    }

    fun showErrorDialog(context: Context?, e: Exception): AlertDialog? {
        return this.showErrorDialog(context, e, null)
    }

    fun hideKeyBoard(view: View?){
        if (view != null) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun milliSecondsToTimer(milliseconds: Long): String {
        // media player show wrong duration sometimes, display 0.00 when duration greater than 100 minutes
        if (milliseconds < 0 || milliseconds > 100 * 60 * 1000) {
            return "0:00"
        }
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }

        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"

        // return timer string
        return finalTimerString
    }
}