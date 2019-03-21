package com.rommansabbir.callmanager

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.rommansabbir.mypermissionmanager.R
import java.lang.Exception


class MyPermissionManager(private val context: Context){
    private val permissionRequestCode: Int = 1
    private val myPermissionManagerInterface: MyPermissionManagerInterface = context as MyPermissionManagerInterface
    private var requiredPermission : String? = null

    /**
     * Setup MyPermissionManager
     * pass your permission
     * @param permission
     */
    fun setupMyPermissionManager(permission : String){
        /**
         * save permission reference in a variable
         */
        requiredPermission = permission
        /**
         * Check if permission already granted or not
         */
        if (!checkPermission()) {
            /**
             * if permission not granted
             * explain why you need permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, requiredPermission!!)) {
                showExplainerDialog()
            } else {
                /**
                 * or request permission
                 */
                requestPermission()
            }
        } else {
            /**
             * Permission already granted, go ahead
             */
            myPermissionManagerInterface.onMPMPermissionGranted()
        }
    }

    /**
     * Permission explain using AlertDialog
     */
    private fun showExplainerDialog() {
        val dialog : AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setPositiveButton("Yes") {
                _, which -> requestPermission()
        }
        dialog.setNegativeButton("No") {
                _, which -> myPermissionManagerInterface.onMPMPermissionDeniedPermanently()
        }
        val alert : AlertDialog = dialog.create()
        alert.setTitle(context.getString(R.string.permissionTitle))
        alert.setMessage(getAppName(context) + context.getString(R.string.permissionMessage))
        alert.show()

    }

    /**
     * This method is responsible for Start a new phone call
     * @param phoneNumber, pass your phone number
     */
    fun startTelephony(phoneNumber: String){
        try {
            val telephonyIntent = Intent(Intent.ACTION_CALL)
            telephonyIntent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(telephonyIntent)
        }
        catch (e : Exception){
            myPermissionManagerInterface.onMPMException(e)
        }
    }

    /**
     * This method is responsible for check self permission
     * if granted, return true
     * else, return false
     */
    private fun checkPermission() : Boolean{
        val isGranted = ContextCompat.checkSelfPermission(context, requiredPermission!!) == PackageManager.PERMISSION_GRANTED
        return isGranted
    }

    /**
     * This method handle your permission request
     */
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            context as Activity,arrayOf(requiredPermission!!),permissionRequestCode
        )
    }

    /**
     * @param requestCode, request code
     * @param permissions, list of requested permissions
     * @param grantResults, granted list for requested permission
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            permissionRequestCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    myPermissionManagerInterface.onMPMPermissionGranted()
                } else {
                    myPermissionManagerInterface.onMPMPermissionDenied()
                }
                myPermissionManagerInterface.onMPMPermissionDeniedPermanently()
            }
            else -> {
            }
        }
    }

    /**
     * This method is responsible to get your app name
     */
    private fun getAppName(context: Context) : String{
        return  context.getString(R.string.app_name)
    }

    /**
     * This method open apps settings
     * when user denied request, open apps setting
     * so that user can manually grant permission
     */
    fun openAppSetting(){
        val appSettingIntent = Intent()
        appSettingIntent.action = ACTION_APPLICATION_DETAILS_SETTINGS
        val appSettingsUri : Uri = Uri.fromParts("package", context.packageName , null)
        appSettingIntent.data = appSettingsUri
        context.startActivity(appSettingIntent)
    }
}