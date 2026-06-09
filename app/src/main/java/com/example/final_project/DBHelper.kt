package com.example.final_project

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TravelDB"
        private const val DATABASE_VERSION = 1

        // 컬럼 정의
        const val TABLE_NAME = "travel_notes"
        const val COL_ID = "id"
        const val COL_PLACE = "place"
        const val COL_DATE = "date"
        const val COL_MEMO = "memo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PLACE TEXT NOT NULL,
                $COL_DATE TEXT NOT NULL,
                $COL_MEMO TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun insertTravel(place: String, date: String, memo: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_PLACE, place)
            put(COL_DATE, date)
            put(COL_MEMO, memo)
        }

        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close() // 사용 후 닫기 필수
        return result
    }

    //
    fun getAllTravels(): android.database.Cursor {
        val db = this.readableDatabase
        // 최신 글이 맨 위에 오도록 id 역순(DESC) 정렬
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC"
        return db.rawQuery(query, null)
    }
}