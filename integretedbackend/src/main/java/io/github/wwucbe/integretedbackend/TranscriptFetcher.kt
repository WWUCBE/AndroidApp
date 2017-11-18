package io.github.wwucbe.integretedbackend

import org.jsoup.Jsoup
import java.io.*
import java.net.*
import java.util.LinkedHashMap
/*
*  Goal: fetch a user's transcript from WWU's unofficial transcript page.
*  Three public methods: getTranscriptPage() takes username/password and returns
*  the page source.
*  parseTranscriptPage() takes a page source and parses it.
*  getCourseList() is a convenience method that runs the other two methods.
*  The reason for this structure is so we can easily feed the parse test pages. */

/* Input: username and password for WWU's Universial Sign-On
*  Output: List of Course objects
*  Description: This is the main public method. It calls all the necessary private methods
*               to retrieve the transcript page, parse the classes out of it, parse the class
*               details and populate the list.*/
@Throws(IOException::class)
fun getCourseList(username: String, password: String): List<Course>? {
    val html = fetchTranscript(username, password)
    if (html == null) {
        return null
    } else {
        return parseTranscriptPage(html);
    }
}

/* Input: complete HTML of transcript page
 * Output: List of Course objects
 * Description: this is factored out and public so we can easily pass in test pages */
fun parseTranscriptPage(html: String): List<Course> {
    val text = getTransText(html)
    val lineList = getRawClassList(text)
    return createCourseObjects(lineList)
}

/* Input: String of the transcript page's entire HTML
*  Output: String of just the actual transcript text
*  Description: JSoup is the bomb. Not much else to say.*/
private fun getTransText(html: String): String {
    val doc = Jsoup.parse(html)

    val pres = doc.select("pre")
    val text = pres.select("font").last().text()

    return text
}

/* Input: String of the transcript page's entire HTML
*  Output: the text found in the value field of a certain hidden input field
*  Description: There's a long-ass code that's uniquely generated
*               each time page loads. Not sure what it's for, but
*               the server wants it. */
private fun extractExecution(html: String): String {
    val soup = Jsoup.parse(html)

    val inputFields = soup.select("input")
    var execution = "not initialized"
    for (f in inputFields) {
        if (f.attr("name") == "execution") {
            execution = f.attr("value")
        }
    }
    return execution
}

/* Input: URL to GET
*  Output: entire HTML content of page
*  Description: GETs page, reads it, keeps newlines (important for the actual
*               transcript portion) and returns the string */
private fun getPage(url: URL): String {
    val yc = url.openConnection()
    val input = BufferedReader(InputStreamReader(
            yc.getInputStream()))
    val htmlBuilder = StringBuilder()
    var inputLine: String?
    inputLine = input.readLine()
    while (inputLine != null) {
        htmlBuilder.append(inputLine)
        htmlBuilder.append("\n")
        inputLine = input.readLine()
    }
    input.close()

    return htmlBuilder.toString()
}

/* Input: URL to send the post request to, and paremeters to send
*  Output: HTTP response code from server
*  Description: formats the parameters in the proper form and sends them. */
private fun postData(url: URL, username: String, password: String, execution: String): Int {
    /* create the parameters we're going to POST.
    *  Geolocation seems to be optional, but the login page sends it, so we will too. */
    val params = LinkedHashMap<String, Any>()
    params.put("username", username)
    params.put("password", password)
    params.put("execution", execution)
    params.put("geolocation", "")
    params.put("_eventId", "submit")

    /* write the params in the proper format  */
    val postData = StringBuilder()
    for ((key, value) in params) {
        if (postData.isNotEmpty()){
            postData.append('&')
        }
        postData.append(URLEncoder.encode(key, "UTF-8"))
        postData.append('=')
        postData.append(URLEncoder.encode(value.toString(), "UTF-8"))
    }
    val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))

    /* send the post requests with parems  */
    val conn = url.openConnection() as HttpURLConnection
    conn.instanceFollowRedirects = false
    conn.requestMethod = "POST"
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
    conn.setRequestProperty("Content-Length", postDataBytes.size.toString())
    conn.doOutput = true
    conn.outputStream.write(postDataBytes)

    /* return the HTTP response code*/
    return conn.responseCode
}

/* Input: Username and Password for WWU Universal Sign-on
*  Output: On success, a String containing the entire page source
*          of the unofficial transcript page.
*          On failure, returns null
*  Description: uses Java's networking library to do all the stuff. GETs the
*               login page, scrapes the mystery "execution" field, POSTs everything.
*               Checks return code to see if login was successful.
*  */
@Throws(IOException::class)
private fun fetchTranscript(username: String, password: String): String? {
    /* postURL will redirect on success*/
    val postURL = URL("https://websso.wwu.edu/cas/login?service=https%3A%2F%2Fadmin.wwu.edu%2Fpls%2Fwwis%2Ftwzkcasl.P_Service_Ticket%3Ftarget%3Dwwskahst.WWU_ViewTran")
    val transURL = URL("https://admin.wwu.edu/pls/wwis/wwskahst.WWU_ViewTran")

    /*sets up cookie managment so we actually stay logged in for the duration of the program */
    val cookieManager = CookieManager()
    CookieHandler.setDefault(cookieManager)

    /*get the login page so we can get the execution value, which randomly changes every time.
    * What is it? Beats me, but the server wants it. */
    val html = getPage(postURL)

    /* extractExecution() just uses JSoup to get the value */
    val execution = extractExecution(html)

    /* post the data and grab the HTTP response code*/
    val response = postData(postURL, username, password, execution)

    /* if login successful, server returns 302 (moved temporarily */
    if (response == HttpURLConnection.HTTP_MOVED_TEMP) {
        /* return the entire transcript page */
        return getPage(transURL)
    } else { /* login failed */
        return null
    }
}



