# Berlin Clock

![Screenshot](images/Screenshot_Berlin_Clock.jpg?raw=true "Screenshot")

This repo contains the Berlin Clock made with Jetpack Compose.

The Berlin Clock, also known as [Mengenlehreuhr](https://en.wikipedia.org/wiki/Mengenlehreuhr),
is a public clock that uses a series of 24 lights to display hours, minutes, and even/odd seconds.

> The 7 segment font is from [dafont](https://www.dafont.com/seven-segment.font)

## Building & Running

After cloning this repo, you can easily build the app with either Android Studio or IntelliJ IDEA.
Simply open the project with either IDE, sync gradle to get the dependencies,
then build and run the app on either a virtual or physical device.

The final result should look like the screenshot above.

Tests can be run by going to the VM test file, or by using the `app:test` gradle run configuration.

## For Reviewers

What was used used:
- Kotlin
- Jetpack Compose
- MVVM with ViewModel and StateFlows
- CI with GitHub Actions

Important files are in:
- /app/src/main/java/com/dev2012/berlinclock
- \app\src\test\java\com\dev2012\berlinclock
