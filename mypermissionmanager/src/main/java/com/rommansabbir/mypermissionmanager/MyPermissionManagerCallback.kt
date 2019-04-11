package com.rommansabbir.mypermissionmanager

import java.lang.Exception

interface MyPermissionManagerCallback {
    fun onMPMPermissionGranted(requestedPermission : String)
    fun onMPMPermissionDenied(requestedPermission : String)
    fun onMPMPermissionDeniedPermanently(requestedPermission : String)
    fun onMPMException(error : Exception)
}