package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Country

class CountryAdapter(
    context: Context,
    resouceId: Int,
    textviewId: Int,
    list: List<Country>,
) : ArrayAdapter<Country>(context, resouceId, textviewId, list) {
    var flater: LayoutInflater? = null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowview(convertView, position)
    }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return rowview(convertView, position)
    }

    private fun rowview(convertView: View?, position: Int): View {
        val rowItem: Country? = getItem(position)
        val view: View
        val vh: viewHolder
        if (convertView == null) {
            flater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = flater!!.inflate(R.layout.item_type, null, false)
            vh = viewHolder()
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as viewHolder
        }
        vh.tvTitle?.text = rowItem?.name

        return view
    }

    private inner class viewHolder {
        var tvTitle: TextView? = null
    }
}