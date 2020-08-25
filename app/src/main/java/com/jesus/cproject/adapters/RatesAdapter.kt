package com.jesus.cproject.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jesus.cproject.R
import com.jesus.cproject.inflate
import com.jesus.cproject.models.Rate
import com.jesus.cproject.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_rates_item.view.*
import java.text.SimpleDateFormat


class RatesAdapter(private val items: List<Rate>) :
    RecyclerView.Adapter<RatesAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(rate: Rate) = with(itemView) {
            textViewRate.text = rate.text
            textViewStar.text = "${rate.rate}"
            textViewCalendar.text = SimpleDateFormat("dd MMM, yyyy").format(rate.createAt)
            Picasso.get().load(rate.profileImgUrl).resize(100, 100).centerCrop()
                .transform(CircleTransform()).into(imageViewProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.fragment_rates_item))

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
}