package com.eutech.pawprints.appointments.data.appointment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppointmentStatus  {
    CONFIRMED, //green
    PENDING, //yellow
    CANCELLED,//red
    COMPLETED //dark green
}

fun AppointmentStatus?.getColor(): Color {
    return when (this) {
        AppointmentStatus.CONFIRMED -> Color(0xFF4CAF50)  // green
        AppointmentStatus.PENDING -> Color(0xFFFFEB3B)    // yellow
        AppointmentStatus.CANCELLED -> Color(0xFFF44336)  // red
        AppointmentStatus.COMPLETED -> Color(0xFF2E7D32)  // dark green
        else -> Color(0xFFF44336)
    }
}


fun AppointmentStatus?.getIcon(): ImageVector {
    return when (this) {
        AppointmentStatus.CONFIRMED -> Icons.Rounded.CheckCircle
        AppointmentStatus.PENDING -> Icons.Rounded.HourglassEmpty
        AppointmentStatus.CANCELLED -> Icons.Rounded.Cancel
        AppointmentStatus.COMPLETED -> Icons.Rounded.Done
        else -> Icons.Rounded.HelpOutline
    }
}


fun AppointmentStatus.getMessage(): String {
    return when (this) {
        AppointmentStatus.CONFIRMED -> "Your appointment is confirmed."
        AppointmentStatus.PENDING -> "Your appointment is pending approval."
        AppointmentStatus.CANCELLED -> "Your appointment has been cancelled."
        AppointmentStatus.COMPLETED -> "Your appointment has been successfully completed."
    }
}
