package com.example.bp_frontend.ListAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.bp_frontend.HomeActivity
import com.example.bp_frontend.R
import com.squareup.picasso.Picasso

class BirdCountListAdapter(

//    author,
//    bird_name,
//    bird_count,
//    photo

    private val context: Context,
    private val bird_name: Array<String?>,
    private val bird_count: Array<String?>,


    ) : BaseAdapter() {

    private lateinit var bird_names: TextView
    private lateinit var bird_counts: TextView

    override fun getCount(): Int {
        return bird_count.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val thisView = LayoutInflater.from(context).inflate(R.layout.card_small_without_photo_fragment,p2,false)

        bird_names = thisView.findViewById(R.id.bird_name)
        bird_counts = thisView.findViewById(R.id.bird_number)

        bird_names.text = bird_name[p0]?.replace("(^\\(|\\)$)", "")?.replace("\"", "")
        bird_counts.text = bird_count[p0]?.replace("(^\\(|\\)$)", "")



        return thisView
    }

}