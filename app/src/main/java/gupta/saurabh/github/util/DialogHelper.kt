package gupta.saurabh.github.util

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.text.Html
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import gupta.saurabh.github.databinding.CustomAlertDialogBinding

object DialogHelper {

    private var isShowing = false
    
    fun showDialog(
        message: String?,
        activity: Activity
    ) {

        if (isShowing) return

        val customAlertBinding = CustomAlertDialogBinding.inflate(activity.layoutInflater)

        val alertDialogBuilder = MaterialAlertDialogBuilder(activity)

            .setBackground(

                ColorDrawable(

                    activity.resources.getColor(

                        android.R.color.transparent,

                        null
                    )
                )
            )

            .setView(customAlertBinding.root).create()

        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setCanceledOnTouchOutside(false)

        customAlertBinding.tvMessage.text = Html.fromHtml(

            message,

            Html.FROM_HTML_OPTION_USE_CSS_COLORS
        )

        customAlertBinding.icClose.setOnClickListener {

            isShowing = false

            alertDialogBuilder.dismiss()
        }

        customAlertBinding.btnOk.setOnClickListener {

            isShowing = false

            alertDialogBuilder.dismiss()
        }

        alertDialogBuilder.show()

        isShowing = true
    }
}