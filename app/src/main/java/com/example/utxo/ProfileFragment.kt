package com.example.utxo

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.utxo.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jahirfiquitiva.libs.textdrawable.TextDrawable
import jahirfiquitiva.libs.textdrawable.TextDrawable.Companion.buildRound


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val rootView = binding.root
        db = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser

        val uid = currentUser?.uid

        Log.d(TAG, "Read document with ID: $uid")

        val image = binding.imageView

        // Mendapatkan data pengguna dari Firestore berdasarkan UID
        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    // Memastikan bahwa dokumen ada sebelum memproses datanya
                    if (document.exists()) {
                        val usernameApp = document.getString("usernameApp")
                        val usernameGitHub = document.getString("usernameGitHub")
                        val nik = document.getString("nik")
                        val email = document.getString("email")

                        // Mendapatkan ID dokumen Firestore
                        val documentId = document.id

                        // Menampilkan ID dokumen di Toast atau di TextView sesuai kebutuhan
                        Toast.makeText(activity, "ID Dokumen: $documentId", Toast.LENGTH_SHORT).show()

                        val drawable: TextDrawable = buildRound(usernameApp?.let { it.firstOrNull()?.toString() } ?: "A", Color.RED)
                        image.setImageDrawable(drawable)

                        // Menampilkan informasi di TextView
                        binding.usernameTextView.text = "Username: $usernameApp"
                        binding.githubUsernameTextView.text = "GitHub: $usernameGitHub"
                        binding.nikTextView.text = "NIK: $nik"
                        binding.emailTextView.text = "Email: $email"
                    } else {
                        // Dokumen tidak ditemukan
                        Toast.makeText(activity, "Dokumen pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Gagal mengambil data pengguna dari Firestore
                    Toast.makeText(activity, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show()
                }
        }


        // Menambahkan onClickListener ke tombol logout
        binding.logoutButton.setOnClickListener {
            // Memanggil FirebaseAuth dan melakukan proses logout
            FirebaseAuth.getInstance().signOut()

            // Membuat Intent untuk berpindah ke Activity Login
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish() // Menutup Activity saat ini
        }
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}