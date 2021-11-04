package com.thequietz.travelog

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:setImage")
fun loadImage(imageView: ImageView, url: String?) {
    url ?: return
    if (url != "") {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
        imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
    }
}
enum class FragmentType {
    DOSI, SPECIFIC
}
