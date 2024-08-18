package com.example.viddeoplataformaexamen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomAdapter(private val context: Context, private val plataformas: List<Map<String, Any>>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return plataformas.size
    }

    override fun getItem(position: Int): Any {
        return plataformas[position]
    }

    override fun getItemId(position: Int): Long {
        return (getItem(position) as Map<String, Any>)["id"] as Long
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.item_platform, parent, false)

        val platform = getItem(position) as Map<String, Any>
        val nameTextView: TextView = view.findViewById(R.id.platformNameTextView)
        val priceTextView: TextView = view.findViewById(R.id.platformPriceTextView)
        val qualityTextView: TextView = view.findViewById(R.id.platformQualityTextView)

        nameTextView.text = platform["name"] as String
        priceTextView.text = "Price: ${platform["price"]}"
        qualityTextView.text = "Quality: ${platform["videoQuality"]}"

        return view
    }
}
