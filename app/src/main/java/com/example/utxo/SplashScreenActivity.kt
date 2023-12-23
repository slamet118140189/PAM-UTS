package com.example.utxo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SplashScreenActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val ONBOARDING_KEY = booleanPreferencesKey("has_seen_onboarding")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Check if the user has seen onboarding
        runBlocking {
            val hasSeenOnboarding = dataStore.data.map { preferences ->
                preferences[ONBOARDING_KEY] ?: false
            }.first()

            val iconn = findViewById<ImageView>(R.id.iconn)
            iconn.alpha = 0f

            iconn.animate().setDuration(3000).alpha(1f).withEndAction {
                // Check login status with Firebase Authentication
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    // User is already logged in, navigate to MainActivity (home)
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    if (!hasSeenOnboarding) {
                        // User is not logged in and hasn't seen onboarding, navigate to OnboardingActivity
                        val intent = Intent(this@SplashScreenActivity, MotionActivity::class.java)
                        startActivity(intent)
                    } else {
                        // User is not logged in, navigate to LoginActivity
                        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}
