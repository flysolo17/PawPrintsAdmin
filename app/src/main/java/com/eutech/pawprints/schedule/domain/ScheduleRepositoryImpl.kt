package com.eutech.pawprints.schedule.domain

import android.util.Log
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.doctors.domain.DOCTORS_COLLECTION
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.doctors.domain.DoctorRepositoryImpl
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.ScheduleWithDoctor
import com.eutech.pawprints.schedule.data.getEndDayOfMonth
import com.eutech.pawprints.schedule.data.getStartDayOfMonth
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.threeten.bp.LocalDate

class ScheduleRepositoryImpl(
    val firestore : FirebaseFirestore
) : ScheduleRepository {
    override suspend fun createSchedule(schedule: Schedule, result: (Results<String>) -> Unit) {
        if (schedule.id == null) {
            result.invoke(Results.failuire("Schedule is no id"))
            return
        }
        result.invoke(Results.loading("Creating schedule"))
        firestore.collection(SCHEDULE_COLLECTION)
            .document(schedule.id)
            .set(schedule)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Results.success("Successfully Created!"))
                } else {
                    result.invoke(Results.failuire("Cannot create schedule"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun getDoctorSchedules(result: (Results<List<Schedule>>) -> Unit) {
        firestore.collection(SCHEDULE_COLLECTION)
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Results.failuire(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }
                value?.let { snapshot ->
                    val schedules = snapshot.toObjects(Schedule::class.java)
                    result.invoke(Results.success(schedules))
                }
            }
    }


    override suspend fun deleteSchedule(id: String, result: (Results<String>) -> Unit) {
        firestore.collection(SCHEDULE_COLLECTION)
            .document(id)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result.invoke(Results.success("Successfully Deleted!"))
                } else {
                    result.invoke(Results.failuire("failed to delete schedule"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun assignDoctor(
        id: String,
        doctorID: String,
        result: (Results<String>) -> Unit
    ) {
        result.invoke(Results.loading("Assigning doctor"))
        firestore.collection(SCHEDULE_COLLECTION)
            .document(id)
            .update("doctorID",doctorID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(Results.success("Succesfully Assigned!"))
                } else {
                    result.invoke(Results.failuire("Failed assigning doctor"))
                }
            }.addOnFailureListener {
                result.invoke(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun getScheduleByMonth(
        localDate: LocalDate,
        result: (Results<List<ScheduleWithDoctor>>) -> Unit
    ) {
        result(Results.loading("Getting schedule"))
        try {
            val schedules = firestore.collection(SCHEDULE_COLLECTION)
                .whereGreaterThanOrEqualTo("date", localDate.getStartDayOfMonth())
                .whereLessThanOrEqualTo("date", localDate.getEndDayOfMonth())
                .get()
                .await()
            val scheduleList = schedules.toObjects(Schedule::class.java)
            Log.d("ScheduleQuery", "Number of schedules found: ${scheduleList.size}")
            Log.d("ScheduleQuery", "Query date ${localDate.getStartDayOfMonth()} - ${localDate.getEndDayOfMonth()}")
            // Print each schedule date for debugging
            scheduleList.forEach { sched ->
                Log.d("ScheduleQuery", "Schedule Date: ${sched.date}")
            }

            val schedulesWithDoctors = mutableListOf<ScheduleWithDoctor>()
            for (sched in scheduleList) {
                if (!sched.doctorID.isNullOrEmpty()) {
                    val doctor = firestore.collection(DOCTORS_COLLECTION)
                        .document(sched.doctorID)
                        .get()
                        .await()
                        .toObject(Doctors::class.java)
                    schedulesWithDoctors.add(ScheduleWithDoctor(sched, doctor))
                }
            }
            result(Results.success(schedulesWithDoctors))
        } catch (e: Exception) {
            Log.e("ScheduleQuery", "error",e)
            result(Results.failuire(e.message.toString()))
        }
    }
    companion object {
        const val SCHEDULE_COLLECTION = "schedules"
    }
}