package io.github.wwucbe.integretedbackend

/**
 * Created by critter on 7/2/2017.
 */
object Constants {
    val CBE = "cbe"
    val MSCM = "mscm"

    val CBEAllowedSubjects = arrayOf(
            "ECON",
            "ACCT",
            "DSCI",
            "MIS",
            "FIN",
            "MKTG",
            "OPS",
            "MGMT",
            "IBUS",
            "HRM"
    )

    /* lazy initialization means that the code in the brackets will only
    *  be called the first time the property is accessed. */
    val MSCMAllowedCourses: HashMap<String, Array<Int>> by lazy {
        val allowedCourses = HashMap<String, Array<Int>>()
        allowedCourses.put("MATH", arrayOf(157))
        allowedCourses.put("DSCI", arrayOf(205))
        allowedCourses.put("ACCT", arrayOf(240, 245))
        allowedCourses.put("ECON", arrayOf(206, 207))
        allowedCourses.put("MIS", arrayOf(220))
        allowedCourses.put("MGMT", arrayOf(271))
        allowedCourses.put("PHYS", arrayOf(114))
        allowedCourses.put("CHEM", arrayOf(121))
        allowedCourses
    }

}