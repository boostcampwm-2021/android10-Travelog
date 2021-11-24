package com.thequietz.travelog

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("app:setImage")
fun loadImage(imageView: ImageView, url: String?) {
    url ?: return
    Glide.with(imageView.context)
        .asBitmap()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(imageView)

    imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
}
fun getTodayDate(): String {
    val time = System.currentTimeMillis()
    val date = Date(time)
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
    return dateFormat.format(date)
}
fun addToByteList(list: MutableList<ByteArrayOutputStream>, view: View) {
    view2Bitmap(view)?.let {
        val bm = Bitmap.createBitmap(it)
        val canvas = Canvas(bm)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)

        val bytes = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        list.add(bytes)
    }
}

fun view2Bitmap(view: View): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        view.width,
        view.height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun byteListToPdf(list: MutableList<ByteArrayOutputStream>, fileName: String) {
    val document = Document()
    PdfWriter.getInstance(
        document,
        FileOutputStream(
            Environment.getExternalStorageDirectory().toString() + "/$fileName.pdf"
        )
    )
    document.open()
    if (list.size != 0) {
        list.forEachIndexed { ind, it ->
            val file = File(Environment.getExternalStorageDirectory(), "./temp.jpg")
            try {
                file.createNewFile()
                val fo = FileOutputStream(file)
                fo.write(it.toByteArray())

                val image = Image.getInstance(file.toString())
                val scaler =
                    (((document.pageSize.width - document.leftMargin()) - document.rightMargin()) / image.width) * 80

                image.scalePercent(scaler)
                image.alignment = (Image.ALIGN_CENTER or Image.ALIGN_TOP)
                document.add(image)
                if (ind == (list.size - 1)) {
                    document.close()
                }
                file.delete()
            } catch (e: IOException) {
                println("something wrong")
            }
        }
    }
}

fun share2Pdf(fileName: String, context: Context): Intent {
    val pdfFile = File(Environment.getExternalStorageDirectory(), "/$fileName.pdf")
    val contentUrl = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", pdfFile)
    return Intent().apply {
        type = "application/*"
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUrl)
    }
}
