# CBE GPA Android App

## What This Is

If you're reading this, you're presumably a senior at WWU that picked this as your Senior Project. And if it was being offered as a senior project, that means something went wrong and you need to fix it, or there are missing features that you need to add. This readme will attempt to familiarize you with the code to make your job easier.

## The Code

The codebase is split into two: the frontend, written in Java, and the backend, written in Kotlin. (I went with kotlin because at the time of this writing it was the hot new thing; hopefully it's still around when you're reading this). The backend is separated into a module in Android Studio.

### Backend

The backend handles connecting to wwu.edu and retrieving the transcript; parsing the transcript (using jSoup); and creating Course objects (defined in Course.kt). When Course objects are created, it checks if it's a valid CBE or MSCM course and sets variables appropriately. getCourseList in TranscriptFetcher.kt is what the app calls to get a List of courses; later, the app send the list to the two functions in calculateGpa.kt to get a numeric GPA value. 

### Frontend

Pretty standard android app. Uses SharedPreferences to save username/password. CourseArrayAdapter implements Filterable to filter the course list (note that the FIlter sublclass is nested inside the Adapter). Storage just holds a reference to the Course list. User added classes are added and removed from the Course list. 

Lots of room for improvement; It's not the best code ever. 

## Something Went Wrong, Plz Help

Guesses as to what went wrong (and where to put the fix):
- Login doesn't work anymore
  - Use the network developer tools in chrome (or whatever) to analyze the POST request and see if something changed. Fix TranscriptFetcher.kt
- Required classes for one or both majors changed
  - Simple fix, look in Constants.kt
- Login works, but something breaks after that
  - Western may have updated how the transcripts are presented. Maybe the DOM changed, or they switched ordering of elements in the transcript. Fix it in TranscriptFetcher.kt 
- The android app looks funky
  - Good luck with that. I never could understand theming in android. 

## Open Bugs

- In the Add Class popup, the grade Spinner is themed slightly wrong; when a grade is selected, the text color is grey, rather than black. 
  - Probably related: on an HTC One M9, the text wasn't showing up at all. Couldn't reproduce anywhere else.
  
