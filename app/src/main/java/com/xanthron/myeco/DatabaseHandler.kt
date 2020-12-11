package com.xanthron.myeco

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.lang.NullPointerException

class DatabaseHandler(var context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EcoDB"
        private val TABLE_NAME = "EcoTable"
        private val COL_ID="id"
        private val COL_DATE ="date"
        private val COL_AMOUNT = "amount"
        private val COL_TYPE = "type"
        private val COL_PURPOSE = "purpose"
        private val COL_BALANCE="balance"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE '" + TABLE_NAME +"'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT,'"
                + COL_DATE + "' DATETIME DEFAULT CURRENT_TIMESTAMP ,'"
                + COL_AMOUNT + "' TEXT,'"
                + COL_TYPE + "' TEXT,'"
                + COL_PURPOSE + "' TEXT,'"
                + COL_BALANCE + "' TEXT"+")")

        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addExpense(data: ModelClass):Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var balance = 0

        val bal = getBalance()

        if(bal>0){
            balance = bal-(data.amount).toInt()
            if(balance>=0){
                contentValues.put(COL_AMOUNT, data.amount)
                contentValues.put(COL_TYPE,data.type)
                contentValues.put(COL_PURPOSE,data.purpose)
                contentValues.put(COL_BALANCE,balance)

                val success = db.insert(TABLE_NAME,null,contentValues)
                db.close()

                return success
            }else{
                return 0
            }
        }
        return 0
    }

    @SuppressLint("Recycle")
    fun viewStatements():List<ViewModelClass>{

        val dataList:ArrayList<ViewModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY id DESC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: SQLiteException){
            return ArrayList()
        }
        var date: String
        var amount: String
        var type: String
        var purpose: String
        var balance: String


        if(cursor.moveToFirst()){
            do{
                date = cursor.getString(cursor.getColumnIndex("date"))
                amount = cursor.getString(cursor.getColumnIndex("amount"))
                type = cursor.getString(cursor.getColumnIndex("type"))
                purpose = cursor.getString(cursor.getColumnIndex("purpose"))
                balance = cursor.getString(cursor.getColumnIndex("balance"))

                val data = ViewModelClass(date = date, amount = amount, type = type, balance =balance, purpose = purpose)
                dataList.add(data)
            }while(cursor.moveToNext())
        }
        return dataList

    }

    fun getBalance():Int{

        val selectQuery = "SELECT balance FROM $TABLE_NAME WHERE id=(SELECT MAX(id) FROM $TABLE_NAME)"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: SQLiteException){
            return 0
        }

        var balance = 0
         if(cursor.moveToFirst()){
            do{

                val res = cursor.getString(cursor.getColumnIndex("balance")).toInt()
                if(res!=null){
                    balance = res
                }

            }while(cursor.moveToNext())
        }

         return balance
    }

    fun addMoney(data: ModelClass):Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_AMOUNT, data.amount)
        contentValues.put(COL_TYPE,data.type)
        contentValues.put(COL_PURPOSE,data.purpose)
        contentValues.put(COL_BALANCE,data.amount)

        val success = db.insert(TABLE_NAME,null,contentValues)
        db.close()

        return success

    }

    fun deleteData():Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME,null,null)

        db.close()
        return success
    }

    fun deleteLastData():Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME,"id=(SELECT MAX(id) FROM $TABLE_NAME)",null)

        db.close()
        return success
    }

    fun addIncome(data: ModelClass):Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var balance = 0

        val bal = getBalance()

        if(bal>=0){
            balance = bal + (data.amount).toInt()
            contentValues.put(COL_AMOUNT, data.amount)
            contentValues.put(COL_TYPE,data.type)
            contentValues.put(COL_PURPOSE,data.purpose)
            contentValues.put(COL_BALANCE,balance)

            val success = db.insert(TABLE_NAME,null,contentValues)
            db.close()

            return success
        }
        return 0
    }
    fun addLoanFrom(data: ModelClass):Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var balance = 0
        val bal = getBalance()

        if(bal>=0){
            balance = bal + (data.amount).toInt()
            contentValues.put(COL_AMOUNT, data.amount)
            contentValues.put(COL_TYPE,data.type)
            contentValues.put(COL_PURPOSE,data.purpose)
            contentValues.put(COL_BALANCE,balance)

            val success = db.insert(TABLE_NAME,null,contentValues)
            db.close()

            return success
        }

        return 0

    }
    fun addLoanTo(data: ModelClass):Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var balance = 0

        val bal = getBalance()

        if(bal>0){
            balance = bal - (data.amount).toInt()
            if(balance>=0){
                contentValues.put(COL_AMOUNT, data.amount)
                contentValues.put(COL_TYPE,data.type)
                contentValues.put(COL_PURPOSE,data.purpose)
                contentValues.put(COL_BALANCE,balance)

                val success = db.insert(TABLE_NAME,null,contentValues)
                db.close()

                return success
            }else{
                return 0
            }
        }
        return 0
    }
    fun viewExpenses():List<ViewModelClass>{

        val dataList:ArrayList<ViewModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE type='EXPENSE' ORDER BY id DESC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: SQLiteException){
            return ArrayList()
        }
        var date: String
        var amount: String
        var type: String
        var purpose: String
        var balance: String


        if(cursor.moveToFirst()){
            do{
                date = cursor.getString(cursor.getColumnIndex("date"))
                amount = cursor.getString(cursor.getColumnIndex("amount"))
                type = cursor.getString(cursor.getColumnIndex("type"))
                purpose = cursor.getString(cursor.getColumnIndex("purpose"))
                balance = cursor.getString(cursor.getColumnIndex("balance"))

                val data = ViewModelClass(date = date, amount = amount, type = type, balance =balance, purpose = purpose)
                dataList.add(data)
            }while(cursor.moveToNext())
        }
        return dataList

    }
    @SuppressLint("Recycle", "ShowToast")
    fun findSum(data: IndViewModelClass): Int {

        val db = this.readableDatabase
        val cursor: Cursor?
        var sm = 0

        Toast.makeText(context, data.s,Toast.LENGTH_SHORT)

        if (data.s == "EXPENSE") {
            val selectQuery = "SELECT SUM($COL_AMOUNT) FROM $TABLE_NAME WHERE $COL_TYPE='EXPENSE'"
            try {
                cursor = db.rawQuery(selectQuery, null)
            } catch (e: SQLiteException) {
                return 0
            }
            if (cursor.moveToFirst()) {
                do {
                    try {
                        sm = cursor.getString(cursor.getColumnIndex("SUM($COL_AMOUNT)")).toInt()
                    }catch (e: NullPointerException){
                        sm=0
                    }
                } while (cursor.moveToNext())
            }
        }else if (data.s == "INCOME") {
            val selectQuery = "SELECT SUM($COL_AMOUNT) FROM $TABLE_NAME WHERE $COL_TYPE='INCOME'"
            try {
                cursor = db.rawQuery(selectQuery, null)
            } catch (e: SQLiteException) {
                return 0
            }
            if (cursor.moveToFirst()) {
                do {

                    try {
                        sm = cursor.getString(cursor.getColumnIndex("SUM($COL_AMOUNT)")).toInt()
                    }catch (e: NullPointerException){
                        sm=0
                    }


                } while (cursor.moveToNext())
            }
        }
        return sm
    }
    fun viewIncome():List<ViewModelClass>{

        val dataList:ArrayList<ViewModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE type='INCOME' ORDER BY id DESC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: SQLiteException){
            return ArrayList()
        }
        var date: String
        var amount: String
        var type: String
        var purpose: String
        var balance: String


        if(cursor.moveToFirst()){
            do{
                try{
                    date = cursor.getString(cursor.getColumnIndex("date"))
                    amount = cursor.getString(cursor.getColumnIndex("amount"))
                    type = cursor.getString(cursor.getColumnIndex("type"))
                    purpose = cursor.getString(cursor.getColumnIndex("purpose"))
                    balance = cursor.getString(cursor.getColumnIndex("balance"))

                    val data = ViewModelClass(date = date, amount = amount, type = type, balance =balance, purpose = purpose)
                    dataList.add(data)
                }catch (e: NullPointerException){

                }
            }while(cursor.moveToNext())
        }
        return dataList

    }
    fun viewLoan():List<ViewModelClass>{

        val dataList:ArrayList<ViewModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE type='LOAN' ORDER BY id DESC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: SQLiteException){
            return ArrayList()
        }
        var date: String
        var amount: String
        var type: String
        var purpose: String
        var balance: String


        if(cursor.moveToFirst()){
            do{
                date = cursor.getString(cursor.getColumnIndex("date"))
                amount = cursor.getString(cursor.getColumnIndex("amount"))
                type = cursor.getString(cursor.getColumnIndex("type"))
                purpose = cursor.getString(cursor.getColumnIndex("purpose"))
                balance = cursor.getString(cursor.getColumnIndex("balance"))

                val data = ViewModelClass(date = date, amount = amount, type = type, balance =balance, purpose = purpose)
                dataList.add(data)
            }while(cursor.moveToNext())
        }
        return dataList

    }
}