package com.example.petr.udacitypopularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.format.Time;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static Map<String, Integer> errorMap;
    static {
        Map<ErrorType, Integer> errorMap = new HashMap<>();
        errorMap.put(ErrorType.EMAIL_IS_ALREADY_USED, R.string.email_is_already_used);
        errorMap.put(ErrorType.EMAIL_IS_NOT_CORRECT, R.string.email_is_not_correct);
        errorMap.put(ErrorType.USER_IS_ALREADY_FOLLOWED, R.string.user_is_already_followed);
        errorMap.put(ErrorType.USER_IS_NOT_FOUND, R.string.user_is_not_found);
        errorMap.put(ErrorType.RESOURCE_IS_NOT_FOUND, R.string.resource_is_not_found);
        errorMap.put(ErrorType.FORBIDDEN, R.string.forbidden);

    }

    private enum ErrorType {

        EMAIL_IS_ALREADY_USED("EMAIL_IS_ALREADY_USED"),
        EMAIL_IS_NOT_CORRECT("EMAIL_IS_NOT_CORRECT"),
        USER_IS_ALREADY_FOLLOWED("USER_IS_ALREADY_FOLLOWED"),
        USER_IS_NOT_FOUND("USER_IS_NOT_FOUND"),
        RESOURCE_IS_NOT_FOUND("RESOURCE_IS_NOT_FOUND"),
        FORBIDDEN("FORBIDDEN");


        private String typeError;

        private ErrorType(String error) {
            typeError = error;
        }

        static public ErrorType getError(String error) {
            for (ErrorType type: ErrorType.values()) {
                if (type.getErrorType().equals(error)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("unknown error");
        }

        public String getErrorType() {
            return typeError;
        }

    }

    public ApplicationTest() {
        super(Application.class);

        System.out.println("int:" + checkError("EMAIL_IS_ALREADY_USED"));

//        long time = System.currentTimeMillis();
//        String[] params = {"qwe", "rty"};
//        String sort = String.format("%s.%s", params[0], params[1]);
//        System.out.println(sort);


//        System.out.println(normalizeDate(time));
//        System.out.println(time);
//        System.out.println(normalizeDate(time)-time);
//        System.out.println(new Date(normalizeDate(time)));
//        System.out.println(new Date(time));
//        System.out.println(new Date(time - normalizeDate(time)));


    }

    public static Integer checkError(String error) {
        return errorMap.get(error);
    }
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }
}