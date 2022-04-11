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

class SmallItemListAdapter(

//    author,
//    bird_name,
//    bird_count,
//    photo

    private val context: Context,
    private val author: Array<String?>,
    private val bird_name: Array<String?>,
    private val bird_count: Array<String?>,
    private val photo: Array<String?>,
    private val id: Array<String?>,


    ) : BaseAdapter() {

    private lateinit var authors: TextView
    private lateinit var bird_names: TextView
    private lateinit var bird_counts: TextView
    private lateinit var photos: ImageView

    override fun getCount(): Int {
        return author.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val thisView = LayoutInflater.from(context).inflate(R.layout.card_small_fragment,p2,false)

        bird_names = thisView.findViewById(R.id.bird_name)
        bird_counts = thisView.findViewById(R.id.bird_number)
        photos = thisView.findViewById(R.id.photo_r)

        bird_names.text = bird_name[p0]?.replace("(^\\(|\\)$)", "")?.replace("\"", "")
        bird_counts.text = bird_count[p0]?.replace("(^\\(|\\)$)", "")

        val path = "https://birdithology.azurewebsites.net/".plus(photo[p0])

        Picasso.with(context)
            .load(path)
            .into(photos)

        return thisView
    }

}