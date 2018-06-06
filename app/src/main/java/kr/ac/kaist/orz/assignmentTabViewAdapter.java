package kr.ac.kaist.orz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kr.ac.kaist.orz.models.Assignment;

//This is custom listview adaptor for ListView in activity_my_courses.xml, using mycourse.xml as template
public class assignmentTabViewAdapter extends ArrayAdapter {

    // ListViewAdapter의 생성자
    public assignmentTabViewAdapter(Context context, List<Assignment> courses) {
        super(context, R.layout.assignment_view, courses);
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.assignment_view, parent, false);

        //화면에 뿌려줄 정보 (myCourseInformation)
        Assignment aItem = (Assignment) getItem(position);

        //View template으로 사용되는 mycourse.xml에서, 각 요소들을 따옴
        TextView courseName = (TextView) customView.findViewById(R.id.course_name);
        TextView assignmentName = (TextView) customView.findViewById(R.id.assignment_name);

        //mycourse.xml에서 정해준 TextView들에게 정보를 뿌려줌
        String assignCourse = aItem.getCourseName();
        String assignName = aItem.getName();
        courseName.setText(assignCourse);
        assignmentName.setText(assignName);

        return customView;
    }
}
