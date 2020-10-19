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

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
* File Utils
*/
object FileUtils {

	private const val DEBUG = false // Set to true to enable logging

	private fun isLocalStorageDocument(uri: Uri): Boolean {
		return LocalStorageProvider.AUTHORITY == uri.authority
	}

	private fun isExternalStorageDocument(uri: Uri): Boolean {
		return "com.android.externalstorage.documents" == uri.authority
	}

	private fun isDownloadsDocument(uri: Uri): Boolean {
		return "com.android.providers.downloads.documents" == uri.authority
	}

	private fun isMediaDocument(uri: Uri): Boolean {
		return "com.android.providers.media.documents" == uri.authority
	}

	private fun isGooglePhotosUri(uri: Uri): Boolean {
		return "com.google.android.apps.photos.content" == uri.authority
	}

	private fun getDataColumn(
		context: Context,
		uri: Uri?,
		selection: String?,
		selectionArgs: Array<String>?
	): String? {
		var cursor: Cursor? = null
		val column = "_data"
		val projection = arrayOf(column)

		try {
			uri?.let {
				cursor =
					context.contentResolver.query(it, projection, selection, selectionArgs, null)
				if (cursor != null && cursor?.moveToFirst() == true) {
					val columnIndex = cursor?.getColumnIndexOrThrow(column)
					return columnIndex?.let { it1 -> cursor?.getString(it1) }
				}
			}
		} finally {
			cursor?.close()
		}
		return null
	}

	/**
	* This fun is used to get path of pics.
	* @param context Context
	* @param uri Uri
	* @return String?
	*/
	@SuppressWarnings("ReturnCount")
	fun getPath(context: Context, uri: Uri): String? {
		// DocumentProvider
		if (DocumentsContract.isDocumentUri(context, uri)) {
			// LocalStorageProvider
			if (isLocalStorageDocument(uri)) {
				// The path is the id
				return DocumentsContract.getDocumentId(uri)
			} else if (isExternalStorageDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				val type = split[0]

				if ("primary".equals(type, ignoreCase = true)) {
					return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
				}
			} else if (isDownloadsDocument(uri)) {

				val fileName: String? = getFilePath(context, uri)
				if (fileName != null) {
					return Environment.getExternalStorageDirectory()
						.toString() + "/Download/" + fileName
				}
				val id = DocumentsContract.getDocumentId(uri)
				if (id.isNotEmpty()) {
					if (id.startsWith("raw:")) {
						return id.replaceFirst("raw:", "")
					}
					try {
						var uri: Uri

						val contentUriPrefixesToTry =
							arrayOf(
								"content://downloads/public_downloads",
								"content://downloads/my_downloads",
								"content://downloads/all_downloads"
							)
						var path: String? = null
						main@for (contentUriPrefix in contentUriPrefixesToTry) {
							try {
								if (id.startsWith("msf:")) {
									val idMsf = id.split(":")
									uri = ContentUris.withAppendedId(
										Uri.parse(contentUriPrefix),
										java.lang.Long.valueOf(idMsf[1])
									)
								} else {
									uri = ContentUris.withAppendedId(
										Uri.parse(contentUriPrefix),
										java.lang.Long.valueOf(id)
									)
								}
								path =
									getDataColumn(context, uri, null, null)
								if (path != null) {
									break@main
								}
							} catch (e: NumberFormatException) {
								path = ""
								e.printStackTrace()
							} catch (e: IllegalArgumentException) {
								path = ""
								e.printStackTrace()
							}
						}
						return path
					} catch (e: NumberFormatException) {
						e.printStackTrace()
						return ""
					}
				}
			} else if (isMediaDocument(uri)) {
				val docId = DocumentsContract.getDocumentId(uri)
				val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				val type = split[0]

				var contentUri: Uri? = null
				when (type) {
					"image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					"video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
					"audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
				}

				val selection = "_id=?"
				val selectionArgs = arrayOf(split[1])

				return getDataColumn(context, contentUri, selection, selectionArgs)
			}
		} else if ("content".equals(uri.scheme, ignoreCase = true)) {
			return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
				context,
				uri,
				null,
				null
			)
		} else if ("file".equals(uri.scheme, ignoreCase = true)) {
			return uri.path
		}
		return null
	}

	val String.extension: String
		get() = this.substringAfterLast('.', "")

	fun getFilePath(context: Context, uri: Uri): String? {
		var cursor: Cursor? = null
		val projection = arrayOf(
			MediaStore.MediaColumns.DISPLAY_NAME
		)
		try {
			cursor = context.contentResolver.query(
				uri, projection, null, null,
				null
			)
			if (cursor != null && cursor.moveToFirst()) {
				val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
				return cursor.getString(index)
			}
		} finally {
			cursor?.close()
		}
		return null
	}
}
