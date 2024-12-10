package com.eutech.pawprints.pets

import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.utils.Results


data class PetState(
    val isLoading : Boolean = false,
    val pets : List<Pet> = emptyList(),
    val filteredPets : List<Pet> = emptyList(),
    val errors : String ? = null,
    val selectedPet : Pet? = null,
    val searchText : String = "",

    val isAddingInfo : Boolean = false,
    val isGettingPetSchedule : Boolean = false,
    val selectedPetAppointments : List<Appointments> = emptyList(),
    val medicalRecords : List<MedicalRecordWithDoctor> = emptyList(),
    val petAppointmentError : String ? = null,
    val messages : String ? = null,


    val isGettingDoctors : Boolean =false,
    val doctors : List<Doctors> = emptyList(),


    val isGettingMedicalRecord : Boolean = false,
    val isAddingMedicalRecord : Boolean = false
)