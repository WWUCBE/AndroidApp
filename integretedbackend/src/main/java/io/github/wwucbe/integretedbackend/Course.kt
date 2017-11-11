package io.github.wwucbe.integretedbackend

/**
 * Holds course information.
 */
data class Course(val name: String,
                  val grade: String,
                  val subject: String,
                  val courseNum: Int,
                  val credits: Int,
                  val userAdded: Boolean) {

    var mscmCourse: Boolean = false;
    var cbeCourse: Boolean = false;

    init {
        if (subject in Constants.MSCMAllowedCourses.keys) {
            val subjects = Constants.MSCMAllowedCourses.get(subject)
            if (courseNum in subjects!!) {
                mscmCourse = true;
            }
        }

        if (subject in Constants.CBEAllowedSubjects) {
            cbeCourse = true;
        }
    }
}
