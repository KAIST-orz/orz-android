package kr.ac.kaist.orz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//This is custom listview adaptor for ListView in activity_my_courses.xml, using mycourse.xml as template
public class openCourseViewAdapter extends ArrayAdapter {
    private List<Course> openCourses;

    private class ViewHolder {
        TextView courseNameText;
        TextView courseCodeProfessorText;
        Button addDeleteButton;

    }

    // ListViewAdapter의 생성자
    public openCourseViewAdapter(Context context, List<Course> courses) {
        super(context, R.layout.mycourse, courses);
        openCourses = courses;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.mycourse, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.courseNameText = convertView.findViewById(R.id.CourseName);
            viewHolder.courseCodeProfessorText = convertView.findViewById(R.id.CourseCodeProfessor);
            viewHolder.addDeleteButton = convertView.findViewById(R.id.myCourse_AddDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // The course data to fill this item of ListView.
        Course courseItem = (Course) getItem(position);

        // Fill in the list item with the information.
        viewHolder.courseNameText.setText(courseItem.getName());
        viewHolder.courseCodeProfessorText.setText(courseItem.getCode() + " · " + courseItem.getProfessor());
        viewHolder.addDeleteButton.setText("+");    // For Open Courses screen, user subscribes (adds) a course.

        // The event listener for button click events.
        viewHolder.addDeleteButton.setTag(position);
        viewHolder.addDeleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final Course courseToAdd = openCourses.get(position);

                // Communicate with the server to subscribe a course.
                OrzApi api = ApplicationController.getInstance().getApi();
                User user = ApplicationController.getInstance().getUser();
                Call<List<Course>> call = api.subscribeCourses(user.getID(), courseToAdd.getID());
                call.enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        if(response.isSuccessful()) {
                            // Show a toast message notifying successful subscription.
                            Toast.makeText(getContext(), "Successfully subscribed course " + courseToAdd.getName().toUpperCase(), Toast.LENGTH_LONG).show();
                        }
                        else {
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to connect the server.", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        return convertView;
    }
}
