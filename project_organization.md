## Project organization

- `DirectionalKey.kt`: A custom view which implements the swiping mechanic necessary for the 12-key keyboard.
- `JapaneseInputMethodService.kt`: Registration of the keyboard as an input method and handling of commit text.
- `Kana.kt`: Kotlin code to handle character "modifiers" (Unicode for han/zen, ten, maru, etc.).

I plan to implement kanji suggestion generation in native code (likely Rust, possibly C++ as it is the path of least resistance for native code in the Android world).