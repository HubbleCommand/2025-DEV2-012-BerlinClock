# Berlin Clock

![Berlin Clock](Kata_BerlinClock.png?raw=true "Berlin Clock")

The Berlin Clock, also known as [Mengenlehreuhr](https://en.wikipedia.org/wiki/Mengenlehreuhr)
is a public clock that uses a series of 24 lights to display hours, minutes, and even/odd seconds.

## Building & Running

This project can easily be run with either Android Studio or IntelliJ IDEA.
Simply open the project with either IDE, sync gradle,
and run the app on either a virtual or physical device.

Tests can be run by going to the VM test file, or by using the `app:test` gradle configurations.

## For Reviewers

What was used used:
- Kotlin
- Jetpack Compose
- MVVM with ViewModel and StateFlows
- DI with koin
- CI with GitHub Actions

Important files are in:
- /app/src/main/java/com/dev2012/tictactoe
- \app\src\test\java\com\dev2012\tictactoe
