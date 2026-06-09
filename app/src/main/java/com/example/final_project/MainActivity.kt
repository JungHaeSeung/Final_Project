package com.example.final_project

import android.content.Context

import android.os.Bundle

import android.widget.Button

import android.widget.DatePicker

import android.widget.EditText

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import java.io.IOException

import java.util.Calendar



class MainActivity : AppCompatActivity() {



    lateinit var datePicker1: DatePicker

    lateinit var edtDiary: EditText

    lateinit var btnWrite: Button

    lateinit var fileName: String



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



        datePicker1 = findViewById(R.id.datePicker1)

        edtDiary = findViewById(R.id.edit)

        btnWrite = findViewById(R.id.btnWrite)



        val cal = Calendar.getInstance()

        val y = cal.get(Calendar.YEAR)

        val m = cal.get(Calendar.MONTH)

        val d = cal.get(Calendar.DAY_OF_MONTH)



        fileName = "${y}_${m + 1}_$d.txt"

        displayDiary(fileName)



        datePicker1.init(y, m, d) { _, year, monthOfYear, dayOfMonth ->

            fileName = "${y}_${m + 1}_$d.txt"

            displayDiary(fileName)

        }



        btnWrite.setOnClickListener { saveDiary() }

    }



    private fun displayDiary(name: String) {
        val diText = readDiary(name)
        edtDiary.setText(diText)
        btnWrite.text = if (diText != null) "수정하기"
        else "새로 저장"
        edtDiary.hint = diText ?: "일기 없음"


        btnWrite.isEnabled = true

    }



// 여기 작성
    private fun saveDiary() {
        try {

            openFileOutput(fileName, Context.MODE_PRIVATE).use { it.write(edtDiary.text.toString().toByteArray())}

            Toast.makeText(this, "$fileName 이 저장됨", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {

            Toast.makeText(this, "파일 저장에 실패했습니다:  ${e.message}", Toast.LENGTH_SHORT).show()

        }

    }

    private fun readDiary(fName: String): String? {

        return try {

            openFileInput(fName).bufferedReader().use { it.readText().trim() }

        } catch (e: IOException) {

            null

        }

    }

}