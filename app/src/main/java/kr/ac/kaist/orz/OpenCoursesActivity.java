package kr.ac.kaist.orz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Course;

import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenCoursesActivity extends AppCompatActivity {
    private ListView m_ListView;
    private myCourseViewAdapter m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_courses);

        //데이터를 저장하게 되는 리스트
        final List<Course> list = new ArrayList<>();

        //리스트뷰에 보여질 아이템을 추가
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

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)findViewById(R.id.listview_openCourse);

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new myCourseViewAdapter(this, list);

        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);
    }
}
