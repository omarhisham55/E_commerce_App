package com.example.e_commerceapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.e_commerceapp.firebase.database.FirestoreController
import com.example.e_commerceapp.firebase.database.UserDB
import com.example.e_commerceapp.util.Constants.Companion.INTRODUCTION_SHARED_PREFS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseInstance() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirestoreController(firebaseFirestore: FirebaseFirestore): FirestoreController =
        FirestoreController(userDB = UserDB(firebaseFirestore))

    @Provides
    fun provideIntroductionSharedPrefs(application: Application): SharedPreferences =
        application.getSharedPreferences(INTRODUCTION_SHARED_PREFS, MODE_PRIVATE)
}