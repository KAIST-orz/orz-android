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
    private Context mainactivity_context;
    List<Course> list;

    // ListViewAdapter의 생성자
    public openCourseViewAdapter(Context context, List<Course> courses) {
        super(context, R.layout.mycourse, courses);
        mainactivity_context = context;
        list = courses;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.mycourse, parent, false);

        //화면에 뿌려줄 정보 (myCourseInformation)
        Course aItem = (Course) getItem(position);

        //View template으로 사용되는 mycourse.xml에서, 각 요소들을 따옴
        TextView courseNameView = (TextView) customView.findViewById(R.id.CourseName);
        TextView courseCodeView = (TextView) customView.findViewById(R.id.CourseCodeProfessor);
        TextView courseLecturerView = (TextView) customView.findViewById(R.id.CourseLecturer);

        //mycourse.xml에서 정해준 TextView들에게 정보를 뿌려줌
        courseNameView.setText(aItem.getName());
        courseCodeView.setText(aItem.getCode());
        courseLecturerView.setText(aItem.getProfessor());

        //삭제 혹은 구독 버튼 클릭 시 나타날 이벤트 구현
        Button addDel_button = (Button) customView.findViewById(R.id.myCourse_AddDelete);
        addDel_button.setTag(position);
        addDel_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                int position=(Integer)view.getTag();
                int courseID_toAdd = list.get(position).getID();

                //삭제 혹은 구독하려고 클릭한 course의 ID 출력 (for testing)
                //Toast.makeText(mainactivity_context, Integer.toString(courseID_toAdd),Toast.LENGTH_LONG).show();


                OrzApi api = ApplicationController.getInstance().getApi();
                User user = ApplicationController.getInstance().getUser();
                Call<List<Course>> call = api.subscribeCourses(user.getID(), courseID_toAdd);

                Toast.makeText(mainactivity_context, "Course subscription successful!",Toast.LENGTH_LONG).show();

                call.enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        if(response.isSuccessful()) {
                            //Adapter에서 mainActivity의 메소드 부르기 (화면 refresh)
                            //[Ref] https://stackoverflow.com/questions/12142255/call-activity-method-from-adapter
                            if(mainactivity_context instanceof MyCoursesActivity) {
                                ((MyCoursesActivity) mainactivity_context).refreshView();
                            }
                        }
                        else {
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                    }
                });

            }
        });

        return customView;
    }
}


//Reference (Custom ListView Adapter) : https://www.youtube.com/watch?v=nOdSARCVYic

