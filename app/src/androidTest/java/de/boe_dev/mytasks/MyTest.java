package de.boe_dev.mytasks;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;


import de.boe_dev.mytasks.ui.MainActivity;

/**
 * Created by benny on 06.06.16.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyTest extends InstrumentationTestCase {

    Context mMockContext;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mMockContext = new RenamingDelegatingContext(
                InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");
    }

    @Test
    public void UiTest() {
        onView(withId(R.id.login_edit_text_mail)).perform(typeText("rwar"));
        onView(withId(R.id.login_with_google_button)).perform(click());
    }

}
