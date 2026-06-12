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


    private lateinit var dbHelper: DBHelper
    private var travelList = ArrayList<TravelItem>()
    private lateinit var adapter: TravelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView1)
        fabAdd = view.findViewById(R.id.fabAdd)

        dbHelper = DBHelper(requireContext())

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        fabAdd.setOnClickListener {
            val intent = Intent(context, AddEditActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadTravelData()
    }

    private fun loadTravelData() {
        travelList.clear()

        val cursor = dbHelper.getAllTravels()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(DBHelper.COL_ID)
                val placeIndex = cursor.getColumnIndex(DBHelper.COL_PLACE)
                val dateIndex = cursor.getColumnIndex(DBHelper.COL_DATE)
                val memoIndex = cursor.getColumnIndex(DBHelper.COL_MEMO)

                if (idIndex != -1 && placeIndex != -1 && dateIndex != -1 && memoIndex != -1) {
                    val id = cursor.getInt(idIndex)
                    val place = cursor.getString(placeIndex)
                    val date = cursor.getString(dateIndex)
                    val memo = cursor.getString(memoIndex)

                    travelList.add(TravelItem(id, place, date, memo))
                }
            } while (cursor.moveToNext())
            cursor.close()
        }

        //데이터셋이 완성되었으므로 어댑터를 새로 만들거나 갱신하여 리사이클러뷰에 바인딩
        adapter = TravelAdapter(requireContext(), travelList)
        recyclerView.adapter = adapter
    }
}
