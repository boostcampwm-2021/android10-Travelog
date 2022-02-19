package com.thequietz.travelog.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.thequietz.travelog.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("app:setImage")
fun loadImage(imageView: ImageView, url: String?) {
    url ?: return
    if (url == "empty") {
        Glide.with(imageView.context)
            .asBitmap()
            .load(R.drawable.animation_loading)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(imageView)
        imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
    } else {
        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(imageView)

        imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
    }
}

@BindingAdapter("app:setCenterImage")
fun loadCenterImage(imageView: ImageView, url: String?) {
    url ?: return
    if (url == "empty") {
        Glide.with(imageView.context)
            .asBitmap()
            .load(R.drawable.animation_loading)
            .into(imageView)
    } else {
        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            // .diskCacheStrategy(DiskCacheStrategy.NONE)
            // .skipMemoryCache(true)
            .into(imageView)
    }
}

fun getTodayDate(): String {
    val time = System.currentTimeMillis()
    val date = Date(time)
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
    return dateFormat.format(date)
}
fun makePdf(
    recyclerview: RecyclerView,
    mainLayout: ConstraintLayout,
    fileName: String,
    context: Context
) {
    val recyclerViewBitmap = getBitmapFromView(recyclerview)
    try {
        val fOut = FileOutputStream(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/$fileName.pdf"
        )

        var maxPageNum: Int = (recyclerViewBitmap.height / recyclerview.height)
        if (recyclerViewBitmap.height % recyclerview.height != 0) {
            maxPageNum++
        }
        var currentHeight = 0
        val document = PdfDocument()
        for (pageNum in 1..maxPageNum) {
            val pageInfo =
                PdfDocument.PageInfo.Builder(recyclerview.width, recyclerview.height, pageNum)
                    .create()
            val page = document.startPage(pageInfo)
            recyclerViewBitmap.prepareToDraw()
            page.canvas.drawBitmap(
                recyclerViewBitmap,
                Rect(0, currentHeight, recyclerview.width, currentHeight + recyclerview.height),
                Rect(0, 0, recyclerview.width, recyclerview.height),
                null
            )
            currentHeight += recyclerview.height
            document.finishPage(page)
            document.writeTo(fOut)
            if (pageNum == maxPageNum) {
                document.close()
            }
        }
        val snackbar = Snackbar.make(
            mainLayout,
            "PDF 생성 완료",
            Snackbar.LENGTH_LONG
        )
            .setAction("공유하기") {
                val intent = share2Pdf(fileName, context)
                context.startActivity(Intent.createChooser(intent, "파일 공유"))
            }
        snackbar.show()
    } catch (e: IOException) {
        println("something wrong..")
        e.printStackTrace()
    }
}

private fun getBitmapFromView(tempView: View): Bitmap {
    var view = tempView
    if (view.visibility != View.VISIBLE) {
        view = getNextView(view)
    }
    val returnedBitmap: Bitmap =
        when (view) {
            is ScrollView -> {
                Bitmap.createBitmap(
                    (view as ViewGroup).getChildAt(0).width,
                    (view as ViewGroup).getChildAt(0).height,
                    Bitmap.Config.ARGB_8888
                )
            }
            is RecyclerView -> {
                view.measure(
                    View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                Bitmap.createBitmap(
                    view.getWidth(),
                    view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888
                )
            }
            else -> {
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            }
        }

    val canvas = Canvas(returnedBitmap)
    val bgDrawable = view.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    view.draw(canvas)
    return returnedBitmap
}

private fun getNextView(tempView: View): View {
    var view = tempView
    if (view.parent != null && view.parent is ViewGroup) {
        val group = view.parent as ViewGroup
        var child: View
        var getNext = false
        for (i in 0 until group.childCount) {
            child = group.getChildAt(i)
            if (getNext) {
                if (child.visibility == View.VISIBLE) {
                    view = child
                }
            }
            if (child.id == view.id) {
                getNext = true
            }
        }
    }
    return view
}

fun share2Pdf(fileName: String, context: Context): Intent {
    val pdfFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/$fileName.pdf")
    val contentUrl =
        FileProvider.getUriForFile(context, context.packageName + ".fileprovider", pdfFile)
    return Intent().apply {
        type = "application/*"
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUrl)
    }
}
fun makeSnackBar(layout: ConstraintLayout, text: String, time: Long = 2000) {
    val snack = Snackbar.make(
        layout,
        "$text",
        Snackbar.LENGTH_SHORT
    )
    snack.show()
    Handler(Looper.getMainLooper()).postDelayed({
        snack.dismiss()
    }, time)
}
fun makeToast(context: Context, text: String, time: Long = 2000) {
    val toast = Toast.makeText(context, "$text", Toast.LENGTH_SHORT)
    toast.show()
    Handler(Looper.getMainLooper()).postDelayed({
        toast.cancel()
    }, time)
}
