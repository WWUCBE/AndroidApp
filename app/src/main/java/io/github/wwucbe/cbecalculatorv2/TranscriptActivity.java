package io.github.wwucbe.cbecalculatorv2;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.wwucbe.integretedbackend.CalculateGpaKt;
import io.github.wwucbe.integretedbackend.Course;

public class TranscriptActivity extends AppCompatActivity
        implements AddClassFragment.NoticeDialogListener {
    static final String TAG = "CBETAG"; // debug purposes
    ListView listview;

    float gpaCBE;
    float gpaMSCM;

    CourseArrayAdapter adapterCBE;
    CourseArrayAdapter adapterMSCM;

    final String CBE = "cbe";
    final String MSCM = "mscm";
    String currentMode = MSCM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);

        /* set the toolbar */
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Transcript");
        ab.setDisplayHomeAsUpEnabled(true);

        /* create adapters */
        createAdapters();

        /* set up the toggle button (kinda hacky, but it works)*/
        toggleMode(null);

        /* display the gpa */
        updateGPA();

    }

    /* merge user and fetched course lists, calculate gpas, and create adapters. */
    private void createAdapters() {
        /* calculate the GPAs */
        gpaCBE = CalculateGpaKt.getCBEGPA(Storage.getCourses());
        gpaMSCM = CalculateGpaKt.getMSCMGPA(Storage.getCourses());

        /*create two adapters  and set the current one*/
        adapterCBE = new CourseArrayAdapter(this, Storage.getCourses(),"cbe");
        adapterMSCM = new CourseArrayAdapter(this, Storage.getCourses(), "mscm");

        listview = (ListView) findViewById(R.id.courseListview);
        if (currentMode == CBE) {
            listview.setAdapter(adapterCBE);
        } else {
            listview.setAdapter(adapterMSCM);
        }

        /* display new gpa */
        updateGPA();
    }

    /* updates the textview that displays the GPA */
    private void updateGPA() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        TextView tvGpa = (TextView) findViewById(R.id.gpa_textview);

        if (currentMode == CBE) {
            tvGpa.setText("GPA: " + df.format(gpaCBE));
        } else {
            tvGpa.setText("GPA: " + df.format(gpaMSCM));
        }
    }

    /* toggles the buttons and reruns the GPA calculation */
    public void toggleMode(View view) {
        Button selected_b;
        Button unselected_b;
        if (currentMode == CBE) {
            currentMode = MSCM;
            listview.setAdapter(adapterMSCM);
            selected_b = (Button) findViewById(R.id.mscm);
            unselected_b = (Button) findViewById(R.id.cbe);
        } else {
            currentMode = CBE;
            listview.setAdapter(adapterCBE);
            selected_b = (Button) findViewById(R.id.cbe);
            unselected_b = (Button) findViewById(R.id.mscm);
        }

        selected_b.setBackgroundColor(Color.rgb(61, 141, 255));
        selected_b.setTextColor(Color.BLACK);
        unselected_b.setBackgroundColor(Color.LTGRAY);
        unselected_b.setTextColor(Color.DKGRAY);

        /* update the textview displaying the GPA*/
        updateGPA();
    }


    /* ACTION BAR STUFF */

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transcript_menu, menu);
        return true;
    }

    /* just using this to intercept the "up" button */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /* we do this so the login screen doesn't automatically
         * log the user back in immediately */
        if (id == android.R.id.home) {
            SharedPreferences settings = getSharedPreferences("cbe", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("autoLogin", false);
            editor.apply();
        } else if (id == R.id.actionHelp) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.actionAddClass) {
            final AddClassFragment adderDialog = new AddClassFragment();
            FragmentManager fragmentManager = getFragmentManager();
            adderDialog.show(fragmentManager, "adder");
        }

        return super.onOptionsItemSelected(item);
    }

    /* triggered by a user pressing the "add class" button from the alert dialog
    *  (code for which is in AddClassFragment.java) */
    public void addClass(String subject, String course, String credits, String grade) {
        grade = grade.toUpperCase().trim();
        subject = subject.toUpperCase().trim();
        course = course.trim();
        credits = credits.trim();

        Course c = new Course("user added", grade, subject,
                Integer.valueOf(course), Integer.valueOf(credits), true);
        Storage.getCourses().add(c);
        createAdapters();
    }

    /* triggered by the "remove" button on user-added classes */
    public void removeClass(View view) {
        Course removed = (Course) view.getTag();
        for (int i = 0; i < Storage.getCourses().size(); i++) {
            if (Storage.getCourses().get(i) == removed) {
                Toast.makeText(this, "Removed Class", Toast.LENGTH_SHORT).show();
                Storage.getCourses().remove(i);
                break;
            }
        }

        createAdapters();

    }
}
