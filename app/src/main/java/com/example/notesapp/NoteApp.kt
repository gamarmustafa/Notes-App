package com.example.notesapp

import android.app.Application

class NoteApp:Application() {
    val db by lazy {
        NoteDatabase.getInstance(this)
    }
}