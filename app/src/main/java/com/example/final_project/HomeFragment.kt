package com.example.final_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 뷰 찾아오기
        recyclerView = view.findViewById(R.id.recyclerView1)
        fabAdd = view.findViewById(R.id.fabAdd)

        // AdapterView 자산 반영: 2줄짜리 그리드 레이아웃 매니저 설정 ( Gemini 답변 )
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // 플로팅 버튼 누르면 -> 글쓰기 액티비티로 이동 (activity_main2를 액티비티로 띄울 예정)
        fabAdd.setOnClickListener {
            // val intent = Intent(context, AddEditActivity::class.java)
            // startActivity(intent)
        }

        return view
    }
}