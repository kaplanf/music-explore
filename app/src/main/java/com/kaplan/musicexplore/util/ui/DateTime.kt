package com.kaplan.musicexplore.util.ui

import android.content.Context
import com.kaplan.musicexplore.R
import org.joda.time.LocalTime
import java.text.SimpleDateFormat
import java.util.*


fun getReadableTime(context: Context,
                    localTime: LocalTime
): String? {
  var hourOfDay = localTime.hourOfDay
  val minute = localTime.minuteOfHour
  var periodSuffix = context.getString(R.string.time_format_am)
  if (hourOfDay > 12) {
    hourOfDay -= 12
    periodSuffix = context.getString(R.string.time_format_pm)
  } else if (hourOfDay == 12) {
    periodSuffix = context.getString(R.string.time_format_pm)
  }
  return String.format(Locale.ENGLISH, "%02d:%02d %s", hourOfDay, minute,
    periodSuffix)
}

fun getReadableDate(date: Date): String? {
  val sdfReadable = SimpleDateFormat("dd MMM yyyy",
    Locale.ENGLISH)
  return sdfReadable.format(date)
}

fun getReadableHour(date: Date): String?{
  val sdfReadable = SimpleDateFormat("h:mm a",
    Locale.ENGLISH)
  return sdfReadable.format(date)
}