package com.versaiilers.buildmart.presentation.ui.general

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.versaiilers.buildmart.databinding.CardviewAdvertisementBinding
import com.versaiilers.buildmart.domain.entity.advertisement.Advertisement
import java.text.NumberFormat
import java.util.Locale

class GeneralFragmentAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "GeneralFragmentAdapter"
    }

    private val advertisements = mutableListOf<Advertisement>()

    fun submitList(newAdvertisements: List<Advertisement>) {
        Log.d(TAG, "Advertisements received when calling submitList $newAdvertisements")
        advertisements.clear()
        advertisements.addAll(newAdvertisements)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardviewAdvertisementBinding.inflate(inflater, parent, false)
        return AdvertisementViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return advertisements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val advertisement = advertisements[position]
        if (holder is AdvertisementViewHolder) {
            holder.bind(advertisement)
        } else {
            throw Exception("$TAG: Incorrect ViewHolder type")
        }
    }

    inner class AdvertisementViewHolder(private val binding: CardviewAdvertisementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(advertisement: Advertisement) {
            val imageUrl = advertisement.srcLink

            //Glide with error handling and no caching
            Glide.with(binding.root.context)
                .asBitmap()
                .load(imageUrl)
//                .override(1920,1080)
//                .fitCenter()
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(R.drawable.placeholder_image) // Заглушка если изображения нет
                .into(binding.cardviewAdvertisementPropertyImage)


            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(advertisement.price)
            binding.cardviewAdvertisementPropertyPrice.text = formattedPrice
            binding.cardviewAdvertisementPropertyLocation.text=advertisement.location
            binding.cardviewAdvertisementPropertyTitle.text=advertisement.title
            binding.cardviewAdvertisementSaveButton.isEnabled=advertisement.saveFlag
            binding.cardviewAdvertisementTextviewRate.text= advertisement.rate.toString()
        }
    }
}











