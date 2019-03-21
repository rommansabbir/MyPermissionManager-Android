package com.rommansabbir.callmanager

import java.lang.Exception

interface MyPermissionManagerInterface {
    fun onMPMPermissionGranted()
    fun onMPMPermissionDenied()
    fun onMPMPermissionDeniedPermanently()
    fun onMPMException(error : Exception)
}