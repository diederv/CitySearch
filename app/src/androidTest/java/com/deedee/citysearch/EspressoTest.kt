package com.deedee.citysearch

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author diederick.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class EspressoTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSplashScreen() {
        Espresso.onView(
            ViewMatchers.withText(appContext.getString(R.string.percentage_header))
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
}