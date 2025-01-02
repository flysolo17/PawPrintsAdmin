package com.eutech.pawprints.pets.view_pets

import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.PetWithOwner
import com.eutech.pawprints.shared.data.transactions.Transaction


data class ViewPetState(
    val isLoading : Boolean = false,
    val records : List<MedicalRecordWithDoctor> = emptyList(),
    val errors : String ? = null,
    val messages : String? = null,
    val petWithOwner: PetWithOwner ? = null,


    val isPetDeleted : String? = null,
    //adding info
    val isAddingInfo : Boolean  = false,


    //doctor state
    val doctors : List<Doctors> = emptyList(),

    //medical
    val isGettingMedicalRecord : Boolean = false,
    val isAddingMedicalRecord : Boolean = false,

    //appointment

    val isGettingPetSchedule : Boolean = false,
    val petAppointmentError : String ? = null,

    val appointments : List<Appointments> = emptyList(),


)