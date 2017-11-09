package io.github.wwucbe.cbecalculatorv2;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.wwucbe.integretedbackend.*;

/**
 * Created by critter on 6/23/2017.
 */

public class CourseArrayAdapter<T> extends ArrayAdapter {
    private static class ViewHolder {
        TextView tvName;
        TextView tvCrse;
        TextView tvSubject;
        TextView tvCredits;
        TextView tvGrade;
        Button bRemove;
    }

    public CourseArrayAdapter(@NonNull Context context, List<Course> courses) {
        super(context, 0, courses);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Course course = (Course) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            // Lookup view for data population
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvSubject = (TextView) convertView.findViewById(R.id.subject);
            viewHolder.tvCredits = (TextView) convertView.findViewById(R.id.credits);
            viewHolder.tvGrade = (TextView) convertView.findViewById(R.id.grade);
            viewHolder.tvCrse= (TextView) convertView.findViewById(R.id.crse);
            viewHolder.bRemove = (Button) convertView.findViewById(R.id.remove_button);
            // cache the ViewHolder object inside the view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data
        viewHolder.tvName.setText(course.getName());
        viewHolder.tvSubject.setText(course.getSubject());
        viewHolder.tvCredits.setText(String.valueOf(course.getCredits()) + " credits");
        viewHolder.tvGrade.setText(padGrade(course.getGrade()));
        viewHolder.tvCrse.setText(String.valueOf(course.getCourseNum()));
        viewHolder.bRemove.setVisibility(View.INVISIBLE);

        /* set style for user added courses */
        if (course.getUserAdded()) {
            viewHolder.tvName.setTextColor(Color.RED);
            viewHolder.bRemove.setVisibility(View.VISIBLE);
            viewHolder.bRemove.setTag(course);
        }

//        /* set background color different for every other item */
//        if (position % 2 == 0) {
//            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryLightColor));
//        } else {
//            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryLightColor));
//        }

        // Return the completed view to render on screen
        return convertView;
    }

    private String padGrade(String grade) {
        int padding = 3 - grade.length();
        for (int i = 0; i < padding; i++) {
            grade = grade + " ";
        }
        return grade;
    }

}
