package com.eutech.pawprints.appointments.domain

import android.util.Log
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.AttendeeType
import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.appointments.data.appointment.InboxTpe
import com.eutech.pawprints.appointments.data.appointment.getMessage
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.doctors.domain.DOCTORS_COLLECTION
import com.eutech.pawprints.schedule.data.getEndDayOfMonth
import com.eutech.pawprints.schedule.data.getStartDayOfMonth
import com.eutech.pawprints.shared.data.pets.PETS_COLLECTION
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.users.USERS_COLLECTION
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import com.eutech.pawprints.shared.presentation.utils.generateRandownString
import com.eutech.pawprints.shared.presentation.utils.toEndTime
import com.eutech.pawprints.shared.presentation.utils.toStartTime
import com.eutech.pawprints.ui.custom.createLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.util.Date
const val INBOX_COLLECTION = "inbox"
class AppointmentRepositoryImpl(
    private val firestore: FirebaseFirestore,
): AppointmentRepository {
    override suspend fun createAppointment(
        appointments: Appointments,
        result: (Results<String>) -> Unit,
    ) {
        if (appointments.id == null) {
            result.invoke(Results.failuire("Failed creating appointment"))
            return
        }
        result.invoke(Results.loading("Create appointment"))
        firestore.collection(APPOINTMENT_COLLECTION)
            .document(appointments.id)
            .set(appointments)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Results.success("Successfully Created!"))
                } else {
                    result.invoke(Results.failuire("Failed creating appointment"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun getAppointment(
        localDate: LocalDate,
        result: (Results<List<Appointments>>) -> Unit,
    ) {
        result.invoke(Results.loading("Getting Appointment"))
        firestore.collection(APPOINTMENT_COLLECTION)
            .whereGreaterThanOrEqualTo("scheduleDate", localDate.getStartDayOfMonth())
            .whereLessThanOrEqualTo("scheduleDate", localDate.getEndDayOfMonth())
            .orderBy("scheduleDate",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(Results.failuire(it.message.toString()))
                    APPOINTMENT_COLLECTION.createLog(
                        message = it.message ?: "unknown error",
                        e = it
                    )
                }
                value?.let {
                    result.invoke(Results.success(it.toObjects(Appointments::class.java)))
                }
            }
    }

    override suspend fun updateAppointmentStatus(
        id: String,
        status: AppointmentStatus,
        result: (Results<String>) -> Unit,
    ) {
        firestore.collection(APPOINTMENT_COLLECTION)
            .document(id)
            .update("status",status)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Results.success("Successfully Updated!"))
                } else {
                    result.invoke(Results.failuire("Failed Updated appointment"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun getAppointmentWithAttendeesAndPets(
        localDate: LocalDate,
        result: (Results<List<AppointmentWithAttendeesAndPets>>) -> Unit
    ) {
        firestore.collection(APPOINTMENT_COLLECTION)
            .whereGreaterThanOrEqualTo("scheduleDate", localDate.getStartDayOfMonth())
            .whereLessThanOrEqualTo("scheduleDate", localDate.getEndDayOfMonth())
            .whereNotEqualTo("status", AppointmentStatus.CANCELLED)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .orderBy("scheduleDate", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    APPOINTMENT_COLLECTION.createLog(error.message.toString(), error)
                    result.invoke(Results.failuire(error.message.toString()))
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val appointments = snapshot.toObjects(Appointments::class.java)

                            // List to store the combined data
                            val appointmentDetailsList = appointments.map { appointment ->
                                // Fetch doctor info if any
                                val doctor = appointment.attendees
                                    .firstOrNull { it.type == AttendeeType.DOCTOR }
                                    ?.let { doctorAttendee ->
                                        firestore.collection(DOCTORS_COLLECTION)
                                            .document(doctorAttendee.id!!)
                                            .get()
                                            .await()
                                            .toObject(Doctors::class.java)
                                    }


                                val user = firestore.collection(USERS_COLLECTION)
                                    .document(appointment.userID!!)
                                    .get()
                                    .await()
                                    .toObject(Users::class.java)


                                val pets = if (appointment.pets.isNotEmpty()) {
                                    firestore.collection(PETS_COLLECTION)
                                        .whereIn("id", appointment.pets)
                                        .get()
                                        .await()
                                        .toObjects(Pet::class.java)
                                } else {
                                    emptyList()
                                }

                                AppointmentWithAttendeesAndPets(
                                    appointments = appointment,
                                    pets = pets,
                                    doctors = doctor,
                                    users = user
                                )
                            }

                            withContext(Dispatchers.Main) {
                                result.invoke(Results.success(appointmentDetailsList))
                            }

                        } catch (e: Exception) {
                            APPOINTMENT_COLLECTION.createLog(e.message.toString(), e)
                            result.invoke(Results.failuire(e.message.toString()))
                        }
                    }
                } else {
                    result.invoke(Results.success(emptyList()))
                }
            }
    }

    override suspend fun getAppointmentWithAttendeesAndPets(result: (Results<List<AppointmentWithAttendeesAndPets>>) -> Unit) {
        firestore.collection(APPOINTMENT_COLLECTION)
            .whereNotEqualTo("status", AppointmentStatus.CANCELLED)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .orderBy("scheduleDate", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    APPOINTMENT_COLLECTION.createLog(error.message.toString(), error)
                    result.invoke(Results.failuire(error.message.toString()))
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val appointments = snapshot.toObjects(Appointments::class.java)

                            // List to store the combined data
                            val appointmentDetailsList = appointments.map { appointment ->
                                // Fetch doctor info if any
                                val doctor = appointment.attendees
                                    .firstOrNull { it.type == AttendeeType.DOCTOR }
                                    ?.let { doctorAttendee ->
                                        firestore.collection(DOCTORS_COLLECTION)
                                            .document(doctorAttendee.id!!)
                                            .get()
                                            .await()
                                            .toObject(Doctors::class.java)
                                    }


                                val user = firestore.collection(USERS_COLLECTION)
                                    .document(appointment.userID!!)
                                    .get()
                                    .await()
                                    .toObject(Users::class.java)


                                val pets = if (appointment.pets.isNotEmpty()) {
                                    firestore.collection(PETS_COLLECTION)
                                        .whereIn("id", appointment.pets)
                                        .get()
                                        .await()
                                        .toObjects(Pet::class.java)
                                } else {
                                    emptyList()
                                }

                                AppointmentWithAttendeesAndPets(
                                    appointments = appointment,
                                    pets = pets,
                                    doctors = doctor,
                                    users = user
                                )
                            }

                            withContext(Dispatchers.Main) {
                                result.invoke(Results.success(appointmentDetailsList))
                            }

                        } catch (e: Exception) {
                            APPOINTMENT_COLLECTION.createLog(e.message.toString(), e)
                            result.invoke(Results.failuire(e.message.toString()))
                        }
                    }
                } else {
                    result.invoke(Results.success(emptyList()))
                }
            }
    }


    override suspend fun cancelAppointmentDueToPastDate(
        appointments: List<AppointmentWithAttendeesAndPets>,
        result: (Results<String>) -> Unit
    ) {
        val batch = firestore.batch()
        appointments.forEach {
            val inbox = Inbox(
                id = generateRandomNumber(8),
                userID = it.users?.id,
                collectionID = it.appointments.id,
                type = InboxTpe.APPOINTMENTS,
                message = "Sad news ${it.users?.name} your appointment was automatically cancelled as it was not completed by the scheduled date. Please contact us if you wish to reschedule."
            )
            val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
            batch.set(inboxRef,inbox)
            val docRef = firestore.collection(APPOINTMENT_COLLECTION).document(it.appointments.id!!)
            batch.update(
                docRef, "status", AppointmentStatus.CANCELLED
            )
        }
        batch.commit().addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(Results.success("Some appointment automatically cancelled due to as it was not completed by the scheduled date"))
            } else {
                result(Results.failuire("Auto cancel failed"))
            }
        }.addOnFailureListener {
            result(Results.failuire(it.message.toString()))
        }
    }

    override suspend fun getAllAppointments(result: (Results<List<AppointmentWithAttendeesAndPets>>) -> Unit) {
        firestore.collection(APPOINTMENT_COLLECTION)
    }

    override suspend fun getAppointmentByPetID(
        petID: String,
        result: (Results<List<Appointments>>) -> Unit
    ) {
        result.invoke(Results.loading("Loading"))
        delay(1000)
        firestore.collection(APPOINTMENT_COLLECTION)
            .whereArrayContains("pets",petID)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .orderBy("scheduleDate", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                APPOINTMENT_COLLECTION.createLog(message = "getting",error)
                value?.let {
                    val results = it.toObjects(Appointments::class.java)
                    result(Results.success(results))
                    APPOINTMENT_COLLECTION.createLog(message = results.toString(),error)
                }
                error?.let {
                    result(Results.failuire(it.message.toString()))
                }
            }
    }

    override suspend fun getMyAppointments(
        userID: String,
        result: (Results<List<Appointments>>) -> Unit
    ) {
        result.invoke(Results.loading("Getting appointments"))
        delay(1000)
        firestore.collection(APPOINTMENT_COLLECTION)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .orderBy("updatedAt",Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result.toObjects(Appointments::class.java)
                    result(Results.success(data))
                } else {
                    result.invoke(Results.failuire("failed getting appointments"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun updateAppointmentStatus(
        status: AppointmentStatus,
        appointments: Appointments,
        result: (Results<String>) -> Unit
    ) {
        val batch = firestore.batch()
        // Create inbox message for the user
        val inbox = Inbox(
            id = generateRandomNumber(8),
            userID = appointments.userID,
            collectionID = appointments.id,
            type = InboxTpe.APPOINTMENTS,
            message = status.getMessage()
        )
        val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
        batch.set(inboxRef, inbox)

        // Update appointment status to CONFIRMED
        val docRef = firestore.collection(APPOINTMENT_COLLECTION).document(appointments.id!!)
        batch.update(
            docRef, "status", status
        )

        try {
            // Commit the batch
            batch.commit().await()
            // Return success
            result(Results.success("Appointment successfully updated."))
        } catch (e: Exception) {
            // Handle failure
            result(Results.failuire(e.localizedMessage ?: "Failed to accept appointment."))
        }
    }


    companion object {
        const val APPOINTMENT_COLLECTION = "appointments"
    }
}