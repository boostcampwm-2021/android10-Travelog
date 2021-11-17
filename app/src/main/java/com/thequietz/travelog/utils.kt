package com.thequietz.travelog

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("app:setImage", "app:setBitmap")
fun loadImage(imageView: ImageView, url: String?, byteArray: ByteArray?) {
    if (url == "" || url == null) {
        byteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            println(bitmap)
            imageView.setImageBitmap(bitmap)
        }
    } else {
        Glide.with(imageView.context)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(imageView)

        imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
    }
}
@BindingAdapter("app:setBitmapImage")
fun loadBitmap(imageView: ImageView, byteArray: ByteArray?) {
    byteArray ?: return
    byteArray.let {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        println(bitmap)
        imageView.setImageBitmap(bitmap)
    }
}
fun getTodayDate(): String {
    val time = System.currentTimeMillis()
    val date = Date(time)
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
    return dateFormat.format(date)
}
