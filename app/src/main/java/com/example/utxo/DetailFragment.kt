package com.example.utxo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.utxo.databinding.FragmentDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var characterId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        characterId = arguments?.getString(EXTRA_NAME, "-1") ?: "-1"

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Gunakan characterId untuk keperluan Anda di sini
        //binding.skilldetaill.text = characterId

        // Panggil fungsi API untuk mendapatkan detail karakter
        getCharacterDetail(characterId)
    }

    private fun getCharacterDetail(characterId: String) {
        val apiService = ApiConfig.getService()
        val call = apiService.getCharacterDetail(characterId)

        call.enqueue(object : Callback<ResultsItem> {
            override fun onResponse(call: Call<ResultsItem>, response: Response<ResultsItem>) {
                if (response.isSuccessful) {
                    val characterDetail = response.body()
                    characterDetail?.let {
                        // Tampilkan informasi karakter ke pengguna
                        displayCharacterDetail(it)
                    }
                } else {
                    // Tangani kesalahan respons API
                    Log.e("DetailFragment", "Failed to get character detail. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResultsItem>, t: Throwable) {
                // Tangani kesalahan panggilan API
                Log.e("DetailFragment", "API call failed", t)
            }
        })
    }

    private fun displayCharacterDetail(detailResponse: ResultsItem) {
        // Menampilkan gambar menggunakan Glide
        Glide.with(requireContext())
            .load(detailResponse.image)
            .placeholder(R.drawable.ic_launcher_background) // Ganti dengan placeholder image atau sesuaikan
            .error(R.drawable.ic_launcher_background) // Ganti dengan image untuk menampilkan kesalahan jika gambar tidak dapat diambil
            .into(binding.imageView)

        binding.textName.text = "Name: ${detailResponse.name}"
        binding.textStatus.text = "Status: ${detailResponse.status}"
        binding.textSpecies.text = "Species: ${detailResponse.species}"
        binding.textGender.text = "Gender: ${detailResponse.gender}"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}