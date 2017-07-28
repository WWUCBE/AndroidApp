package io.github.wwucbe.cbecalculatorv2;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.wwucbe.wwutranscript.Course;

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
    }

    Map<String, Boolean> subjects;

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
            // cache the ViewHolder object inside the view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data
        viewHolder.tvName.setText(course.getName());
        viewHolder.tvSubject.setText(course.getSubject());
        viewHolder.tvCredits.setText(String.valueOf(course.getCredits()));
        viewHolder.tvGrade.setText(course.getGrade());
        viewHolder.tvCrse.setText(String.valueOf(course.getCourseNum()));

        // Return the completed view to render on screen
        return convertView;
    }

}
