package io.github.wwucbe.integretedbackend

/**
 * Calculates Grade Point Average.
 */

/* Input: List of courses, and a map of subject names to visibility.
*  Output: grade point averages for classes
*  Description: takes into account all relevant classes, including dupes, and averages them */
fun getCBEGPA(courses: List<Course>): Float {
    var totalCredits = 0
    var totalPoints = 0f
    for (c in courses) {
        /* return value of -1 indicates it shouldn't be factored into the GPA calculation*/
        val courseGrade = letterToNumber(c.grade)
        if (courseGrade >= 0 && c.cbeCourse) {
            totalCredits += c.credits
            totalPoints += courseGrade * c.credits
        }
    }

    val gpa = totalPoints / totalCredits
    return gpa
}

/* Input: List of courses, and a map of subject names to visibility.
*  Output: grade point averages for classes
*  Description: only takes into account most recent class when encountering duplicates  */
fun getMSCMGPA(courses: List<Course>): Float {
    /* Create a data class to store credits and number grade */
    data class GradeInfo(val credits: Int, val grade: Float);
    /* store grades in hashmap, automatically overwriting any older grades */
    val uniqueClasses = HashMap<String, GradeInfo>()
    for (c in courses) {
        /* return value of -1 indicates it shouldn't be factored into the GPA calculation*/
        val courseGrade = letterToNumber(c.grade)
        if (courseGrade >= 0 && c.mscmCourse) {
            uniqueClasses.put(c.name, GradeInfo(c.credits, courseGrade))
        }
    }

    /* iterate over the unique classes and add up the totals */
    var totalCredits = 0
    var totalPoints = 0f
    for ((name, gradeInfo) in uniqueClasses) {
        totalCredits += gradeInfo.credits
        totalPoints += gradeInfo.grade* gradeInfo.credits
    }

    val gpa = totalPoints / totalCredits
    return gpa
}

/* Input: the grade, e.g. A- or D+
*  Output: the number value, on a 4-point scale. -1 if it doesn't get counted.
*  Description: Handles normal grades, K grades, and Fresh Start students*/
fun letterToNumber(letterGradeOriginal: String): Float {
    var numberGrade = 0f
    var letter: Char
    var modifier = ' '
    val letterGrade = letterGradeOriginal.toUpperCase() + "  " // ugly but works

    /* K is incomplete; after completion, the actual grade will be added after the K, e.g. KB+ */
    if (letterGrade[0] == 'K' && letterGrade[1] == ' ') {
        /* course is still incomplete, no grade assigned */
        letter = 'K'
        modifier = ' '
    } else if (letterGrade[0] == 'K') {
        /* Class has been completed and now there's a grade */
        letter = letterGrade[1]
        modifier = letterGrade[2]
    } else {
        /* Just a normal, non-K class*/
        letter = letterGrade[0]
        modifier = letterGrade[1]
    }

    /* Fresh Start students haves grades marked with an asterisk, and are not counted GPA calculation */
    if ('*' in letterGrade) {
        letter = '*'
    }

    /* Source, under Grades and Grade Averages:
     * http://www.wwu.edu/registrar/student_services/records_grading.shtml */
    if (letter == 'A') {
        numberGrade = 4f
    } else if (letter == 'B') {
        numberGrade = 3f
    } else if (letter == 'C') {
        numberGrade = 2f
    } else if (letter == 'D') {
        numberGrade = 1f
    } else if (letter == 'F') {
        numberGrade = 0f
    } else if (letter == 'Z') {
        numberGrade = 0f
    } else {
        numberGrade = -1f
    }

    if (modifier == '+' && numberGrade < 4) {
        numberGrade += 0.3f
    } else if (modifier == '-') {
        numberGrade -= 0.3f
    }

    return numberGrade
}



