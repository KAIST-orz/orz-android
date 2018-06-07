package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesActivity extends AppCompatActivity {
    private ListView m_ListView;
    private myCourseViewAdapter m_Adapter;

    //데이터를 저장하게 되는 리스트
    private List<Course> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        //List에 정보 추가 (이 정보들이 listView로 보여짐)
        //addData();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview_myCourses);
        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new myCourseViewAdapter(this, list);
        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);

        //Floating Action Button을 누르면 OpenCourse 페이지로 넘어갈 수 있게.
        FloatingActionButton fab = findViewById(R.id.myCourse_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        OpenCoursesActivity.class); // 다음넘어갈 화면

                startActivity(intent); //다음 화면으로 넘어감
            }
        });
    }


    //Activity가 사용자와 상호작용을 하기 직전에 호출되는 부분
    //[Ref] 안드로이드 생명주기 (http://vaert.tistory.com/161)
    protected void onResume()
    {
        super.onResume();
        refreshView();
    }


    //리스트뷰에 보여질 아이템을 list에 추가
    //DB에서 전달받은 값을 list에 add하면 될 듯.
    private void addData()
    {
        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        Call<List<Course>> call = api.getStudentCourses(user.getID());
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
        list.add(new myCourseInformation("Logical Writing", "HSS001(K)","Jaeun Oh", 100));
        list.add(new myCourseInformation("Interactive Product Design", "ID301(A)","Woohun Lee", 200));
        list.add(new myCourseInformation("Computer Organization", "CS311","Hyunsoo Yoon", 301));
        list.add(new myCourseInformation("Introduction to Software Engineering", "CS350","Doo-Hwan Bae", 404));
        */
    }

    public void refreshView(){
        // List 초기화
        list.clear();

        // List에 데이터 입력
        addData();
    }
}
