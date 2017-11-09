package io.github.wwucbe.integretedbackend

/**
 * Holds course information.
 */
data class Course(val name: String,
                  val grade: String,
                  val subject: String,
                  val courseNum: Int,
                  val credits: Int,
                  val userAdded: Boolean)
