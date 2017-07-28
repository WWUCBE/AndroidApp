package io.github.wwucbe.cbecalculatorv2;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import io.github.wwucbe.wwutranscript.CalculateGpa;
import io.github.wwucbe.wwutranscript.Course;

public class TranscriptActivity extends AppCompatActivity {
    static final String TAG = "CBETAG"; // debug purposes
    List<Course> courseListCBE;
    List<Course> courseListMSCM;
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

        /* create the two filtered lists and save them */
        courseListCBE = CalculateGpa.INSTANCE.filterCourses(LoginActivity.courseList, CBE);
        courseListMSCM = CalculateGpa.INSTANCE.filterCourses(LoginActivity.courseList, MSCM);

        /* calculate the GPAs */
        gpaCBE = CalculateGpa.INSTANCE.getCBEGPA(courseListCBE);
        gpaMSCM = CalculateGpa.INSTANCE.getMSCMGPA(courseListMSCM);

        /*create two adapters  and set CBE as default */
        listview = (ListView) findViewById(R.id.courseListview);
        adapterCBE = new CourseArrayAdapter(this, courseListCBE);
        adapterMSCM = new CourseArrayAdapter(this, courseListMSCM);
        listview.setAdapter(adapterCBE);

        /* set up the toggle button (kinda hacky, but it works)*/
        toggleMode(null);

        /* display the gpa */
        updateGPA();

    }

    /* updates the textview that displays the GPA */
    private void updateGPA() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        TextView tvGpa = (TextView) findViewById(R.id.gpa_textview);

        if (currentMode == CBE) {
            tvGpa.setText(df.format(gpaCBE));
        } else {
            tvGpa.setText(df.format(gpaMSCM));
        }
    }

    /* toggles the buttons and reruns the GPA calculation */
    public void toggleMode(View view) {
        Button selected_b;
        Button unselected_b;
        if (currentMode == CBE) {
            currentMode = MSCM;
            listview.setAdapter(adapterMSCM);
            selected_b = (Button) findViewById(R.id.mscn);
            unselected_b = (Button) findViewById(R.id.cbe);
        } else {
            currentMode = CBE;
            listview.setAdapter(adapterCBE);
            selected_b = (Button) findViewById(R.id.cbe);
            unselected_b = (Button) findViewById(R.id.mscn);
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
            LoginActivity.autoLogin = false;
        } else if (id == R.id.actionHelp) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
