package com.kaplan.musicexplore.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class PrivateApi

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CoroutineScropeIO
