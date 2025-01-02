package com.eutech.pawprints.doctors.domain

import android.net.Uri
import com.eutech.pawprints.doctors.data.DoctorWithSchedules
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.domain.ScheduleRepositoryImpl
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
const val DOCTORS_COLLECTION = "doctors";
class DoctorRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): DoctorRepository {
    override suspend fun createDoctor(doctors: Doctors,uri: Uri ?, result: (Results<String>) -> Unit) {
        try {
            if (doctors.id == null) {
                result.invoke(Results.failuire("User not found!"))
                return
            }
            result.invoke(Results.loading("Creating Doctors"))
            if (uri != null) {
                val storageRef = storage.reference.child("${DOCTORS_COLLECTION}/${doctors.id}/${uri.lastPathSegment}")
                val result = storageRef.putFile(uri).await()
                val url = storageRef.downloadUrl.await()
                doctors.profile = url.toString()
            }
            firestore.collection(DOCTORS_COLLECTION)
                .document(doctors.id!!)
                .set(doctors)
                .await()
            result.invoke(Results.success("Successfully Created!"))
        } catch (e : Exception) {
            result.invoke(Results.failuire(e.message.toString()))
        }
    }

    override suspend fun getAllDoctors(result: (Results<List<Doctors>>) -> Unit) {
        result.invoke(Results.loading("Getting All Doctors"))
        firestore.collection(DOCTORS_COLLECTION)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(Results.failuire(it.message.toString()))
                }
                value?.let {
                    result.invoke(Results.success(it.toObjects(Doctors::class.java)))
                }
            }
    }

    override suspend fun deleteDoctor(doctors: Doctors, result: (Results<String>) -> Unit) {
        try {
            if (!doctors.profile.isNullOrEmpty()) {
                storage.getReferenceFromUrl(doctors.profile!!).delete().await()
            }
            
            firestore.collection(DOCTORS_COLLECTION)
                .document(doctors.id!!)
                .delete()
                .await()
            result.invoke(Results.success("Successfully Deleted"))
        }catch (e : Exception) {
            result.invoke(Results.failuire(e.message.toString()))
        }
    }

    override suspend fun getDoctorById(doctorID: String, result: (Results<Doctors?>) -> Unit) {
        result.invoke(Results.loading("Getting doctor by id"))
        firestore.collection(DOCTORS_COLLECTION)
            .document(doctorID)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(Results.success(it.toObject(Doctors::class.java)))
                }
                error?.let {
                    result.invoke(Results.failuire(it.message.toString()))
                }
            }
    }
    override suspend fun getDoctorsWithSchedules(result: (Results<List<DoctorWithSchedules>>) -> Unit) {
        try {
            result(Results.loading("Getting Doctors with schedules!"))
            val doctors = firestore.collection(DOCTORS_COLLECTION)
                .get()
                .await()
                .toObjects(Doctors::class.java)
            val schedules = firestore
                .collection(ScheduleRepositoryImpl.SCHEDULE_COLLECTION)
                .get()
                .await()
                .toObjects<Schedule>()
            val doctorWithSchedules = mutableListOf<DoctorWithSchedules>()
            doctors.forEach {
                val scheds = schedules.filter { it.doctorID == it.id }
                doctorWithSchedules.add(
                    DoctorWithSchedules(
                        doctors = it,
                        schedules = scheds
                    )
                )
            }
            result(Results.success(doctorWithSchedules))

        }catch (e : Exception) {
            result(Results.failuire(e.message.toString()))
        }
    }

    companion object {

    }
}