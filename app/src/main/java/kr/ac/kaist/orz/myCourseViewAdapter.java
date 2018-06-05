package kr.ac.kaist.orz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//This is custom listview adaptor for ListView in activity_my_courses.xml, using mycourse.xml as template
public class myCourseViewAdapter extends ArrayAdapter {
    private Context mainactivity_context;
    List<myCourseInformation> list;

    // ListViewAdapter의 생성자
    public myCourseViewAdapter(Context context, List<myCourseInformation> courses) {
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
        myCourseInformation aItem = (myCourseInformation) getItem(position);

        //View template으로 사용되는 mycourse.xml에서, 각 요소들을 따옴
        TextView courseNameView = (TextView) customView.findViewById(R.id.myCourseName);
        TextView courseCodeView = (TextView) customView.findViewById(R.id.myCourseCode);
        TextView courseLecturerView = (TextView) customView.findViewById(R.id.myCourseLecturer);

        //mycourse.xml에서 정해준 TextView들에게 정보를 뿌려줌
        courseNameView.setText(aItem.getCourseName());
        courseCodeView.setText(aItem.getCourseCode());
        courseLecturerView.setText(aItem.getCourseLecturer());

        //삭제 혹은 구독 버튼 클릭 시 나타날 이벤트 구현
        Button addDel_button = (Button) customView.findViewById(R.id.myCourse_AddDelete);
        addDel_button.setTag(position);
        addDel_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                int position=(Integer)view.getTag();
                int courseID_toModify = list.get(position).getCourseID();

                //삭제 혹은 구독하려고 클릭한 course의 ID 출력 (for testing)
                Toast.makeText(mainactivity_context, Integer.toString(courseID_toModify),Toast.LENGTH_LONG).show();

                //Intent에 삭제 혹은 구독할 course의 ID를 담아 다른 activity로 전달할 수 있다. (아래 코드 부분에 해당됨)
                /*
                Intent intent = new Intent(
                        mainactivity_context, // 현재화면의 제어권자
                        OrzMainActivity.class); // 다음넘어갈 화면
                intent.putExtra("ID of course-to-be-modified", courseID_toModify);

                mainactivity_context.startActivity(intent); //다음 화면으로 넘어감
                */
            }
        });

        return customView;
    }
}


//Reference (Custom ListView Adapter) : https://www.youtube.com/watch?v=nOdSARCVYic