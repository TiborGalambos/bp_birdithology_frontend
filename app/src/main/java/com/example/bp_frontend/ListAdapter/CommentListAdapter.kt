package com.example.bp_frontend.ListAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bp_frontend.R
import com.example.bp_frontend.dataItems.Comment
import com.example.bp_frontend.dataItems.ObservationDataItem
import com.example.bp_frontend.dataItems.ObservationList

class CommentListAdapter(

    private val context: Context,
    private val comment: Array<out String>?,
    private val com_author: Array<out String>?,
//    private val com_author: List<Comment>,
//    private val comment: List<Comment>

) : BaseAdapter() {

    private lateinit var com_authors: TextView
    private lateinit var comments: TextView

    override fun getCount(): Int {
        return comment!!.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val thisView = LayoutInflater.from(context).inflate(R.layout.comment_fragment,p2,false)

        com_authors = thisView.findViewById(R.id.com_author)
        comments = thisView.findViewById(R.id.comment)


        com_authors.text = com_author!![this.count-1-p0].replace("(^\\(|\\)$)", "").replace("\"", "")
        comments.text = comment!![this.count-1-p0].replace("(^\\(|\\)$)", "").replace("\"", "")

        return thisView
    }
}