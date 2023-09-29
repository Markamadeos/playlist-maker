package com.guap.vkr.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.sharing.domain.ExternalNavigator
import com.guap.vkr.playlistmaker.sharing.domain.model.EmailData
import java.lang.Exception

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(sharedLink: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "text/plan"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.android_developer_url))
        }

        context.startActivity(shareIntent)
    }

    override fun openLink(openLink: String) {
        val licenseIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(context.getString(R.string.license_url))
        }

        context.startActivity(licenseIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val feedbackIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.developer_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.letter_topic))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.letter_body))
        }
        try {
            context.startActivity(feedbackIntent)
        } catch (e: Exception) {
            Toast.makeText(
                    context,
                    context.getString(R.string.mail_app_not_found),
                    Toast.LENGTH_LONG
            ).show()
        }

    }
}