package com.eutech.pawprints.shared.data.pets

import android.os.Build
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date


const val PETS_COLLECTION = "pets"
data class Pet(
    val id: String? = null,
    var ownerID: String? = null,
    val name: String? = null,
    var image: String? = null,
    val species: String? = null,
    val breed: String? = null,
    val birthday: String? = null,
    val otherDetails: Map<String, String>? = null,
    val createdAt : Date?= Date(),
    val updatedAt : Date? = Date()
)

fun String.getAge(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("MMM, dd yyyy")
        val birthday: LocalDate = try {
            LocalDate.parse(this, formatter)
        } catch (e: DateTimeParseException) {
            return "Invalid date format"
        }

        val today = LocalDate.now()
        val period = Period.between(birthday, today)
        val years = period.years
        val months = period.months
        val days = period.days

        return when {
            years > 0 && months > 0 -> "$years year${if (years > 1) "s" else ""} and $months month${if (months > 1) "s" else ""} old"
            years > 0 -> "$years year${if (years > 1) "s" else ""} old"
            months > 0 && days > 0 -> "$months month${if (months > 1) "s" else ""} and $days day${if (days > 1) "s" else ""} old"
            months > 0 -> "$months month${if (months > 1) "s" else ""} old"
            days > 0 -> "$days day${if (days > 1) "s" else ""} old"
            else -> "Today"
        }
    } else {
        return  "unknown age"
    }  // Adjust the format based on your birthday string format
}
