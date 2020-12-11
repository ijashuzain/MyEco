package com.xanthron.myeco

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.NullPointerException

class ExpenseActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        val btn_add_expense = findViewById<FloatingActionButton>(R.id.btn_add_expense)
        //val btn_view_expense = findViewById<Button>(R.id.btn_view_expense)

        val et_amount_expense = findViewById<EditText>(R.id.et_amount_expense)
        val et_purpose_expense = findViewById<EditText>(R.id.et_purpose_expense)

        ViewExpense()
        btn_add_expense.setOnClickListener{



            val amount = et_amount_expense.text.toString()
            val purpose = et_purpose_expense.text.toString()
            val type = "EXPENSE"

            val databaseHandler = DatabaseHandler(this)

            if (amount.trim() != "" && purpose.trim() != "" ) {
                val status = databaseHandler.addExpense(ModelClass(amount,type,purpose))
                if (status > 0){

                    Toast.makeText(applicationContext,"Saved", Toast.LENGTH_SHORT).show()
                    et_amount_expense.text.clear()
                    et_purpose_expense.text.clear()

                }else{
                    Toast.makeText(applicationContext,"Insufficient Balance", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(applicationContext,"Can not enter blank data", Toast.LENGTH_SHORT).show()
            }

            ViewExpense()

        }
    }
    private fun ViewExpense(){
//            val builder = AlertDialog.Builder(this)
//            val inflater =  layoutInflater
//            builder.setTitle("View")
//
//            val dialogLayout = inflater.inflate(R.layout.custom_dialog_view,null)
//            val listView = dialogLayout.findViewById<ListView>(R.id.dialog_list_view)
//            builder.setView(dialogLayout)

        val listView = findViewById<ListView>(R.id.listViewExpense)

        val databaseHandler = DatabaseHandler(this)
        val data:List<ViewModelClass> = databaseHandler.viewExpenses()

        val s = "EXPENSE"
        val sm = databaseHandler.findSum(IndViewModelClass(s))

        val dataArrayDate = Array(data.size){"null"}
        val dataArrayAmount = Array(data.size){"null"}
        val dataArrayType = Array(data.size){"null"}
        val dataArrayPurpose = Array(data.size){"null"}
        val dataArrayBalance = Array(data.size){"null"}

        var index = 0
        for(e in data){
            dataArrayDate[index] = e.date
            dataArrayAmount[index] = e.amount
            dataArrayType[index] = e.type
            dataArrayPurpose[index] = e.purpose
            dataArrayBalance[index] = e.balance
            index++
        }

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

        val tvSum = findViewById<TextView>(R.id.tv_sum)
        tvSum.text = sm.toString()
    }
}