package com.xanthron.myeco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.NullPointerException

class LoanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)

        ViewLoan()

        val LoanType = resources.getStringArray(R.array.LoanType)
        val btn_add_loan = findViewById<FloatingActionButton>(R.id.btn_add_loan)
        val et_amount_loan = findViewById<EditText>(R.id.et_amount_loan)
        val et_name_loan = findViewById<EditText>(R.id.et_name_loan)
        val spn_loanType = findViewById<Spinner>(R.id.spinner_loan)

        val spinner_loan = findViewById<Spinner>(R.id.spinner_loan)

        if (spinner_loan != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, LoanType)
            spinner_loan.adapter = adapter
        }
        btn_add_loan.setOnClickListener{
            val amount = et_amount_loan.text.toString()
            val name = et_name_loan.text.toString()
            val loantype = spn_loanType.selectedItem.toString()
            val purpose = ("$loantype $name")
            val type = "LOAN"
            val status = -1

            val databaseHandler = DatabaseHandler(this)

            if (amount.trim() != "" && purpose.trim() != "" ) {
                if(loantype=="From"){
                    val status = databaseHandler.addLoanFrom(ModelClass(amount,type,purpose))
                    if (status > 0){
                        Toast.makeText(applicationContext,"Saved", Toast.LENGTH_SHORT).show()
                        et_amount_loan.text.clear()
                        et_name_loan.text.clear()

                    }else{
                        Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                        et_amount_loan.text.clear()
                        et_name_loan.text.clear()
                    }
                }else{
                    val status = databaseHandler.addLoanTo(ModelClass(amount,type,purpose))
                    if (status > 0){
                        Toast.makeText(applicationContext,"Saved", Toast.LENGTH_SHORT).show()
                        et_amount_loan.text.clear()
                        et_name_loan.text.clear()

                    }else{
                        Toast.makeText(applicationContext,"Insufficient Balance", Toast.LENGTH_SHORT).show()
                        et_amount_loan.text.clear()
                        et_name_loan.text.clear()
                    }
                }

            }else{
                Toast.makeText(applicationContext,"Can not enter blank data", Toast.LENGTH_SHORT).show()
            }
            ViewLoan()
        }

    }
    private fun ViewLoan(){
        val listView = findViewById<ListView>(R.id.listViewLoan)

        val databaseHandler = DatabaseHandler(this)
        val data:List<ViewModelClass> = databaseHandler.viewLoan()

        val s = "LOAN"
//        val sm = databaseHandler.findSum(IndViewModelClass(s))

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

//        val tvSum = findViewById<TextView>(R.id.tv_sum)
//        tvSum.text = sm.toString()
    }
}