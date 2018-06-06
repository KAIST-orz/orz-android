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

public class lecturerCourseViewAdapter extends ArrayAdapter {
    private Context mainactivity_context;
    List<myCourseInformation> list;

    // ListViewAdapter의 생성자
    public lecturerCourseViewAdapter(Context context, List<myCourseInformation> courses) {
        super(context, R.layout.mycourse, courses);
        mainactivity_context = context;
        list = courses;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.lecturer_course, parent, false);

        //화면에 뿌려줄 정보 (myCourseInformation)
        myCourseInformation aItem = (myCourseInformation) getItem(position);

        //View template으로 사용되는 mycourse.xml에서, 각 요소들을 따옴
        TextView courseNameView = (TextView) customView.findViewById(R.id.CourseName);
        TextView courseCodeView = (TextView) customView.findViewById(R.id.CourseCode);
        TextView courseLecturerView = (TextView) customView.findViewById(R.id.CourseLecturer);

        //mycourse.xml에서 정해준 TextView들에게 정보를 뿌려줌
        courseNameView.setText(aItem.getCourseName());
        courseCodeView.setText(aItem.getCourseCode());
        courseLecturerView.setText(aItem.getCourseLecturer());

        return customView;
    }
}
