package kr.ac.kaist.orz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//This is custom listview adaptor for ListView in activity_my_courses.xml, using mycourse.xml as template
public class myCourseViewAdapter extends ArrayAdapter {

    // ListViewAdapter의 생성자
    public myCourseViewAdapter(Context context, List<myCourseInformation> courses) {
        super(context, R.layout.mycourse, courses);
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.mycourse, parent, false);

        //화면에 뿌려줄 정보 (myCourseInformation)
        myCourseInformation aItem = (myCourseInformation) getItem(position);

        //View template으로 사용되는 mycourse.xml에서, 각 요소들을 따옴
        TextView courseNameView = (TextView) customView.findViewById(R.id.myCourseName);
        TextView courseIDView = (TextView) customView.findViewById(R.id.myCourseID);
        TextView courseLecturerView = (TextView) customView.findViewById(R.id.myCourseLecturer);

        //mycourse.xml에서 정해준 TextView들에게 정보를 뿌려줌
        courseNameView.setText(aItem.getCourseName());
        courseIDView.setText(aItem.getCourseID());
        courseLecturerView.setText(aItem.getCourseLecturer());

        return customView;
    }
}

//Reference (Custom ListView Adapter) : https://www.youtube.com/watch?v=nOdSARCVYic