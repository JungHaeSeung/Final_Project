package com.example.final_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        // 3.  구글 맵 열기 인텐트 설정 (Project9_4 자산 반영)
        btnGoogleMap.setOnClickListener {
            // 서울 시청 기준 위도/경도 (원하는 추천 여행지 좌표로 변경 가능)
            val uri = Uri.parse("geo:37.5665,126.9780")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // 4. 공식 홈페이지(구글) 열기 인텐트 설정
        btnHomepage.setOnClickListener {
            val uri = Uri.parse("https://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // 5. 개발자에게 문의 문자 보내기 인텐트 설정
        btnSupportSMS.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                putExtra("sms_body", "안녕하세요, '나만의 여행 일지' 앱 관련 문의입니다. 현재는 이용하실 수 없습니다.")
                //임시 사용 중단으로 설정
                data = Uri.parse("smsto:" + Uri.encode("010-0000-0000")) // 본인 번호나 가상 번호
            }
            startActivity(intent)
        }

        return view
    }
}