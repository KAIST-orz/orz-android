package kr.ac.kaist.orz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesActivity extends AppCompatActivity {
    private ListView m_ListView;
    private ListAdapter m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        //데이터를 저장하게 되는 리스트
        List<myCourseInformation> list = new ArrayList<>();
        //리스트뷰에 보여질 아이템을 추가
        list.add(new myCourseInformation("Logical Writing", "HSS001(K)","Jaeun Oh"));
        list.add(new myCourseInformation("Interactive Product Design", "ID301(A)","Woohun Lee"));
        list.add(new myCourseInformation("Computer Organization", "CS311","Hyunsoo Yoon"));
        list.add(new myCourseInformation("Introduction to Software Engineering", "CS350","Doo-Hwan Bae"));

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview);

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new myCourseViewAdapter(this, list);

        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);


        /*
        m_Adapter.add(new myCourseInformation("Logical Writing", "HSS001(K)","Jaeun Oh"));
        m_Adapter.add(new myCourseInformation("Interactive Product Design", "ID301(A)","Woohun Lee"));
        m_Adapter.add(new myCourseInformation("Computer Organization", "CS311","Hyunsoo Yoon"));
        m_Adapter.add(new myCourseInformation("Introduction to Software Engineering", "CS350","Doo-Hwan Bae"));
        */

        /*
        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item = (String)adapterView.getItemAtPosition(position);

                //텍스트뷰에 출력
                selected_item_textview.setText(selected_item);
            }
        });
        */
    }
}
