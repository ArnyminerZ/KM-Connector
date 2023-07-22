# KM Connector
A library that provides a way to have a common interface that connects different platforms.

Also has some utilities for sharing boilerplate code. Those are:
- [Preferences](#preferences)

# Utilities
## Preferences
There's a predefined `PreferencesProvider` object that provides a platform-specific approach for storing preferences.

The libraries used for each platform are:
- Desktop: [Java Preferences](https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/util/prefs/Preferences.html)
- Android: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

### Methods:
