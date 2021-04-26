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
}