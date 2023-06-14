package com.example.codingchallange.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.example.codingchallange.R

object CustomDialog {
    fun setLoadingDialog(context: Context, isCancelable: Boolean): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.loading_screen, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setDimAmount(0f);
        alertDialog.setCancelable(isCancelable)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return alertDialog
    }


}