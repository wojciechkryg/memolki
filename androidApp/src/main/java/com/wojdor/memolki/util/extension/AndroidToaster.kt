package com.wojdor.memolki.util.extension

import android.content.Context
import android.widget.Toast

class AndroidToaster(
    private val context: Context
) : Toaster {

    override fun show(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
