package com.xanthron.myeco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.NullPointerException

class IncomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        val btn_add_income = findViewById<FloatingActionButton>(R.id.btn_add_income)
        val et_amount_income = findViewById<EditText>(R.id.et_amount_income)
        val et_purpose_income = findViewById<EditText>(R.id.et_purpose_income)

        ViewIncome()

        btn_add_income.setOnClickListener{

            val amount = et_amount_income.text.toString()
            val purpose = et_purpose_income.text.toString()
            val type = "INCOME"

            val databaseHandler = DatabaseHandler(this)

            if (amount.trim() != "" && purpose.trim() != "" ) {
                val status = databaseHandler.addIncome(ModelClass(amount,type,purpose))
                if (status > 0){
                    Toast.makeText(applicationContext,"Saved", Toast.LENGTH_SHORT).show()
                    et_amount_income.text.clear()
                    et_purpose_income.text.clear()

                }else{
                    Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(applicationContext,"Can not enter blank data", Toast.LENGTH_SHORT).show()
            }

            ViewIncome()

        }

    }
    private fun ViewIncome(){

        val listView = findViewById<ListView>(R.id.listViewIncome)

        val databaseHandler = DatabaseHandler(this)
        val data:List<ViewModelClass> = databaseHandler.viewIncome()

        val s = "INCOME"
        val sm = databaseHandler.findSum(IndViewModelClass(s))

        val dataArrayDate = Array(data.size){"null"}
        val dataArrayAmount = Array(data.size){"null"}
        val dataArrayType = Array(data.size){"null"}
        val dataArrayPurpose = Array(data.size){"null"}
        val dataArrayBalance = Array(data.size){"null"}

        for ((index, e) in data.withIndex()){
            dataArrayDate[index] = e.date
            dataArrayAmount[index] = e.amount
            dataArrayType[index] = e.type
            dataArrayPurpose[index] = e.purpose
            dataArrayBalance[index] = e.balance
        }

        try{
            val listAdapter = ViewListAdapter(this,dataArrayDate,dataArrayAmount,dataArrayType,dataArrayPurpose,dataArrayBalance)
            listView.adapter = listAdapter
        }catch (e: NullPointerException){

        }

        val tvSum = findViewById<TextView>(R.id.tv_sum_income)
        tvSum.text = sm.toString()

    }
}