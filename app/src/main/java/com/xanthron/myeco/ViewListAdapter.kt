package com.xanthron.myeco

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text

class ViewListAdapter(private val context: Activity, private val date: Array<String>, private val amount: Array<String>, private val type: Array<String>, private val purpose: Array<String>, private val balance: Array<String>)
    : ArrayAdapter<String>(context,R.layout.custom_list,date){


    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_dialog_list,null,true)

        val databaseHandler = DatabaseHandler(context)

        val tvDate = rowView.findViewById(R.id.tvDate) as TextView
        val tvAmount = rowView.findViewById(R.id.tvAmount) as TextView
        val tvType = rowView.findViewById(R.id.tvType) as TextView
        val tvPurpose = rowView.findViewById(R.id.tvPurpose) as TextView
        val tvBalance = rowView.findViewById(R.id.tvBalance) as TextView


        tvDate.text = date[position]
        tvAmount.text = amount[position]
        tvType.text = type[position]
        tvPurpose.text = purpose[position]
        tvBalance.text = balance[position]

        return rowView
    }

}
