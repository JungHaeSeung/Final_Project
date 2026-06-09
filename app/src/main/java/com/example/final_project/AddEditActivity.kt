package com.example.final_project

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddEditActivity : AppCompatActivity() {

    // 1. XML 위젯들과 매핑할 변수 선언
    private lateinit var datePicker1: DatePicker
    private lateinit var edtPlace: EditText
    private lateinit var edit: EditText
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var imagePreview: ImageView
    private lateinit var btnWrite: Button

    private var selectedDate: String = ""

    // 카메라 촬영 결과를 받아오기 위한 구분 태그 (Project9_4 자산 활용)
    private val REQUEST_IMAGE_CAPTURE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 아까 최종 세팅한 activity_main2.xml 연결
        setContentView(R.layout.activity_main2)

        // 2. 위젯 ID 연결하기
        datePicker1 = findViewById(R.id.datePicker1)
        edtPlace = findViewById(R.id.edtPlace)
        edit = findViewById(R.id.edit)
        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGallery)
        imagePreview = findViewById(R.id.imagePreview)
        btnWrite = findViewById(R.id.btnWrite)

        // 3. 날짜 설정 (기존 캘린더 일기장 자산)
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        // 초기 날짜 세팅 (형식: yyyy-MM-dd)
        selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)

        datePicker1.init(y, m, d) { _, year, monthOfYear, dayOfMonth ->
            selectedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
        }

        // 4. [기능 융합] 사진 찍기 버튼 클릭 리스너 (Project9_4 암시적 인텐트)
        btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // 결과를 받아와야 하므로 startActivity가 아닌 startActivityForResult 사용
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

        // 5. 갤러리 버튼 클릭 리스너 (추후 구현 예정, 일단 토스트)
        btnGallery.setOnClickListener {
            Toast.makeText(this, "갤러리 기능은 DB 연동 후 구현 예정입니다.", Toast.LENGTH_SHORT).show()
        }

        // 6. 저장하기 버튼 클릭 리스너
        btnWrite.setOnClickListener {
            val place = edtPlace.text.toString().trim()
            val memo = edit.text.toString().trim()

            if (place.isEmpty()) {
                Toast.makeText(this, "여행지를 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 우선 화면을 닫고 데이터가 잘 넘어가는지 토스트로 검증 (추후 SQLite DB에 insert)
            Toast.makeText(this, "[$place] $selectedDate 기록 완료 (DB 저장 예정)", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // 7. 카메라로 찍은 사진 결과 처리하기 (Project9_4 확장)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 찍은 사진을 비트맵 데이터로 가져옴
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            if (imageBitmap != null) {
                // 숨겨져 있던 이미지뷰를 보이게 하고 사진을 꽂아줌
                imagePreview.visibility = View.VISIBILITY_VISIBLE
                imagePreview.setImageBitmap(imageBitmap)
            }
        }
    }
}