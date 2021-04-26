package com.pembelajar.musicrecommendation

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import com.pembelajar.musicrecommendation.commons.OkHttpHelper

fun Throwable.getUserUnderstandableError(context: Context?): String {
    return OkHttpHelper.getUserUnderstandableError(context ?: RekomendasiApp.instance, this)
}

fun Context?.showErrorDialog(e: Throwable, okListener: (() -> Unit)?): AlertDialog? {
    return showErrorDialog(e.getUserUnderstandableError(this), okListener)
}

fun Context?.showErrorDialog(message: String, okListener: (() -> Unit)?): AlertDialog? {
    return if (this == null) {
        AlertDialog.Builder(RekomendasiApp.instance).create()
    } else {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.ThemeAlertDialog))
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> okListener?.let { it() }}
            .show()
    }
}

fun Context?.showErrorDialog(e: Exception): AlertDialog? {
    return this?.let {
        return this.showErrorDialog(e, null)
    }
}