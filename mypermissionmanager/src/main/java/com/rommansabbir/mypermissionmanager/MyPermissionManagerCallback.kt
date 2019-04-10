package com.rommansabbir.mypermissionmanager

import java.lang.Exception

interface MyPermissionManagerCallback {
    fun onMPMPermissionGranted()
    fun onMPMPermissionDenied()
    fun onMPMPermissionDeniedPermanently()
    fun onMPMException(error : Exception)
}