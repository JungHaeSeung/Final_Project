package com.example.final_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class TravelAdapter(
    private val context: Context,
    private val travelList: ArrayList<TravelItem>
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    //각각의 격자 칸(Position)에 맞는 데이터를 위젯에 바인딩(매핑)
    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val item = travelList[position]

        holder.tvPlace.text = item.place
        holder.tvDate.text = item.date
        //기본 안드로이드 갤러리 아이콘으로 임시 세팅
        holder.ivPhoto.setImageResource(android.R.drawable.ic_menu_gallery)

        //항목 클릭 시 해당 기록의 상세 화면으로 이동할 수 있는 기초 리스너 설정
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "${item.place} 상세 보기 화면으로 이동 예정 (추후 구현)", Toast.LENGTH_SHORT).show()

        }
    }


    override fun getItemCount(): Int {
        return travelList.size
    }

    //ViewHolder 설정
    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
        val tvPlace: TextView = itemView.findViewById(R.id.tvPlace)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }
}