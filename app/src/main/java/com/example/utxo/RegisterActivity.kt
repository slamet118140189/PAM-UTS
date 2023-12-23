package com.example.utxo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.utxo.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()
            val usernameApp = binding.edtUsernameApp.text.toString()
            val usernameGitHub = binding.edtUsernameGithub.text.toString()
            val nik = binding.edtNik.text.toString()

            // Validasi input
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailRegister.error = "Email Tidak Valid"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                binding.edtPasswordRegister.error = "Password Minimal 6 Karakter"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            // Mendaftarkan pengguna dan menyimpan data ke Firestore
            registerUser(email, password, usernameApp, usernameGitHub, nik)
        }
    }

    private fun registerUser(email: String, password: String, usernameApp: String, usernameGitHub: String, nik: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Pengguna berhasil didaftarkan, simpan data ke Firestore
                    val currentUser = auth.currentUser
                    val uid = currentUser?.uid

                    if (uid != null) {
                        // Menggunakan UID sebagai ID dokumen
                        val user = hashMapOf(
                            "email" to email,
                            "usernameApp" to usernameApp,
                            "usernameGitHub" to usernameGitHub,
                            "nik" to nik
                        )

                        db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                                // Pindah ke Activity selanjutnya
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Gagal menyimpan data ke Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                Log.e("FirestoreError", "Gagal menyimpan data ke Firestore", e)
                            }
                    } else {
                        // Gagal mendapatkan UID pengguna
                        Toast.makeText(this, "Gagal mendapatkan UID pengguna", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Gagal mendaftarkan pengguna, tampilkan pesan kesalahan
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
