package io.github.wwucbe.cbecalculatorv2;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.github.wwucbe.integretedbackend.*;

/**
 * Created by critter on 7/31/2017.
 */

public class AddClassFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void addClass(String subject, String course, String credits, String grade);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    String[] grades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"};

    View view;

    /* user-typed things */
    String subject;
    String number;
    String credits;
    String grade;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        /* inflate layout */
        view = inflater.inflate(R.layout.add_class_dialog, null);
        // atatch the grade list to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.grade_dropdown_item, R.id.grade_spinner_textview, grades);
        adapter.setDropDownViewResource(R.layout.grade_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_grade_spinner);
        spinner.setAdapter(adapter);

        // Set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle("Add Class")
                // Add action buttons
                .setPositiveButton("add", null)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddClassFragment.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                collectData();
                if (validInput()) {
                    mListener.addClass(subject, number, credits, grade);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void collectData() {
        subject = ((EditText) view.findViewById(R.id.dialog_subject)).getText().toString();
        number = ((EditText) view.findViewById(R.id.dialog_course)).getText().toString();
        credits = ((EditText) view.findViewById(R.id.dialog_credits)).getText().toString();

        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_grade_spinner);
        grade = (String) spinner.getSelectedItem();
    }

    private boolean validInput() {
        if (subject.length() == 0 ||
                number.length() == 0 ||
                credits.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

}
