package io.github.wwucbe.cbecalculatorv2;

import android.content.Context;
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
import io.github.wwucbe.integretedbackend.*;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    /* do the networking stuff in a different thread, call validate() when complete */
    class RetrieveTranscriptTask extends AsyncTask<String, Void, List<Course>> {
        private List<Course> courses;
        private Exception exception;

        protected List<Course> doInBackground(String... userpass) {
            try {
                courses = TranscriptFetcherKt.getCourseList(userpass[0], userpass[1]);
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

        /* get stored username/password if they exists */
        SharedPreferences settings = getSharedPreferences("cbe", Context.MODE_PRIVATE);
        String username = settings.getString("username", null);
        String password = settings.getString("password", null);
        Boolean autoLogin = settings.getBoolean("autoLogin", false);

        /* if they exist, add them to the EditText fields*/
        if (username != null && password != null) {
            ((TextView) findViewById(R.id.username_et)).setText(username);
            ((TextView) findViewById(R.id.password_et)).setText(password);

            /* check the checkbox, for logical consistency, and because it's looked at by the
             * submit function */
            CheckBox checkbox = (CheckBox) findViewById(R.id.save_info_checkBox);
            checkbox.setChecked(true);

            /* submit is the onclick handler for the button, so we just pass it null for the view */
            /* if I don't change anything this won't get triggered ever */
            if (autoLogin) {
                submit(null);
            }
        }

        /* set teh toobar */
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setTitle(R.string.app_name);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        /* reset button */
        Button button = (Button) findViewById(R.id.login_button);
        button.setText(R.string.login);
        button.setClickable(true);
    }

    /* onclick hander for submit button. Executes the async task. */
    public void submit(View view) {
        EditText username_et = (EditText) findViewById(R.id.username_et);
        EditText password_et = (EditText) findViewById(R.id.password_et);

        String username = username_et.getText().toString();
        String password = password_et.getText().toString();

        new RetrieveTranscriptTask().execute(username, password);

        CheckBox checkBox = (CheckBox) findViewById(R.id.save_info_checkBox);

        SharedPreferences settings = getSharedPreferences("cbe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if (checkBox.isChecked()) {
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("autoLogin", true);
            editor.apply();

        } else {

            editor.remove("username");
            editor.remove("password");
            editor.putBoolean("autoLogin", false);
            editor.apply();
        }

        /* set button */
        Button button = (Button) findViewById(R.id.login_button);
        button.setText(R.string.loggingin);
        button.setClickable(false);

    }

    /* checks whether the login attempt was a success or not. If it was,
    *  it starts the view transcript activity. */
    private void validate(List<Course> courses){
        if (courses == null) {
            Storage.setCourses(null);
            Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show();

            /* reset button */
            Button button = (Button) findViewById(R.id.login_button);
            button.setText(R.string.login);
            button.setClickable(true);
        } else {
            Storage.setCourses(courses);
            Intent intent = new Intent(this, TranscriptActivity.class);
            startActivity(intent);
        }
    }
}
