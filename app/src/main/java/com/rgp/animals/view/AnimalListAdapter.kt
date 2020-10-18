package com.rgp.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rgp.animals.R
import com.rgp.animals.databinding.FragmentDetailBinding
import com.rgp.animals.databinding.ItemAnimalBinding
import com.rgp.animals.model.Animal
import com.rgp.animals.util.getProgressDrawable
import com.rgp.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter(private val animalList: ArrayList<Animal>):
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {
    private lateinit var dataBinding: ItemAnimalBinding

    class AnimalViewHolder(var view: ItemAnimalBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_animal, parent,
            false)
        return AnimalViewHolder(dataBinding)

    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        dataBinding.animal = animalList[position]

        holder.view.animalLayout.setOnClickListener {
            val action =
                ListFragmentDirections.actionDetail(animalList[position])
            Navigation.findNavController(holder.view.root).navigate(action)
        }
    }

    override fun getItemCount() = animalList.size

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

}