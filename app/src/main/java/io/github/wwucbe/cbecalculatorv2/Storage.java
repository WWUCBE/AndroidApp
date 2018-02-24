package io.github.wwucbe.cbecalculatorv2;

import java.util.ArrayList;
import java.util.List;

import io.github.wwucbe.integretedbackend.Course;

/**
 * Class with static fields and methods to pass data around between activities
 */

public class Storage {
    /* private constructor so nobody can instantiate it */
    private Storage() {}

    public static ArrayList<Course> courses;
    public static ArrayList<Course> joinedCBECourses;
    public static ArrayList<Course> joinedMSCMCourses;

    /* getters and setters */


    public static void setCourses(List<Course> newCourses) {
        joinedMSCMCourses = new ArrayList<>();
        joinedCBECourses = new ArrayList<>();
        courses = new ArrayList<>();
        for (Course c : newCourses) {
            courses.add(c);
            joinedCBECourses.add(c);
            joinedMSCMCourses.add(c);
        }
            }

    public static void add(Course c) {
        joinedCBECourses.add(c);
        joinedMSCMCourses.add(c);
    }

    public static void remove(Course c) {
        joinedCBECourses.remove(c);
        joinedMSCMCourses.remove(c);
    }

}
