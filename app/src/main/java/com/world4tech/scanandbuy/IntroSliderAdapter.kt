package com.world4tech.scanandbuy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.world4tech.scanandbuy.util.IntroSlider

class IntroSliderAdapter(private val introSlides:List<IntroSlider>):RecyclerView.Adapter<IntroSliderAdapter.IntroSliderViewHolder>() {
    inner class IntroSliderViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val titleText = view.findViewById<TextView>(R.id.heading_view)
        private val imageIcon = view.findViewById<ImageView>(R.id.Slide_image)
        fun bind(introSlider : IntroSlider){
            titleText.text  = introSlider.title
            imageIcon.setImageResource(introSlider.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        return IntroSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.slider_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

}