package com.thequietz.travelog.ui.place.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.ui.place.model.PlaceDetailReview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaceReviewLayout(_context: Context, review: PlaceDetailReview) :
    ConstraintLayout(_context) {

    init {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.layout_place_review, this, true)
        val converter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)

        v.findViewById<TextView>(R.id.tv_item_place_review).text = review.author
        v.findViewById<TextView>(R.id.tv_item_place_review_time).text =
            converter.format(Date(review.time * 1000))
        v.findViewById<TextView>(R.id.tv_item_place_review_text).text = review.text
        v.findViewById<TextView>(R.id.tv_item_place_review_relative_time).text =
            review.timeDescription
        v.findViewById<TextView>(R.id.tv_item_place_review_rating).text = "⭐ ${review.rating}"

        val imageView = v.findViewById<ImageView>(R.id.iv_item_place_review_profile)
        Glide.with(imageView)
            .load(review.authorImage)
            .centerCrop()
            .override(imageView.measuredWidth, imageView.measuredHeight)
            .into(imageView)
    }
}
