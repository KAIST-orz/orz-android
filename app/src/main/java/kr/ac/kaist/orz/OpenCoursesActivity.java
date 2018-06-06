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

    //데이터를 저장하게 되는 리스트
    List<myCourseInformation> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_courses);

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview_openCourse);
        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new myCourseViewAdapter(this, list);
        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);
    }

    //Activity가 사용자와 상호작용을 하기 직전에 호출되는 부분
    //[Ref] 안드로이드 생명주기 (http://vaert.tistory.com/161)
    protected void onResume()
    {
        super.onResume();

        // List 초기화
        list.clear();

        // List에 데이터 입력
        addData();
    }


    //리스트뷰에 보여질 아이템을 list에 추가
    //DB에서 전달받은 값을 list에 add하면 될 듯.
    private void addData()
    {
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(A)","Sumgwon Kang",0));
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(B)","Park Jinah", 1));
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(C)","Martin ZIEGLER", 2));
        list.add(new myCourseInformation("Data Structure", "CS206(A)","Keeung Kin", 3));
        list.add(new myCourseInformation("Data Structure", "CS206(B)","Alice Oh", 4));
        list.add(new myCourseInformation("Data Structure", "CS206(C)","Duksan Ryu", 5));
    }
}
