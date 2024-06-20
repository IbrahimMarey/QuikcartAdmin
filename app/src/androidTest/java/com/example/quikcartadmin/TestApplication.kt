package com.example.quikcartadmin

import android.app.Application
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(TestApplication::class)
class TestApplication : Application() {
}