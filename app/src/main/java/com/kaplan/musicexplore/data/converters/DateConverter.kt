package com.kaplan.musicexplore.data.converters

import androidx.room.TypeConverter
import org.joda.time.LocalDate
import java.util.*

class DateConverter {
  @TypeConverter
  fun dateToString(date: LocalDate?): String? {
    return date?.toString()
  }

  @TypeConverter
  fun stringToDate(value: String?): LocalDate? {
    return if (value == null) null else LocalDate.parse(value)
  }

  @TypeConverter
  fun toDate(dateLong: Long): Date? {
    return Date(dateLong)
  }

  @TypeConverter
  fun fromDate(date: Date?): Long? {
    return date?.time;
  }
}