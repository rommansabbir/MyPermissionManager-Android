package com.rommansabbir.mypermissionmanager

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


object MyPermissionManager{
    private const val permissionRequestCode: Int = 1
    private lateinit var myPermissionManagerCallback: MyPermissionManagerCallback
    private var requiredPermission : String? = null
    private var countPermissionDenied : Int = 0
    /**
     * Setup MyPermissionManager
     * pass your permission
     * @param permission
     */

    fun requestPermission(context: Context, permission : String){
        myPermissionManagerCallback = context as MyPermissionManagerCallback
        /**
         * save permission reference in a variable
         */
        requiredPermission = permission
        /**
         * Check if permission already granted or not
         */
        if (!checkPermission(context)) {
            /**
             * if permission not granted
             * explain why you need permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, requiredPermission!!)) {
                showExplainerDialog(context)
            } else {
                /**
                 * or request permission
                 */
                requestSinglePermission(context)
            }
        } else {
            /**
             * Permission already granted, go ahead
             */
            myPermissionManagerCallback.onMPMPermissionGranted(permission)
        }
    }

    /**
     * Permission explain using AlertDialog
     */
    private fun showExplainerDialog(context: Context) {
        val dialog : AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setPositiveButton("Yes") {
                _, which ->
            requestSinglePermission(context)
        }
        dialog.setNegativeButton("No") {
                _, which -> myPermissionManagerCallback.onMPMPermissionDenied(requiredPermission!!)
        }
        val alert : AlertDialog = dialog.create()
        alert.setTitle(context.getString(R.string.permissionTitle))
        alert.setMessage(getAppName(context) + context.getString(R.string.permissionMessage))
        alert.show()

    }

    /**
     * This method is responsible for check self permission
     * if granted, return true
     * else, return false
     */
    private fun checkPermission(context: Context) : Boolean{
        return ContextCompat.checkSelfPermission(context, requiredPermission!!) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * This method handle your permission request
     */
    private fun requestSinglePermission(context: Context){
        ActivityCompat.requestPermissions(
            context as Activity,arrayOf(requiredPermission!!),permissionRequestCode
        )
    }

    /**
     * @param requestCode, request code
     * @param permissions, list of requested permissions
     * @param grantResults, granted list for requested permission
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionRequestCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0]== requiredPermission)) {
                    myPermissionManagerCallback.onMPMPermissionGranted(requiredPermission!!)
                }
                if(countPermissionDenied>=5){
                    myPermissionManagerCallback.onMPMPermissionDeniedPermanently(requiredPermission!!)
                    countPermissionDenied = 0
                }
                else {
                    myPermissionManagerCallback.onMPMPermissionDenied(requiredPermission!!)
                    if(countPermissionDenied<5){
                        countPermissionDenied++
                    }
                }
            }
            else -> {
                myPermissionManagerCallback.onMPMPermissionDeniedPermanently(requiredPermission!!)
            }
        }
    }

    /**
     * Request for permission again if not granted
     */
    fun requestPermissionAgain(context: Context){
        requestPermission(context, requiredPermission!!)
    }

    /**
     * This method is responsible to get your app name
     */
    private fun getAppName(context: Context) : String{
        return  context.applicationInfo.loadLabel(context.packageManager).toString()
    }

    /**
     * This method open apps settings
     * when user denied request, open apps setting
     * so that user can manually grant permission
     */
    fun openAppSetting(context: Context){
        val appSettingIntent = Intent()
        appSettingIntent.action = ACTION_APPLICATION_DETAILS_SETTINGS
        val appSettingsUri : Uri = Uri.fromParts("package", context.packageName , null)
        appSettingIntent.data = appSettingsUri
        context.startActivity(appSettingIntent)
    }
}