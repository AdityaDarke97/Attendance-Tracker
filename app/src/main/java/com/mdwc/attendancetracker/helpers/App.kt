package com.mdwc.attendancetracker.helpers

import android.app.Application
import io.paperdb.Paper

//Initialize PaperDB
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
    }
}