package com.example.utxo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rickAdapter: RickAdapter
    private var characterList: List<ResultsItem?> = emptyList() // Data karakter asli
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        navController = NavHostFragment.findNavController(this)

        recyclerView = view.findViewById(R.id.rv_character)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // Initialize adapter with an empty list
        rickAdapter = RickAdapter(characterList, navController)
        recyclerView.adapter = rickAdapter

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.queryHint = "Search Characters" // Atur hint untuk SearchView

        // Tambahkan listener pencarian
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Tidak perlu melakukan apa-apa saat pengguna menekan "Submit"
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle perubahan teks dalam SearchView
                filterCharacters(newText)
                return true
            }
        })

        // Load data using Retrofit, similar to your existing code
        loadRickData()

        return view
    }

    private fun loadRickData() {
        ApiConfig.getService().getRick().enqueue(object : Callback<ResponseRick> {
            override fun onResponse(call: Call<ResponseRick>, response: Response<ResponseRick>) {
                if (response.isSuccessful) {
                    val responseRick = response.body()
                    val dataRick = responseRick?.results
                    characterList = dataRick.orEmpty() // Simpan data karakter ke dalam list
                    rickAdapter.setData(characterList) // Update adapter dengan data karakter
                }
            }

            override fun onFailure(call: Call<ResponseRick>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterCharacters(query: String?) {
        // Filter data karakter berdasarkan query
        val filteredCharacters = characterList.filter { character ->
            character?.name?.contains(query.orEmpty(), ignoreCase = true) == true
        }

        rickAdapter.setData(filteredCharacters) // Update adapter dengan data yang telah difilter
    }
}
