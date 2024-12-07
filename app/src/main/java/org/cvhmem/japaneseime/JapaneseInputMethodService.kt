package org.cvhmem.japaneseime

import android.inputmethodservice.InputMethodService
import android.view.View

class JapaneseInputMethodService public constructor() : InputMethodService()
{
    override fun onCreateInputView(): View {
        return layoutInflater.inflate(R.layout.input, null).apply {
//            if (this is MyKeyboardView) {
//                setOnKeyboardActionListener(this@MyInputMethod)
//                keyboard = latinKeyboard
//            }

        }
    }

}