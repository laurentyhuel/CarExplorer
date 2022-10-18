package com.lyh.carexplorer.feature.core.ui

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.lyh.carexplorer.feature.core.R
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {

        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            val authority = "com.lyh.carexplorer.fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )



        }
    }
}