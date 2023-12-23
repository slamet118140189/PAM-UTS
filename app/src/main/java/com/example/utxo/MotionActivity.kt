package com.example.utxo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.utxo.databinding.ActivityMotionBinding

class MotionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMotionBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("MyPreferences", Activity.MODE_PRIVATE)

        // Cek apakah pengguna telah melihat halaman ini
        val hasSeenMotion = sharedPreferences.getBoolean("hasSeenMotion", false)
        if (hasSeenMotion) {
            // Jika pengguna telah melihat halaman ini, lanjutkan ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.button.setOnClickListener {
            // Simpan bahwa pengguna telah melihat halaman ini
            val editor = sharedPreferences.edit()
            editor.putBoolean("hasSeenMotion", true)
            editor.apply()

            // Lanjutkan ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
