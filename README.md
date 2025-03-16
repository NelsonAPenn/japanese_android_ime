# Japanese Android IME

A FOSS Japanese IME for Android.

The goal of this project is to become the de-facto standard for Japanese input on custom Android ROMs (and be a competitive option on stock Android).

Features and considerations:

- **Fully FOSS:** Released under a GPL v3.0 copyleft license.
- **Privacy-oriented:** Zero tracking, no phone home.
- **Actively maintained:** .
- **No upstream:** All the code for this project lies here. Every line of code is written with this IME in mind. There is no upstream to block (or provide) features.

This project was created because the Mozc Android IME is no longer maintained. Custom Android ROMs, past a certain Android version, have no options for Japanese input.

For a high-level description of where to look for what aspects of the application, check out the [Project Organization page](/project_organization.md).

## Current features

### 12-key input

This works today and can be used to input kana, albeit with some shortcomings.

The core swiping behavior is implemented, complete with haptic feedback.

![Screenshot of 12-key layout](/12_key.png)

Known issues:

- No extender character
- The handling of uncommitted text needs thorough revision. I remember this
  being imperfect in mozc-android as well.
- Background is still transparent, making for text overlapping possible.
- Hankaku / zenkaku options work, but don't feel the most natural in practice.
    - I wanted to avoid an "everything button," instead opting for more explicit decision between adding a ten and adding a maru, for example. I do still believe there is value in this, but I think the design could still be improved.
- Archaic characters still present as a way of entertaining myself, these should
  be removed as they will be useless to normal users.

## Roadmap

1. 12-key input
2. Conversion to kanji using free data sources.
3. Settings activity
4. Submission to F-droid.
5. Radical input
6. Romaji input
7. Non-essentials (emoji, etc.)

In order to provide an extremely stable, functional product, **quashing bugs and unintended behavior will be heavily prioritized over new features**.


## Installation

Currently, this app must be built with Android studio into an APK and manually installed on the target device.

In the future (once the core functionality is implemented), releases will be made to F-droid and likely the Play Store as well.

## Contributing

See [contributing guielines](/CONTRIBUTING.md).
