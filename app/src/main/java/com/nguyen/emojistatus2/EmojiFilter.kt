package com.nguyen.emojistatus2

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Toast

class EmojiFilter(val context: Context): InputFilter {
    companion object {
        const val TAG = "EmojiFilter"
    }

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
        if (source == null || source.isBlank()) {
            // if the added text is not valid, return empty string
            return ""
        } else {
            // if the added text is valid, return source
            Log.i(TAG, "Added text $source, it has length ${source.length} characters")
            // the 3 types that encompass all possible unicode characters
            val validTypes = listOf(Character.SURROGATE, Character.NON_SPACING_MARK, Character.OTHER_SYMBOL).map {
                it.toInt()
            }
            for (c in source) {
                val type = Character.getType(c)
                Log.i(TAG, "Character type $type")
                if (!validTypes.contains(type)) {
                    Toast.makeText(context, "Only emojis are allowed", Toast.LENGTH_SHORT).show()
                    return ""
                }
            }
            return source
        }
    }
}