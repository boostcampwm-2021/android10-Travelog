package com.thequietz.travelog.ui.place.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.thequietz.travelog.R

class PlaceReviewDividerLayout(_context: Context) : LinearLayout(_context) {

    init {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_review_divider, this, true)
    }
}
