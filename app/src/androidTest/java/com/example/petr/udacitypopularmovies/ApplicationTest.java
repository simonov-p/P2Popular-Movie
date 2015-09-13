package com.example.petr.udacitypopularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.format.Time;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

        long time = System.currentTimeMillis();
        String[] params = {"qwe", "rty"};
        String sort = String.format("%s.%s", params[0], params[1]);
        System.out.println(sort);


//        System.out.println(normalizeDate(time));
//        System.out.println(time);
//        System.out.println(normalizeDate(time)-time);
//        System.out.println(new Date(normalizeDate(time)));
//        System.out.println(new Date(time));
//        System.out.println(new Date(time - normalizeDate(time)));


    }
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }
}