package io.github.wwucbe.cbecalculatorv2;

import java.util.List;

import io.github.wwucbe.integretedbackend.Course;

/**
 * Class with static fields and methods to pass data around between activities
 */

public class Storage {
    /* private constructor so nobody can instantiate it */
    private Storage() {}

    private static List<Course> courses;
//    private static List<Course> userAdded;
//    private static List<Course> joinedCourses;

    /* getters and setters */


    public static List<Course> getCourses() {
        return courses;
    }

    public static void setCourses(List<Course> courses) {
        Storage.courses = courses;
    }


}
