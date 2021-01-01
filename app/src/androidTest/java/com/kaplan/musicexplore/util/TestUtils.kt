package com.kaplan.musicexplore.util

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import com.kaplan.musicexplore.ui.favorite.data.Favorite
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import java.util.*

val testFavoriteA = Favorite(
    1440742904,
    Calendar.getInstance().time,
    "Dark Fantasy",
    "https://music.apple.com/us/album/dark-fantasy/1440742903?i=1440742904&uo=4",
    "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview114/v4/be/ed/06/beed0686-4a5d-5a63-eace-3ff0fec071e7/mzaf_8119476763649230409.plus.aac.p.m4a",
    "https://is3-ssl.mzstatic.com/image/thumb/Music113/v4/1d/8a/2e/1d8a2e21-254a-09d9-7489-0c120b8df1aa/source/100x100bb.jpg",
    280787
)

val testFavoriteB = Favorite(
    1440742905,
    Calendar.getInstance().time,
    "Gorgeous (feat. Kid Cudi & Raekwon)",
    "\"https://music.apple.com/us/album/gorgeous-feat-kid-cudi-raekwon/1440742903?i=1440742905&uo=4",
    "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview114/v4/59/ed/5e/59ed5e63-952e-9d90-4569-50b6e58cda4c/mzaf_2284291637558391061.plus.aac.p.m4a",
    "https://is3-ssl.mzstatic.com/image/thumb/Music113/v4/1d/8a/2e/1d8a2e21-254a-09d9-7489-0c120b8df1aa/source/100x100bb.jpg",
    357653,
)

/**
 * [Calendar] object used for tests.
 */
val testCalendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, 1998)
    set(Calendar.MONTH, Calendar.SEPTEMBER)
    set(Calendar.DAY_OF_MONTH, 4)
}

/**
 * Returns the content description for the navigation button view in the toolbar.
 */
fun getToolbarNavigationContentDescription(activity: Activity, toolbarId: Int) =
    activity.findViewById<Toolbar>(toolbarId).navigationContentDescription as String

/**
 * Simplify testing Intents with Chooser
 *
 * @param matcher the actual intent before wrapped by Chooser Intent
 */
fun chooser(matcher: Matcher<Intent>): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_CHOOSER),
    hasExtra(`is`(Intent.EXTRA_INTENT), matcher)
)