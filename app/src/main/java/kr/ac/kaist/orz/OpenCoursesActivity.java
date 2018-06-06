package kr.ac.kaist.orz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenCoursesActivity extends AppCompatActivity {
    private ListView m_ListView;
    private openCourseViewAdapter m_Adapter;

    //데이터를 저장하게 되는 리스트
    private List<Course> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_courses);

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview_openCourse);
        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new openCourseViewAdapter(this, list);
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
        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        Call<List<Course>> call = api.getSchoolCourses(user.getSchoolID());
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if(response.isSuccessful()) {
                    list.addAll(response.body());
                    m_Adapter.notifyDataSetChanged();
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
            }
        });

        /*
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(A)","Sumgwon Kang",0));
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(B)","Park Jinah", 1));
        list.add(new myCourseInformation("Discrete Mathematics", "CS204(C)","Martin ZIEGLER", 2));
        list.add(new myCourseInformation("Data Structure", "CS206(A)","Keeung Kin", 3));
        list.add(new myCourseInformation("Data Structure", "CS206(B)","Alice Oh", 4));
        list.add(new myCourseInformation("Data Structure", "CS206(C)","Duksan Ryu", 5));
        */
    }
}
