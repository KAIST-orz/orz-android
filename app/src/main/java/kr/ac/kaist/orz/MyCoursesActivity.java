package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);


        //데이터를 저장하게 되는 리스트
        final List<Course> list = new ArrayList<>();

        //리스트뷰에 보여질 아이템을 추가
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

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview);

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

        /*
        ///////////////////////////////////////////////////////////////////////////////
        //Testing
        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //--For testing : 버튼 클릭 시 assignment 이름 출력--
//                Toast.makeText(getApplicationContext() , "Test successful!!!",Toast.LENGTH_LONG).show();
                //

                // 상세정보 화면으로 이동하기(인텐트 날리기)
                // 1. 다음화면을 만든다
                // 2. AndroidManifest.xml 에 화면을 등록한다
                // 3. Intent 객체를 생성하여 날린다
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        AssignmentDetailsActivity.class); // 다음넘어갈 화면

                // intent 객체에 데이터를 실어서 보내기
                // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                intent.putExtra("Assignment ID", list.get(position).getCourseName());

                startActivity(intent);

            }
        });
        ///////////////////////////////////////////////////////////////////////////////
        */

        /*
        m_Adapter.add(new Course("Logical Writing", "HSS001(K)","Jaeun Oh"));
        m_Adapter.add(new Course("Interactive Product Design", "ID301(A)","Woohun Lee"));
        m_Adapter.add(new Course("Computer Organization", "CS311","Hyunsoo Yoon"));
        m_Adapter.add(new Course("Introduction to Software Engineering", "CS350","Doo-Hwan Bae"));
        */
    }
}
