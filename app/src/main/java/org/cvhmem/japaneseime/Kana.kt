/*
 * Copyright 2024 Nelson Penn
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

private enum class KanaClassId
{
    A,
    I,
    U,
    E,
    O,
    Ka,
    Ki,
    Ku,
    Ke,
    Ko,
    Sa,
    Si,
    Su,
    Se,
    So,
    Ta,
    Ti,
    Tu,
    Te,
    To,
    Na,
    Ni,
    Nu,
    Ne,
    No,
    Ha,
    Hi,
    Hu,
    He,
    Ho,
    Ma,
    Mi,
    Mu,
    Me,
    Mo,
    Ya,
    Yi,
    Yu,
    Ra,
    Ri,
    Ru,
    Re,
    Ro,
    Wa,
    Wi,
    We,
    Wo,
    Nn,
}

enum class Variant
{
    Normal,
    Small,
    Ten,
    Maru
}

data class KanaClass(
    val baseChar: Char,
    val supportsSmall: Boolean,
    val supportsTen: Boolean,
    val supportsMaru: Boolean,
) {
    fun getChar(variant: Variant): Char?
    {
        if (supportsSmall && variant == Variant.Small)
        {
            return baseChar - 1
        }
        if (variant == Variant.Normal)
        {
            return baseChar
        }
        if (supportsTen && variant == Variant.Ten)
        {
            return baseChar + 1
        }
        if (supportsMaru && variant == Variant.Maru)
        {
            return baseChar + 2
        }
        return null
    }
}

private data class KanaClassBoundary(
    val unicodeStart: Char,
    val kanaClass: KanaClassId,
)

private val KANA_CLASSES: Array<KanaClass> = arrayOf(
    KanaClass(baseChar='あ', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='い', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='う', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='え', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='お', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='か', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='き', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='く', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='け', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='こ', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='さ', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='し', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='す', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='せ', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='そ', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='た', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='ち', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='つ', supportsSmall=true, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='て', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='と', supportsSmall=false, supportsTen=true, supportsMaru=false),
    KanaClass(baseChar='な', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='に', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ぬ', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ね', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='の', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='は', supportsSmall=false, supportsTen=true, supportsMaru=true),
    KanaClass(baseChar='ひ', supportsSmall=false, supportsTen=true, supportsMaru=true),
    KanaClass(baseChar='ふ', supportsSmall=false, supportsTen=true, supportsMaru=true),
    KanaClass(baseChar='へ', supportsSmall=false, supportsTen=true, supportsMaru=true),
    KanaClass(baseChar='ほ', supportsSmall=false, supportsTen=true, supportsMaru=true),
    KanaClass(baseChar='ま', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='み', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='む', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='め', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='も', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='や', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ゆ', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='よ', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ら', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='り', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='る', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='れ', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ろ', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='わ', supportsSmall=true, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ゐ', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ゑ', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='を', supportsSmall=false, supportsTen=false, supportsMaru=false),
    KanaClass(baseChar='ん', supportsSmall=false, supportsTen=false, supportsMaru=false),
)

/*
 * Ranges of characters specifically handled by this module
 */
private val HIRAGANA_RANGE: CharRange = 'あ'..'ん';
private val KATAKANA_RANGE: CharRange = 'ァ'..'ン';

class KanaConverter() {
    private val kanaClassBoundaries: ArrayList<KanaClassBoundary> =
        ArrayList(KANA_CLASSES.count() + 1)

    init {
        for ((kanaClass, kanaClassId) in KANA_CLASSES.zip(KanaClassId.entries.toTypedArray())) {
            var classStart = kanaClass.baseChar
            if (kanaClass.supportsSmall) {
                classStart -= 1
            }
            kanaClassBoundaries.add(KanaClassBoundary(unicodeStart=classStart, kanaClass=kanaClassId));
        }
    }

    fun lookUp(char: Char): Pair<KanaClass, Variant>?
    {
        if (!HIRAGANA_RANGE.contains(char)) { return null; }
        // TODO: binary search
        var kanaClass: KanaClass? = null;
        for (boundary in kanaClassBoundaries)
        {
            if (boundary.unicodeStart > char) {
                break
            }
            kanaClass = KANA_CLASSES[boundary.kanaClass.ordinal];
        }
        if (kanaClass == null) { return null }


        val variant = when (char)
        {
            kanaClass.baseChar - 1 -> Variant.Small
            kanaClass.baseChar -> Variant.Normal
            kanaClass.baseChar + 1 -> Variant.Ten
            kanaClass.baseChar + 2 -> Variant.Maru
            else -> return null
        };


        return kanaClass to variant
    }
}
