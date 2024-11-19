package com.eutech.pawprints.shared.domain.di

import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.appointments.domain.AppointmentRepositoryImpl
import com.eutech.pawprints.auth.domain.AuthRepository
import com.eutech.pawprints.auth.domain.AuthRepositoryImpl
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.doctors.domain.DoctorRepositoryImpl
import com.eutech.pawprints.schedule.domain.ScheduleRepository
import com.eutech.pawprints.schedule.domain.ScheduleRepositoryImpl
import com.eutech.pawprints.products.domain.CategoryRepository
import com.eutech.pawprints.products.domain.CategoryRepositoryImpl
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.products.domain.ProductRepositoryImpl
import com.eutech.pawprints.shared.domain.repository.inbox.InboxRepository
import com.eutech.pawprints.shared.domain.repository.inbox.InboxRepositoryImpl
import com.eutech.pawprints.shared.domain.repository.pets.PetRepository
import com.eutech.pawprints.shared.domain.repository.pets.PetRepositoryImpl
import com.eutech.pawprints.shared.domain.repository.users.UsersRepository
import com.eutech.pawprints.shared.domain.repository.users.UsersRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ) : AuthRepository {
        return AuthRepositoryImpl(auth,firestore)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ) : ProductRepository {
        return ProductRepositoryImpl(storage,firestore)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        firestore: FirebaseFirestore,

    ) : CategoryRepository {
        return CategoryRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideDoctorRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
        ) : DoctorRepository {
        return DoctorRepositoryImpl(firestore,storage)
    }
    @Provides
    @Singleton
    fun proviceAppointmentRepository(
        firestore: FirebaseFirestore,

    ) : AppointmentRepository {
        return AppointmentRepositoryImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideScheduleRepository(
        firestore: FirebaseFirestore,
        ) : ScheduleRepository {
        return ScheduleRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ) : UsersRepository {
        return  UsersRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun providePetRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ) : PetRepository {
        return  PetRepositoryImpl(firestore,storage)
    }

    @Provides
    @Singleton
    fun provideInboxRepository(
        firestore: FirebaseFirestore,
    ) : InboxRepository {
        return  InboxRepositoryImpl(firestore)
    }
}