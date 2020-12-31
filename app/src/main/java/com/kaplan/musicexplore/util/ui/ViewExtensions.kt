package com.kaplan.musicexplore.util.ui

import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.setTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar!!.title = title
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

private val negative = floatArrayOf(
    -1.0f,     .0f,     .0f,    .0f,  255.0f,
    .0f,   -1.0f,     .0f,    .0f,  255.0f,
    .0f,     .0f,   -1.0f,    .0f,  255.0f,
    .0f,     .0f,     .0f,   1.0f,     .0f
)

fun Drawable.toNegative() {
    this.colorFilter = ColorMatrixColorFilter(negative)
}