## RatingSeekBar

![Sample GIF][sample-gif]

Custom view for Net Promoter Score (NPS) or other score-based rating input on Android. [Looking for iOS?][rating-slider-ios]

### Usage
```xml
<com.trafi.ratingseekbar.RatingSeekBar
    android:id="@+id/seek_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:rsb_max="10" />
```

```kotlin
seek_bar.setOnSeekBarChangeListener { view, rating ->
    // use rating
}
```
Or try out the [included sample][sample].

### Installation
```groovy
// top-level build.gradle
allprojects {
    repositories {
        // ..
        maven { url 'https://jitpack.io' }
    }
}

// module build.gradle
dependencies {
    // ..
    implementation 'com.trafi:rating-seek-bar:0.4-alpha'
}
```

### Advanced usage
```xml
<com.trafi.ratingseekbar.RatingSeekBar
    android:id="@+id/seek_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:rsb_max="10"
    app:rsb_active_color="#1F757B"
    app:rsb_active_text_color="#FFFFFF"
    app:rsb_inactive_color="#FAF8FA"
    app:rsb_inactive_text_color="#110711"
    app:rsb_text_size="14sp" />
```


[rating-slider-ios]: https://github.com/trafi/RatingSlider
[sample]: https://github.com/trafi/rating-seek-bar/blob/master/sample/src/main/java/com/trafi/ratingseekbar/sample/DemoActivity.java
[sample-gif]: img/rating-seek-bar.gif
