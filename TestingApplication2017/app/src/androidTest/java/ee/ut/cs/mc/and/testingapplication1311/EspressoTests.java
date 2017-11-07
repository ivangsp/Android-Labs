package ee.ut.cs.mc.and.testingapplication1311;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class EspressoTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<MainActivity>( MainActivity.class  ){
                @Override
                protected void beforeActivityLaunched() {
                    clearSharedPrefs();
                    super.beforeActivityLaunched();
                }
            };


    @Test
    public void addingNewItem(){
        onView(withText("Milk")).check(doesNotExist());

        onView(withId(R.id.editText_name))
                .perform(typeText("Milk"), closeSoftKeyboard());

        onView(withText("Add")).perform(click());

        onView(withText("Milk")).check(matches(isDisplayed()));

    }


    @Test
    public void addingSameItemIncreasesQty() throws InterruptedException {

        //Add 1 milk
        onView(withId(R.id.editText_name))
                .perform(typeText("Milk"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());

        //Check qty (check that neighbouring element contains "1")
        onView(allOf(withText("Milk"), hasSibling(withText("1"))))
                .check(matches(isDisplayed()));

        //add 2nd milk
        onView(withId(R.id.editText_name))
                .perform(typeText("Milk"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());


        onView(allOf(withText("Milk"), hasSibling(withText("2"))))
                .check(matches(isDisplayed()));

    }


    @Test
    public void addingItemsUpdatesTotalCost() throws InterruptedException {
        onView(withId(R.id.tv_sum)).check(matches(withText("Total Cost: $ 0")));
        //Add 1 milk
        onView(withId(R.id.editText_name))
                .perform(typeText("Milk"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.tv_sum)).check(matches(withText("Total Cost: $ 1")));
    }


    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().
                getSharedPreferences(MainActivity.APP_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
