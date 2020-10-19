/*
* Copyright 2020 MediaCentric
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.mediacentric.app.extension.file

import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract.Document
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider
import android.webkit.MimeTypeMap
import com.basicneedscorporation.basicneeds.R
import com.growinginfotech.businesshub.base.BITMAP_COMPRESS_QUALITY
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
* This class is used to provide data form local storage
*/
class LocalStorageProvider : DocumentsProvider() {

	@Throws(FileNotFoundException::class)
	override fun queryRoots(projection: Array<String>?): Cursor {
		val result = MatrixCursor(projection ?: DEFAULT_ROOT_PROJECTION)
		val homeDir = context?.getExternalFilesDir(null)
		val row = result.newRow()
		homeDir?.let { homeDirectory ->
			row.add(Root.COLUMN_ROOT_ID, homeDirectory.absolutePath)
			row.add(Root.COLUMN_DOCUMENT_ID, homeDirectory.absolutePath)
			row.add(Root.COLUMN_TITLE, context?.getString(R.string.app_name))
			row.add(Root.COLUMN_FLAGS, Root.FLAG_LOCAL_ONLY or Root.FLAG_SUPPORTS_CREATE)
			row.add(Root.COLUMN_ICON, R.mipmap.ic_launcher)
			// These columns are optional
			row.add(Root.COLUMN_AVAILABLE_BYTES, homeDirectory.freeSpace)
		}
		return result
	}

	@Throws(FileNotFoundException::class)
	override fun createDocument(
		parentDocumentId: String,
		mimeType: String,
		displayName: String
	): String? {
		val newFile = File(parentDocumentId, displayName)
		try {
			newFile.createNewFile()
			return newFile.absolutePath
		} catch (e: IOException) {
			//
		}

		return null
	}

	@Throws(FileNotFoundException::class)
	override fun openDocumentThumbnail(
		documentId: String,
		sizeHint: Point,
		signal: CancellationSignal
	): AssetFileDescriptor? {
		val options = BitmapFactory.Options()
		options.inJustDecodeBounds = true
		BitmapFactory.decodeFile(documentId, options)
		val targetHeight = 2 * sizeHint.y
		val targetWidth = 2 * sizeHint.x
		val height = options.outHeight
		val width = options.outWidth
		options.inSampleSize = 1
		if (height > targetHeight || width > targetWidth) {
			val halfHeight = height / 2
			val halfWidth = width / 2
			while (halfHeight / options.inSampleSize > targetHeight || halfWidth / options.inSampleSize > targetWidth) {
				options.inSampleSize *= 2
			}
		}
		options.inJustDecodeBounds = false
		val bitmap = BitmapFactory.decodeFile(documentId, options)
		val tempFile: File?
		var out: FileOutputStream? = null
		try {
			tempFile = File.createTempFile("thumbnail", null, context?.cacheDir)
			out = FileOutputStream(tempFile)
			bitmap.compress(Bitmap.CompressFormat.PNG, BITMAP_COMPRESS_QUALITY, out)
		} catch (e: IOException) {
			//
			return null
		} finally {
			out?.run {
				try {
					out.close()
				} catch (e: IOException) {
					//
				}
			}
		}
		return AssetFileDescriptor(
			ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY),
			0,
			AssetFileDescriptor.UNKNOWN_LENGTH
		)
	}

	@Throws(FileNotFoundException::class)
	override fun queryChildDocuments(
		parentDocumentId: String,
		projection: Array<String>?,
		sortOrder: String
	): Cursor {
		val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
		val parent = File(parentDocumentId)
		parent.listFiles()?.let { files ->
			for (file in files) {
				if (!file.name.startsWith(".")) {
					includeFile(result, file)
				}
			}
		}
		return result
	}

	@Throws(FileNotFoundException::class)
	override fun queryDocument(documentId: String, projection: Array<String>?): Cursor {
		val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
		includeFile(result, File(documentId))
		return result
	}

	@Throws(FileNotFoundException::class)
	private fun includeFile(result: MatrixCursor, file: File) {
		val row = result.newRow()
		// These columns are required
		row.add(Document.COLUMN_DOCUMENT_ID, file.absolutePath)
		row.add(Document.COLUMN_DISPLAY_NAME, file.name)
		val mimeType = getDocumentType(file.absolutePath)
		row.add(Document.COLUMN_MIME_TYPE, mimeType)
		var flags = if (file.canWrite()) {
			Document.FLAG_SUPPORTS_DELETE or Document.FLAG_SUPPORTS_WRITE
		} else {
			0
		}
		if (mimeType.startsWith("image/")) {
			flags = flags or Document.FLAG_SUPPORTS_THUMBNAIL
		}
		row.add(Document.COLUMN_FLAGS, flags)
		row.add(Document.COLUMN_SIZE, file.length())
		row.add(Document.COLUMN_LAST_MODIFIED, file.lastModified())
	}

	@Throws(FileNotFoundException::class)
	override fun getDocumentType(documentId: String): String {
		val file = File(documentId)
		if (file.isDirectory) {
			return Document.MIME_TYPE_DIR
		}
		// From FileProvider.getType(Uri)
		val lastDot = file.name.lastIndexOf('.')
		if (lastDot >= 0) {
			val extension = file.name.substring(lastDot + 1)
			val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
			if (mime != null) {
				return mime
			}
		}
		return "application/octet-stream"
	}

	@Throws(FileNotFoundException::class)
	override fun deleteDocument(documentId: String) {
		File(documentId).delete()
	}

	@Throws(FileNotFoundException::class)
	override fun openDocument(
		documentId: String,
		mode: String,
		signal: CancellationSignal?
	): ParcelFileDescriptor {
		val file = File(documentId)
		val isWrite = mode.indexOf('w') != -1
		return if (isWrite) {
			ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)
		} else {
			ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
		}
	}

	override fun onCreate(): Boolean {
		return true
	}

	companion object {

		val AUTHORITY = "${"BuildConfig.APPLICATION_ID"}.provider"

		private val DEFAULT_ROOT_PROJECTION = arrayOf(
			Root.COLUMN_ROOT_ID,
			Root.COLUMN_FLAGS,
			Root.COLUMN_TITLE,
			Root.COLUMN_DOCUMENT_ID,
			Root.COLUMN_ICON,
			Root.COLUMN_AVAILABLE_BYTES
		)

		private val DEFAULT_DOCUMENT_PROJECTION = arrayOf(
			Document.COLUMN_DOCUMENT_ID,
			Document.COLUMN_DISPLAY_NAME,
			Document.COLUMN_FLAGS,
			Document.COLUMN_MIME_TYPE,
			Document.COLUMN_SIZE,
			Document.COLUMN_LAST_MODIFIED
		)
	}
}
