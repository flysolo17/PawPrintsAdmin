package com.eutech.pawprints.shared.presentation.utils

import android.content.Context
import android.widget.Toast
import com.eutech.pawprints.products.data.products.Discount
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random


fun generateRandownString(length: Int = 10): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
       .map { Random.nextInt(0, charPool.size)
       .let { charPool[it] } }.joinToString("")

}

fun Double.displayPrice(discount: Discount?): Double {
    val price = this
    return if (discount == null || discount.expiration?.time!! < System.currentTimeMillis()) {
        price
    } else {
        price - (price * discount.value / 100)
    }
}

fun generateRandomNumber(length: Int = 10): String {
    val charPool: List<Char> = ('0'..'9').toList()
    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

data class TextFieldData(
    val value : String = "",
    val hasError : Boolean = false,
    val errorMessage : String ? = null
)


fun Double.toPhp(): String {
    return "₱ %.2f".format(this)
}


fun Date.toExpireFormat(): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.toCreatedAt() : String {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return dateFormat.format(this)
}
fun Date.toStocks() : String {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.getDefault())
    return dateFormat.format(this)
}

fun Context.toast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}


fun Date.toStartTime(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toStartTime
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}

fun Date.toEndTime(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toEndTime
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return calendar.time
}


fun Date.toStartMonth(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toStartMonth
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}

fun Date.toEndOfMonth(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toEndOfMonth
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return calendar.time
}

