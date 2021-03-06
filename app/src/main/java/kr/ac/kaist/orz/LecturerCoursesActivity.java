package kr.ac.kaist.orz;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerCoursesActivity extends AppCompatActivity {
        private ListView m_ListView;
        private lecturerCourseViewAdapter m_Adapter;

        //데이터를 저장하게 되는 리스트
        private List<Course> list = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lecturer_courses);

            // Xml에서 추가한 ListView 연결
            m_ListView = (ListView)findViewById(R.id.lecturer_listview);
            //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
            m_Adapter = new lecturerCourseViewAdapter(this, list);
            //리스트뷰의 어댑터를 지정해준다.
            m_ListView.setAdapter(m_Adapter);


            //listview에 보여지는 row를 클릭 할 경우, 다른 activity로 넘어가는 기능
            //[Ref]http://bitsoul.tistory.com/95 [Happy Programmer~]
            m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    //--For testing : 버튼 클릭 시 courseID 출력
                    //Toast.makeText(getApplicationContext() , Integer.toString(list.get(position).getID()),Toast.LENGTH_LONG).show();

                    // 상세정보 화면으로 이동하기(인텐트 날리기)
                    // 1. 다음화면을 만든다 (생략)
                    // 2. AndroidManifest.xml 에 화면을 등록한다 (생략)
                    // 3. Intent 객체를 생성하여 날린다
                    Intent intent = new Intent(
                            getApplicationContext(), // 현재화면의 제어권자 (fragment의 경우 getActivity, activity의 경우 getApplicationContext 함수 사용)
                            LectureDetailsActivity.class); // 다음넘어갈 화면

                    // intent 객체에 데이터를 실어서 보내기
                    // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                    // 데이터 주고 받는 법 : http://pds0819.tistory.com/entry/%EC%9D%B8%ED%85%90%ED%8A%B8Intent%EB%A1%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%84%EB%8B%ACputExtra-getExtras-%EA%B7%B8%EB%A6%AC%EA%B3%A0-TapHost%EC%97%90%EC%84%9C-%EB%8B%A4%EB%A5%B8-%EC%97%91%ED%8B%B0%EB%B9%84%ED%8B%B0%EB%A1%9C-%EB%B3%80%EC%88%98%EA%B0%92-%EC%A0%84%EB%8B%AC
                    intent.putExtra("courseName", list.get(position).getName());
                    intent.putExtra("courseCode", list.get(position).getCode());
                    intent.putExtra("courseLecturer", list.get(position).getProfessor());
                    intent.putExtra("courseID", list.get(position).getID());


                    startActivity(intent);
                }
            });


            //Floating Action Button을 누르면 OpenCourse 페이지로 넘어갈 수 있게.
            FloatingActionButton fab = findViewById(R.id.lecturer_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //For Testing
                    //Toast.makeText(getApplicationContext() , "Success!",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(
                            getApplicationContext(), // 현재화면의 제어권자
                            RegisterLectureActivity.class); // 다음넘어갈 화면

                    startActivity(intent); //다음 화면으로 넘어감
            }});
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
        Call<List<Course>> call = api.getLecturerCourses(user.getID());
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

    public void refreshView(){
        finish();
        startActivity(getIntent());
    }

    // Let the lecturer see the account settings page.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orz_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AccountSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
