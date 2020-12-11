package com.xanthron.myeco

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val btn_statements = findViewById<Button>(R.id.btn_statements)
        val btn_expenses = findViewById<Button>(R.id.btn_expenses)
        val btn_income = findViewById<Button>(R.id.btn_income)
        val btn_loan = findViewById<Button>(R.id.btn_loan)
        val btn_float_add = findViewById<FloatingActionButton>(R.id.btn_float_add)
        val btn_float_delete = findViewById<FloatingActionButton>(R.id.btn_float_delete)

        btn_statements.setOnClickListener{
            val intent = Intent(this,StatementsActivity::class.java)
            startActivity(intent)
        }

        btn_expenses.setOnClickListener{
            val intent = Intent(this,ExpenseActivity::class.java)
            startActivity(intent)
        }

        btn_income.setOnClickListener{
            val intent = Intent(this,IncomeActivity::class.java)
            startActivity(intent)
        }

        btn_loan.setOnClickListener{
            val intent = Intent(this,LoanActivity::class.java)
            startActivity(intent)
        }

        btn_float_add.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.custom_dialog_add, null)
            builder.setView(dialogLayout)
            val et_dialog_amount  = dialogLayout.findViewById<EditText>(R.id.et_dialog_amount)

            val databaseHandler = DatabaseHandler(this)

            val test = databaseHandler.getBalance()

            if (test==0){

                builder.setTitle("Enter Main Balance")
                builder.setPositiveButton("OK") { dialogInterface, i ->

                    val amount = et_dialog_amount.text.toString()
                    val type = "Main"
                    val purpose = "Main"


                    if (et_dialog_amount.text.trim() != "" ) {
                        val status = databaseHandler.addMoney(ModelClass(amount,type,purpose))
                        if (status > -1){
                            Toast.makeText(applicationContext,"Added", Toast.LENGTH_SHORT).show()
                            et_dialog_amount.text.clear()

                        }else{
                            Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(applicationContext,"Can not be blank", Toast.LENGTH_SHORT).show()
                    }

                }
                builder.setNegativeButton("Cancel") { dialogInterface, i ->

                }

            }else{
                builder.setTitle("Balance")
                et_dialog_amount.setText(test.toString())
            }

            builder.show()

        }

        btn_float_delete.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater =  layoutInflater
            builder.setTitle("Clear all data")
            builder.setMessage("Please type 'CONFIRM' bellow ")
            val dialogLayout = inflater.inflate(R.layout.custom_dialog_delete,null)
            val et_dialog_confirm = dialogLayout.findViewById<EditText>(R.id.et_dialog_confirm)
            builder.setView(dialogLayout)
            builder.setPositiveButton("Confirm"){ dialogInterface, i->

                if(et_dialog_confirm.text.toString() == "CONFIRM"){
                    val databaseHandler = DatabaseHandler(this)
                    val status = databaseHandler.deleteData()
                }else{
                    Toast.makeText(this,"Please Enter Valid Confirmation",Toast.LENGTH_SHORT)
                }

            }
            builder.show()

        }
    }
}