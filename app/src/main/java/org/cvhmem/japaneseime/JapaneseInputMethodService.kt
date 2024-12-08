package org.cvhmem.japaneseime

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.CursorAnchorInfo
import android.widget.Button

class JapaneseInputMethodService public constructor() : InputMethodService()
{
    private val composingText: StringBuilder = StringBuilder();
    private var anchorInfo: CursorAnchorInfo? = null;

    override fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
        // TODO: never hit
        anchorInfo = cursorAnchorInfo;
    }

    override fun onCreateInputView(): View {
        return layoutInflater.inflate(R.layout.input, null).apply {
            /*
             * TODO: this doesn't work well when the cursor is moved around.
             * New characters are always appended to the end of the composing text.
             * Characters are always deleted from before the composing text
             */
            findViewById<Button>(R.id.button_a).setOnClickListener{
                composingText.append("あ")
                currentInputConnection.setComposingText(composingText, 1)
            };
            findViewById<Button>(R.id.button_confirm).setOnClickListener {
                currentInputConnection.commitText(composingText, 1)
                composingText.clear()
            }
            findViewById<Button>(R.id.button_delete).setOnClickListener{
                currentInputConnection.deleteSurroundingText(1, 0)
            }


//            if (this is MyKeyboardView) {
//                setOnKeyboardActionListener(this@MyInputMethod)
//                keyboard = latinKeyboard
//            }

        }
    }

}