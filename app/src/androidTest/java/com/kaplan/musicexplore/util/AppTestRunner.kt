package com.kaplan.musicexplore.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.kaplan.musicexplore.TestApp

class AppTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}
