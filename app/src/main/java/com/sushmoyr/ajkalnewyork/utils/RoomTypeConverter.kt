package com.sushmoyr.ajkalnewyork.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class RoomTypeConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray{
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream)
        return outStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}