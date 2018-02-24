package io.github.wwucbe.cbecalculatorv2;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.wwucbe.integretedbackend.*;


/**
 * Created by critter on 6/23/2017.
 */

public class CourseArrayAdapter<T> extends ArrayAdapter implements Filterable {
    private class ViewHolder {
        TextView tvName;
        TextView tvError;
        TextView tvSubject;
        TextView tvCredits;
        TextView tvGrade;
        ImageButton bRemove;
        ImageButton bEdit;
    }

    private final String CBE = "cbe";
    private final String MSCM = "mscm";

    private Filter courseFilter;
    private ArrayList<Course> courseList;
    private String program;
    private Context context;

    public CourseArrayAdapter(@NonNull Context context, ArrayList<Course> courses, String program) {
        super(context, 0, courses);
        this.getFilter().filter(program);
        this.courseList = courses;
        this.program = program;
        this.context = context;
    }

    public String getInfo() {
        return "Type: " + program + " Size: " + courseList.size();
    }

    public int getCount() {
        return courseList.size();
    }

    public Course getItem(int position) {
        return courseList.get(position);
    }

    public long getItemId(int position) {
        return courseList.get(position).hashCode();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup  parent) {
        // Get the data item for this position
        Course course = (Course) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            // Lookup view for data population
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvError = (TextView) convertView.findViewById(R.id.error_message);
            viewHolder.tvSubject = (TextView) convertView.findViewById(R.id.subject_and_num);
            viewHolder.tvCredits = (TextView) convertView.findViewById(R.id.credits);
            viewHolder.tvGrade = (TextView) convertView.findViewById(R.id.grade);
            viewHolder.bRemove = (ImageButton) convertView.findViewById(R.id.remove_button);
            viewHolder.bEdit = (ImageButton) convertView.findViewById(R.id.edit_button);
            // cache the ViewHolder object inside the view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data
        String subj_and_num =  course.getSubject() + " " + String.valueOf(course.getCourseNum());
        subj_and_num = String.format("%1$-" + 9 + "s", subj_and_num);
        viewHolder.tvName.setText(course.getName());
        viewHolder.tvError.setText("");
        viewHolder.tvSubject.setText(subj_and_num);
        viewHolder.tvCredits.setText(String.valueOf(course.getCredits()) + " credits");
        viewHolder.tvGrade.setText(padGrade(course.getGrade()));
        viewHolder.bRemove.setVisibility(View.INVISIBLE);
        viewHolder.bEdit.setVisibility(View.INVISIBLE);

        /* set style for user added courses */
        if (course.getUserAdded()) {
            viewHolder.tvName.setText("");
            viewHolder.tvError.setTextColor(ContextCompat.getColor(context, R.color.secondaryDarkColor));
            viewHolder.bRemove.setVisibility(View.VISIBLE);
            viewHolder.bRemove.setTag(course);
            viewHolder.bEdit.setVisibility(View.VISIBLE);
            viewHolder.bEdit.setTag(course);
            viewHolder.tvError.setVisibility(View.VISIBLE);
            viewHolder.tvName.setVisibility(View.GONE);

            /* detect if it's a valid course  */
            Boolean invalidCBE = course.getCbeCourse() == false && program.equals(CBE);
            Boolean invalidMSCM = course.getMscmCourse() == false && program.equals(MSCM);
            if (invalidCBE || invalidMSCM) {
                viewHolder.tvError.setTextColor(Color.RED);
                viewHolder.tvError.setText("not a valid " + program.toUpperCase() + " course");
            } else {
                viewHolder.tvError.setText("User added");
            }
        } else {
            viewHolder.tvName.setVisibility(View.VISIBLE);
            viewHolder.tvError.setVisibility(View.GONE);
        }

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

    @Override
    public Filter getFilter() {
        if (courseFilter == null)
            courseFilter = new CourseFilter();

        return courseFilter;
    }

    private class CourseFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic

            // We perform filtering operation
            List<Course> filteredCourseList= new ArrayList<Course>();

            for (Course c : courseList) {
                Boolean isCBE = c.getCbeCourse() && constraint.toString().equals(CBE);
                Boolean isMSCM = c.getMscmCourse() && constraint.toString().equals(MSCM);
                Boolean isUserAdded = c.getUserAdded();

                /* show all user added courses no matter what they are */
                if (isCBE || isMSCM || isUserAdded) {
                    filteredCourseList.add(c);
                }
            }

            results.values = filteredCourseList;
            results.count = filteredCourseList.size();

            Log.d("CBEDEBUG", "For program " + constraint.toString());
            Log.d("CBEDEBUG", "Filtered to: " + results.count + " courses");

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            /* replace the entries in the list with the filtered ones, but not changing the
            * reference the original list. */
            courseList.clear();
            courseList.addAll((ArrayList<Course>) results.values);
            notifyDataSetChanged();

        }
    }


}
