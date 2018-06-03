package kr.ac.kaist.orz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OpenCoursesActivity extends AppCompatActivity {
    private ListView m_ListView;
    private ListAdapter m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_courses);

        //데이터를 저장하게 되는 리스트
        List<myCourseInformation> list = new ArrayList<>();
        //리스트뷰에 보여질 아이템을 추가
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(A)","Sumgwon Kang"));
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(B)","Park Jinah"));
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(C)","Martin ZIEGLER"));
        list.add(new myCourseInformation("Data Structure", "CS206(A)","Keeung Kin"));
        list.add(new myCourseInformation("Data Structure", "CS206(B)","Alice Oh"));
        list.add(new myCourseInformation("Data Structure", "CS206(C)","Duksan Ryu"));

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview_openCourse);

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new myCourseViewAdapter(this, list);

        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);
    }
}
