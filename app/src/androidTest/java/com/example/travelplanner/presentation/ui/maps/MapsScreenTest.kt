package com.example.travelplanner.presentation.ui.maps

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.travelplanner", appContext.packageName)
    }

    @Test
    fun backButton_triggersCallback() {
        var clicked = false
        composeRule.setContent {
            MapsScreen(lat = 48.8566, lon = 2.3522, name = "Paris", modifier = Modifier, onBackClick = { clicked = true })
        }
        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.runOnIdle {
            assertTrue("Back callback should have been invoked", clicked)
        }
    }
}