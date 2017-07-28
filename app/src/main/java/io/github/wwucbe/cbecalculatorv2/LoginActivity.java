package io.github.wwucbe.cbecalculatorv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.github.wwucbe.wwutranscript.*;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    /* we'll save the list here so the other activity can access it. Not great design, but it works. */
    static List<Course> courseList;

    /* so at first start, autologin is enabled, but will be disabled when user hits
    *  up button from the transcript activity. Again, not great design... */
    static boolean autoLogin = true;

    /* do the networking stuff in a different thread, call validate() when complete */
    class RetrieveTranscriptTask extends AsyncTask<String, Void, List<Course>> {
        private List<Course> courses;
        private Exception exception;

        protected List<Course> doInBackground(String... userpass) {
            try {
                courses = TranscriptFetcher.INSTANCE.getCourseList(userpass[0], userpass[1]);
                return courses;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(List<Course> courses) {
            if (exception != null) {
                Log.d("CBE", exception.toString());
            }
            validate(courses);
        }
    }

    /* END ASYNC TASK */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* get stored username/password if the exists */
        SharedPreferences settings = getPreferences(0);
        String username = settings.getString("username", null);
        String password = settings.getString("password", null);

        /* if they exist, add them to the EditText fields*/
        if (username != null && password != null) {
            ((TextView) findViewById(R.id.username_et)).setText(username);
            ((TextView) findViewById(R.id.password_et)).setText(password);

            /* check the checkbox, for logical consistency, and because it's looked at by the
             * submit function */
            CheckBox checkbox = (CheckBox) findViewById(R.id.save_info_checkBox);
            checkbox.setChecked(true);

            /* submit is the onclick handler for the button, so we just pass it null for the view */
            if (autoLogin) {
                submit(null);
            }
        }

        /* set teh toobar */
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.app_name);

    }

    /* onclick hander for submit button. Executes the async task. */
    public void submit(View view) {
        EditText username_et = (EditText) findViewById(R.id.username_et);
        EditText password_et = (EditText) findViewById(R.id.password_et);

        String username = username_et.getText().toString();
        String password = password_et.getText().toString();

        new RetrieveTranscriptTask().execute(username, password);

        CheckBox checkBox = (CheckBox) findViewById(R.id.save_info_checkBox);
        if (checkBox.isChecked()) {
            SharedPreferences settings = getPreferences(0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();

            autoLogin = true;
        } else {
            SharedPreferences settings = getPreferences(0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();
        }

        /* set button */
        Button button = (Button) findViewById(R.id.login_button);
        button.setText("Logging in...");
        button.setClickable(false);

    }

    /* checks whether the login attempt was a success or not. If it was,
    *  it starts the view transcript activity. */
    private void validate(List<Course> courses){
        if (courses == null) {
            courseList = null;
            Toast.makeText(this, "Invalid Logon", Toast.LENGTH_SHORT).show();

            /* reset button */
            Button button = (Button) findViewById(R.id.login_button);
            button.setText("Login");
            button.setClickable(true);
        } else {
            courseList = courses;
            Intent intent = new Intent(this, TranscriptActivity.class);
            startActivity(intent);
        }
    }
}