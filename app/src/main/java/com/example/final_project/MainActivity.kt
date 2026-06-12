package com.example.final_project

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private lateinit var datePicker1: DatePicker
    private lateinit var edtPlace: EditText
    private lateinit var edit: EditText
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var imagePreview: ImageView
    private lateinit var btnWrite: Button

    private var selectedDate: String = ""
    private val REQUEST_IMAGE_CAPTURE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 새로 합친 레이아웃 파일 지정 (아래 2번에서 새로 작성할 파일)
        //setContentView(R.layout.activity_main2)
        //setContentView(R.layout.activity_main_container)
        setContentView(R.layout.activity_main2)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this) // null 체크(?. 사용)


        /* if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment())
                .commit()
        } */

        // 지도 프래그먼트 초기화 및 연결
        /*val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            ?: (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment) */

    /*
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

     */

        // 위젯 ID 연결하기
        datePicker1 = findViewById(R.id.datePicker1)
        edtPlace = findViewById(R.id.edtPlace)
        edit = findViewById(R.id.edit)
        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGallery)
        imagePreview = findViewById(R.id.imagePreview)
        btnWrite = findViewById(R.id.btnWrite)

        // 날짜 설정
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)

        datePicker1.init(y, m, d) { _, year, monthOfYear, dayOfMonth ->
            selectedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
        }

        // 사진 찍기 버튼 클릭
        btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

        // 갤러리 버튼 클릭
        btnGallery.setOnClickListener {
            Toast.makeText(this, "갤러리 기능은 DB 연동 후 구현 예정입니다.", Toast.LENGTH_SHORT).show()
        }

        // 저장하기 버튼 클릭
        btnWrite.setOnClickListener {
            val place = edtPlace.text.toString().trim()
            val memo = edit.text.toString().trim()

            if (place.isEmpty()) {
                Toast.makeText(this, "여행지를 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // DBHelper 객체 생성 (기존 자산 활용)
            val dbHelper = DBHelper(this)
            val successRowId = dbHelper.insertTravel(place, selectedDate, memo)

            if (successRowId != -1L) {
                Toast.makeText(this, "『$place』 여행 일기가 저장되었습니다!", Toast.LENGTH_SHORT).show()
                edtPlace.setText("")
                edit.setText("")
                imagePreview.visibility = View.GONE
            } else {
                Toast.makeText(this, "저장에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 지도가 준비되었을 때 실행되는 함수
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val soonchunhyang = LatLng(36.7690, 126.9314) // 순천향대학교 기본위치

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(soonchunhyang, 16f))

        googleMap.addMarker(
            MarkerOptions()
                .position(soonchunhyang)
                .title("순천향대학교")
                .snippet("아산 캠퍼스")
        )


        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_normal -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.menu_satellite -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.menu_search -> {
                showSearchDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveToLocation(latLng: LatLng, title: String) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
    }

    private fun showSearchDialog() {
        val editText = EditText(this)
        editText.hint = "주소 또는 장소명 입력"

        AlertDialog.Builder(this)
            .setTitle("위치 검색")
            .setView(editText)
            .setPositiveButton("검색") { _, _ ->
                val keyword = editText.text.toString()
                searchLocation(keyword)
            }
            .setNeutralButton("취소", null)
            .show()
    }

    private fun searchLocation(keyword: String) {
        val geocoder = Geocoder(this, Locale.KOREA)
        try {
            val results = geocoder.getFromLocationName(keyword, 1)
            if (!results.isNullOrEmpty()) {
                val address = results[0]
                val latLng = LatLng(address.latitude, address.longitude)
                moveToLocation(latLng, keyword)
            } else {
                Toast.makeText(this, "검색 결과를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            if (imageBitmap != null) {
                imagePreview.visibility = View.VISIBLE
                imagePreview.setImageBitmap(imageBitmap)
            }
        }
    }
}