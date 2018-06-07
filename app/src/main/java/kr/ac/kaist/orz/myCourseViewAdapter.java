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
public class myCourseViewAdapter extends ArrayAdapter<Course> {
    List<Course> myCourses;

    private class ViewHolder {
        TextView courseNameText;
        TextView courseCodeProfessorText;
        Button addDeleteButton;
    }

    // ListViewAdapter의 생성자
    public myCourseViewAdapter(Context context, List<Course> courses) {
        super(context, R.layout.mycourse, courses);
        myCourses = courses;
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

        // The course item that contains information about a course.
        Course courseItem = myCourses.get(position);

        // Fill in the TextViews with the course information.
        viewHolder.courseNameText.setText(courseItem.getName());
        viewHolder.courseCodeProfessorText.setText(courseItem.getCode() + " · " + courseItem.getProfessor());
        viewHolder.addDeleteButton.setText("-");    // For My Courses screen, the button deletes courses.

        // The event listener for button click events.
        viewHolder.addDeleteButton.setTag(position);
        viewHolder.addDeleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= (Integer) view.getTag();
                final Course myCourse = myCourses.get(position);

                // Communicate with the server to delete (unsubscribe) the course.
                OrzApi api = ApplicationController.getInstance().getApi();
                User user = ApplicationController.getInstance().getUser();
                Call<Void> call = api.deleteStudentCourses(user.getID(), myCourse.getID());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            // Show a toast message notifying successful deletion.
                            Toast.makeText(getContext(), "Successfully deleted course " + myCourse.getName().toUpperCase(),Toast.LENGTH_LONG).show();

                            // Refresh the activity's view to excluded deleted course.
                            Context context = getContext();
                            if (context instanceof MyCoursesActivity) {
                                ((MyCoursesActivity) context).refreshView();
                            }
                        }
                        else {
                            // Show a message alerting failure.
                            Toast.makeText(getContext(), "Failed to delete course. ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Show a message alerting failure.
                        Toast.makeText(getContext(), "Failed to delete course. ", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        return convertView;
    }
}


//Reference (Custom ListView Adapter) : https://www.youtube.com/watch?v=nOdSARCVYic