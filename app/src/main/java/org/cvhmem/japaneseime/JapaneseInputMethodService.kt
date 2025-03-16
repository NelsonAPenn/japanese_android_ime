/*
 * Copyright 2024, 2025 Nelson Penn
 *
 * This file is part of Japanese Android IME.
 *
 * Japanese Android IME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Japanese Android IME is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Japanese Android IME. If not, see <https://www.gnu.org/licenses/>.
 */
package org.cvhmem.japaneseime

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.EditorInfo
import android.widget.Button

class JapaneseInputMethodService public constructor() : InputMethodService()
{
    private val composingText: StringBuilder = StringBuilder();
    private var anchorInfo: CursorAnchorInfo? = null;
    private val kanaConverter = KanaConverter()

    private fun resetState()
    {
        this.composingText.clear();
        this.anchorInfo = null;
    }


    override fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
        // TODO: never hit
        anchorInfo = cursorAnchorInfo;
    }

    override fun onFinishInput() {
        resetState()
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        resetState()
    }

    private fun onInput(value: String) {
        composingText.append(value)
        currentInputConnection.setComposingText(composingText, 1)
    }

    private fun tryModifyLastCharacter(convert: (Char) -> Char?)
    {
        if (composingText.isNotEmpty())
        {
            val old = composingText[composingText.length - 1];
            val newChar = convert(old);
            if (newChar != null)
            {
                composingText[composingText.length - 1] = newChar;
            }
            currentInputConnection.setComposingText(composingText, 1)
        }
    }

    private fun tryAddTen()
    {
        tryModifyLastCharacter { old: Char ->
            val res = kanaConverter.lookUp(old);
            if (res == null) { null }
            else {
                val (kanaClass, variant) = res;
                if (variant == Variant.Ten)
                {
                    kanaClass.getChar(Variant.Normal)
                }
                else {
                    kanaClass.getChar(Variant.Ten)
                }
            }
        }
    }
    private fun tryAddMaru()
    {
        tryModifyLastCharacter { old: Char ->
            val res = kanaConverter.lookUp(old);
            if (res == null) { null }
            else {
                val (kanaClass, variant) = res;
                if (variant == Variant.Maru)
                {
                    kanaClass.getChar(Variant.Normal)
                }
                else {
                    kanaClass.getChar(Variant.Maru)
                }
            }
        }
    }
    private fun tryConvertZen()
    {
        tryModifyLastCharacter { old: Char ->
            val res = kanaConverter.lookUp(old);
            if (res == null) { null }
            else {
                val (kanaClass, variant) = res;
                if (variant == Variant.Small)
                {
                    kanaClass.getChar(Variant.Normal)
                }
                else {
                    null
                }
            }
        }
    }
    private fun tryConvertHan()
    {
        tryModifyLastCharacter { old: Char ->
            val res = kanaConverter.lookUp(old);
            if (res == null) { null }
            else {
                val (kanaClass, variant) = res;
                if (variant == Variant.Normal)
                {
                    kanaClass.getChar(Variant.Small)
                }
                else {
                    null
                }
            }
        }
    }


    override fun onCreateInputView(): View {
        return layoutInflater.inflate(R.layout.input, null).apply {
            /*
             * TODO: this doesn't work well when the cursor is moved around.
             * New characters are always appended to the end of the composing text.
             * Characters are always deleted from before the composing text
             */
            val lambda = {value: String -> onInput(value)};
            val directionalKeyIds = arrayOf(
                R.id.button_a,
                R.id.button_k,
                R.id.button_s,
                R.id.button_t,
                R.id.button_n,
                R.id.button_h,
                R.id.button_m,
                R.id.button_y,
                R.id.button_r,
                R.id.button_w,
                R.id.button_punctuation,
            )
            for (id in directionalKeyIds)
            {
                findViewById<DirectionalKey>(id).onInput = lambda;
            }
            findViewById<DirectionalKey>(R.id.button_hanzen).onInput = {
                value: String ->
                when(value) {
                    "◌゙" -> {
                        tryAddTen()
                    }
                    "◌゚" -> {
                        tryAddMaru()
                    }
                    "半" -> {
                        tryConvertHan()
                    }
                    "全" -> {
                        tryConvertZen()
                    }
                }

            };
            findViewById<Button>(R.id.button_confirm).setOnClickListener {
                currentInputConnection.commitText(composingText, 1)
                composingText.clear()
            }
            findViewById<Button>(R.id.button_delete).setOnClickListener{
                currentInputConnection.deleteSurroundingText(1, 0)
            }

        }
    }

}