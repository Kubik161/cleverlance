package cz.beesli.cleverlancetest.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import timber.log.Timber

object PhotoUtil {

    // decodes byteArray into Bitmap, width and height should be measures of target view
    fun decodeImage(input: ByteArray, width: Int, height: Int): Bitmap {

        //only measure input
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(input, 0, input.size, options)

        //set downsize coefficient
        options.inSampleSize = calculateInSampleSize(options, width, height)
        Timber.d("inSampleSize %d", options.inSampleSize)

        //decode for real
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(input, 0, input.size, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}