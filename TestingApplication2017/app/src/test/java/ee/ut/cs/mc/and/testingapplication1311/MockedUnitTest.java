package ee.ut.cs.mc.and.testingapplication1311;


import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockedUnitTest {

    static final String json = "[{'amount':1,'name':'Milk','price':34},{'amount':1,'name':'Beer','price':23}]";

    @Mock
    Number stupidSimpleMockObject;

    @Mock
    SharedPreferences sharedPreferences;

    @Test
    public void testWithStupidSimpleMock(){
        when(stupidSimpleMockObject.doubleValue()).thenReturn(40.0);
        assertThat(stupidSimpleMockObject.doubleValue(), is(42.0));
    }

    @Test
    public void testSharedPrefs() {

        //TODO!
    }

    private void setupSharedPreferencesMock() {
        //Behaviour for getString(key, defaultValue) method call:
        when(sharedPreferences.getString(
                eq(UserStateHelper.KEY_JSON_BASKET), anyString() )
        ).thenReturn(json);
    }
}
