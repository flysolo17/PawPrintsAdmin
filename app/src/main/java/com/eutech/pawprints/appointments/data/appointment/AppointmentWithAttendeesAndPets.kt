package com.eutech.pawprints.appointments.data.appointment

import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.users.Users
import com.google.firebase.firestore.auth.User


data class AppointmentWithAttendeesAndPets(
    val appointments: Appointments,
    val pets : List<Pet>,
    val doctors: Doctors?,
    val users : Users ?
)