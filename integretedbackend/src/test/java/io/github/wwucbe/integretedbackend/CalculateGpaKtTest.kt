package io.github.wwucbe.integretedbackend

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by critter on 11/17/2017.
 */
class CalculateGpaKtTest {
    @Test
    fun regularGrades() {
        assertEquals(4.0f, letterToNumber("A"));
        assertEquals(1.0f, letterToNumber("D"));
        assertEquals(0.7f, letterToNumber("D-"));
    }

    @Test
    fun lowercaseGrades() {
        assertEquals(4.0f, letterToNumber("a"));
        assertEquals(0f, letterToNumber("f"));
    }

    @Test
    fun KGrades() {
        assertEquals(-1f, letterToNumber("K"));
    }

}