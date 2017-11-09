package io.github.wwucbe.integretedbackend

import java.util.ArrayList

/* Input: a string containing just the raw text of the transcript.
*  Output: a list of the lines from the transcript that are classes.
*  Does not perform any parsing of class details. */
internal fun getRawClassList(text: String): ArrayList<String> {
    val classesLines = ArrayList<String>()
    val lines = text.split("\n".toRegex())
    val regex = Regex("^\\w{3,4}\\s+[0-9]")
    for (line in lines) {
        /* If it's a class, line starts with Subject code, e.g. FIN or CSCI */
        if (regex.containsMatchIn(line)) {
            classesLines.add(line)
        }
    }
    return classesLines
}

/*Input: list of lines from the transcript that are classes
* Output: none
* Side Effect: adds Course objects to the class-level courseList
* Description: Parses each line, pulling out class details */
internal fun createCourseObjects(rawClassList: ArrayList<String>): ArrayList<Course> {
    val courseList = ArrayList<Course>();
    /* hardcoded offset values */
    val offset_crse: Int = 5
    val offset_name: Int = 17
    val offset_credits: Int = 48
    val offset_grade: Int = 52

    /* even though the actual transcript is rigidly formatted, this takes a
    *  more careful approach, because our test files are kinda scattershot,
    *  and it was easier to modify this than fix all the test files.*/
    var regex = Regex("\\s+") // 1 or more spaces
    for (course in rawClassList) {
        val splitLine = course.split(regex);
        val subject = splitLine[0]
        val crse = splitLine[1].toInt()

        /* crn is next, than the name starts, which is of course split. So
        *  we go down the list until finding an integer, which will be the
        *  credit number*/

        var creditPos = 3
        var word = splitLine[creditPos]
        while (word.toIntOrNull() !is Int) {
            creditPos++
            word = splitLine[creditPos]
        }

        /* at this point, "word" contains the credits, and substring(3, creditPos) will net us the name */

        val name = splitLine.slice(3..creditPos).joinToString(" ")
        val credits = word.toInt()
        val grade = splitLine[creditPos+1]

        val courseObject = Course(name, grade, subject, crse, credits, false)
        courseList.add(courseObject)
    }

    return courseList
}