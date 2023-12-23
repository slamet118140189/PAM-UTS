package com.example.utxo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RickAdapter(var dataRick: List<ResultsItem?>, private val navController: NavController) :
    RecyclerView.Adapter<RickAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRick = view.findViewById<ImageView>(R.id.item_image_rick)
        val nameRick = view.findViewById<TextView>(R.id.item_name_rick)
        val detailButton: Button = view.findViewById(R.id.buttonDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (dataRick != null) {
            return dataRick.size
        }
        return 0
    }

    fun setData(newData: List<ResultsItem?>?) {
        dataRick = newData.orEmpty().filterNotNull()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nameRick.text = dataRick?.get(position)?.name
        val id = dataRick?.get(position)?.id

        Glide.with(holder.imgRick)
            .load(dataRick?.get(position)?.image)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgRick)

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "$id", Toast.LENGTH_SHORT).show()
        }

        holder.detailButton.setOnClickListener {
            val action = HomeFragmentDirections.actionNavSkillToNavSkilldetail2(id.toString())
            holder.itemView.findNavController().navigate(action)
        }

    }
}
