package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LectureDetailsActivity extends AppCompatActivity {
    //서버에서 불러온 assignment를 저장하는 list
    private List<Assignment> list = new ArrayList<>();

    ListView listview1;
    List<String> assignments =new ArrayList<>();
    ArrayAdapter adapter;
    int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);
//        View view = findViewById(R.id.activity_lecture_details);

        //LecturerCoursesActivity 에서 선택한 Course의 정보를 Intent에 담아 불러온다
        Intent intent = getIntent();
        String courseNameStr = intent.getExtras().getString("courseName");
        String courseCodeStr = intent.getExtras().getString("courseCode");
        String courseLecturerStr = intent.getExtras().getString("courseLecturer");
        courseID = intent.getExtras().getInt("courseID");

        //Course 정보를 보여주는 TextView를 setText 해준다.
        TextView courseName = (TextView) findViewById(R.id.editText_lecture_name);
        TextView courseCode = (TextView) findViewById(R.id.editText_lecture_code);
        TextView courseLecturer = (TextView) findViewById(R.id.editText_professor);
        courseName.setText(courseNameStr);
        courseCode.setText(courseCodeStr);
        courseLecturer.setText(courseLecturerStr);

        //Adapter를 ListView에 연결
        listview1 = findViewById(R.id.listView_notification);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, assignments);
        listview1.setAdapter(adapter);

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (assignments.get(position).equals("Add new assignment")) {
                    Intent intent = new Intent(getApplicationContext(), RegisterAssignmentActivity.class);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                } else {
                    //선택된 assignment의 ID (Intent에 담아서 전달)
                    //Iterator<Assignment> new_itr = list.iterator();
                    int assignmentID_toPass = list.get(position).getID();

                    Intent intent = new Intent(getApplicationContext(), AssignmentDetailsLecturerActivity.class);
                    intent.putExtra("assignmentID", assignmentID_toPass); //Pass the ID of the chosen assignment
                    startActivity(intent);
                }
            }
        });
        setListViewHeightBasedOnChildren(listview1);
    }

    public void delete(View v) {
        //Toast.makeText(this, "lecture deleted", Toast.LENGTH_LONG).show();

        final OrzApi api = ApplicationController.getInstance().getApi();

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure to delete this course?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call = api.deleteCourse(courseID);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() && response.code()==200) {
                            Toast.makeText(LectureDetailsActivity.this, "Successfully deleted", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LectureDetailsActivity.this, LecturerCoursesActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        else {
                            Toast.makeText(LectureDetailsActivity.this, "Deletion failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LectureDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        adb.setNegativeButton("no", null);
        adb.show();

        finish();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //Activity가 사용자와 상호작용을 하기 직전에 호출되는 부분
    //[Ref] 안드로이드 생명주기 (http://vaert.tistory.com/161)
    protected void onResume() {
        super.onResume();

        // List 초기화
        assignments.clear();
        list.clear();

        // List에 데이터 입력
        addData();
    }

    //리스트뷰에 보여질 아이템을 list에 추가
    //DB에서 전달받은 값을 list에 add하면 될 듯.
    private void addData() {
        //리스트뷰에 보여질 아이템을 list에 추가
        //DB에서 전달받은 값을 list에 add하면 될 듯.
        OrzApi api = ApplicationController.getInstance().getApi();
        Call<List<Assignment>> call = api.getCourseAssignment(courseID);
        call.enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                if (response.isSuccessful()) {
                    list.addAll(response.body());

                    //For DEBUGGING
                    //Toast.makeText(getApplicationContext(), "Successful adding name to assignment list " + Integer.toString(list.size()), Toast.LENGTH_LONG).show();

                    //Add assignment names to the list
                    Iterator<Assignment> itr = list.iterator();
                    while(itr.hasNext()){
                        Assignment cursor = itr.next();
                        assignments.add(cursor.getName());
                    }
                    assignments.add("Add new assignment");

                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(listview1);
                } else {
                    Toast.makeText(getApplicationContext(), "Assignment information download from server failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {
                //Assignment가 없을 경우에도 이 부분이 실행되는 것 같다.
                Toast.makeText(getApplicationContext(), "Server communication failed(onFailure)", Toast.LENGTH_LONG).show();
            }
       });
    }

    public void refreshView(){
        finish();
        startActivity(getIntent());
    }
}

