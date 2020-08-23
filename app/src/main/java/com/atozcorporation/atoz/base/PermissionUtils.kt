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
package com.atozcorporation.atoz.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.growinginfotech.businesshub.base.PERMISSION_REQUEST_CODE

/**
* Created by Simform Solutions on 08 May 2020.
**/

// Code for upload Profile Picture
fun checkPermissionForUploadImage(cotext: Context): Boolean {
	return ContextCompat.checkSelfPermission(
		cotext,
		Manifest.permission.READ_EXTERNAL_STORAGE
	) == PackageManager.PERMISSION_GRANTED &&
			ContextCompat.checkSelfPermission(
				cotext,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			) == PackageManager.PERMISSION_GRANTED &&
			ContextCompat.checkSelfPermission(
				cotext,
				Manifest.permission.CAMERA
			) == PackageManager.PERMISSION_GRANTED
}

fun checkStoragePermission(cotext: Context): Boolean {
	return ContextCompat.checkSelfPermission(
		cotext,
		Manifest.permission.READ_EXTERNAL_STORAGE
	) == PackageManager.PERMISSION_GRANTED &&
			ContextCompat.checkSelfPermission(
				cotext,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			) == PackageManager.PERMISSION_GRANTED
}

fun askPermissionForUploadImage(activity: Activity) {

	ActivityCompat.requestPermissions(
		activity,
		arrayOf(
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
		),
		PERMISSION_REQUEST_CODE
	)
}

fun askStoragePermission(activity: Activity) {

	ActivityCompat.requestPermissions(
		activity,
		arrayOf(
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
		),
		PERMISSION_REQUEST_CODE
	)
}
