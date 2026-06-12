package com.example.final_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. fragment_info.xml 레이아웃을 화면에 연결
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        // 2. XML 내부의 버튼 위젯들 찾아오기
        val btnGoogleMap = view.findViewById<Button>(R.id.btnGoogleMap)
        val btnHomepage = view.findViewById<Button>(R.id.btnHomepage)
        val btnSupportSMS = view.findViewById<Button>(R.id.btnSupportSMS)

        btnGoogleMap.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        // 공식 홈페이지(구글) 열기 인텐트 설정
        btnHomepage.setOnClickListener {
            val uri = Uri.parse("https://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // 개발자에게 문의 문자 보내기 인텐트 설정 (미정)
        btnSupportSMS.setOnClickListener {
            Toast.makeText(context, "구현 준비 중입니다. 이용에 불편함을 드려 죄송합니다.", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}