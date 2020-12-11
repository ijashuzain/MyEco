package com.xanthron.myeco

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel


class StatementsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statements)

        val btn_statements_delete = findViewById<Button>(R.id.btn_statements_delete)
        val btn_statements_export = findViewById<Button>(R.id.btn_statements_export)

        viewList()


        btn_statements_delete.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete")
            builder.setMessage("Do you want to delete ?")
            builder.setPositiveButton("Confirm"){ dialogInterface, i->
                val databaseHandler = DatabaseHandler(this)
                val status = databaseHandler.deleteLastData()
                viewList()
            }
            builder.setNegativeButton("Cancel"){ dialogInterface, i->

            }

            builder.show()
        }
        btn_statements_export.setOnClickListener{
            exportData()
        }
    }

    private fun viewList() {
        val databaseHandler = DatabaseHandler(this)
        val data:List<ViewModelClass> = databaseHandler.viewStatements()

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
        val listAdapter = ListAdapter(
                this,
                dataArrayDate,
                dataArrayAmount,
                dataArrayType,
                dataArrayPurpose,
                dataArrayBalance
        )
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = listAdapter
    }

    private fun exportData() {
        val SAMPLE_DB_NAME = "EcoDB"
        val sd: File = Environment.getExternalStorageDirectory()
        val data: File = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath = "/data/com.xanthron.myeco/databases/$SAMPLE_DB_NAME"
        val backupDBPath = "/MyEco/$SAMPLE_DB_NAME"
        val currentDB = File(data, currentDBPath)
        val backupDB = File(sd, backupDBPath)
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}