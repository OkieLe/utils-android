package io.github.boopited.droidutils.listeners

import android.text.Editable
import android.text.TextWatcher

interface SimpleTextWatcher: TextWatcher {
    override fun afterTextChanged(editor: Editable) {
        // Extender only need override necessary method
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Extender only need override necessary method
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Extender only need override necessary method
    }
}