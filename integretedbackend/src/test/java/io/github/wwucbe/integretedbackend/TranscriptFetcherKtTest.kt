package io.github.wwucbe.integretedbackend

import org.junit.Test
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert.*
import java.io.File

/**
 * Created by critter on 11/17/2017.
 */
class TranscriptFetcherKtTest {

    /* this is needed because the backend returns floats of a lesser precision, so a plain
    *  equality check will fail when it shouldn't */
    private fun floatsEqual(a: Float, b: Float) : Boolean {
        if (b == 0f && a == 0f) {
            return true
        }
        else if (b == 0f) {
            return false
        }
        else if (Math.abs(a/b - 1) < .001f) {
            return true
        }

        return false
    }

    @Test
    fun parseTranscriptPageTest() {
        val testDir = File("./testPages/")
        val testPagesNumber = testDir.listFiles().size
        for (file in testDir.listFiles()) {
            /* read in the whole file as a string */
            val testPage = file.readText()

            /* get the precalculated GPAs*/
            val pattern = "(?:<!--\\s*\\w*\\s*\\w*:\\s*)(\\d*.\\d*)(?:.*\\s*\\w*\\s*\\w*:\\s*)?(\\d*.\\d*)?"
            val r = Pattern.compile(pattern)
            val m = r.matcher(testPage)

            /* get the first match */
             if (!m.find()) {
                 continue
             }

            /* save the expected values */
            val expectedCbeGPA = m.group(1).toFloat()
            var expectedMscmGPA: Float?

            /* if there's no MSCM grade in the testfile, that means they should be the same */
            try {
                expectedMscmGPA = m.group(2).toFloat()
            } catch (e: Exception) {
                expectedMscmGPA = expectedCbeGPA;
            }

            /* pass in the page and get back a course list*/
            val courses = parseTranscriptPage(testPage)

            /* run the course list through the two functions to get
            *  the actual gpas*/
            val true_cbe_gpa = getCBEGPA(courses)
            val true_mscm_gpa = getMSCMGPA(courses)

            /* if CBE calculation is wrong */
            if (!floatsEqual(expectedCbeGPA, true_cbe_gpa)) {
                println(file.name)
                println("Calculated CBE Grade: " + true_cbe_gpa)
                println("Expected CBE Grade: " + expectedCbeGPA)
            }

            /* if CBE calculation is wrong */
            if (expectedMscmGPA != null && !floatsEqual(expectedMscmGPA, true_mscm_gpa)) {
                println(file.name)
                println("Calculated MSCM Grade: " + true_cbe_gpa)
                println("Expected MSCM Grade: " + expectedMscmGPA)
            }
        }
    }

}