package com.android.test.testastropay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import com.android.test.testastropay.R
import java.util.*

class CitiesAdapter(context: Context, countryList: ArrayList<City>) :
    ArrayAdapter<City>(context, 0, countryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
                R.layout.weather_item_city, parent, false)

        val imageViewFlag = view.findViewById<ImageView>(R.id.ivCountry)
        val textViewName = view.findViewById<TextView>(R.id.tvCountry)
        val currentItem: City? = getItem(position)
        currentItem?.let {
            imageViewFlag.setImageResource(it.flagImage)
            textViewName.text = it.countryName
        }
        return view
    }
}

class City (val countryName: String, val flagImage: Int)
