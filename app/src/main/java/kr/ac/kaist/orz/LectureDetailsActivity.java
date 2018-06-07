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
import java.util.List;

public class LectureDetailsActivity extends AppCompatActivity {

    List<String> assignments = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);

        //LecturerCoursesActivity 에서 선택한 Course의 정보를 Intent에 담아 불러온다
        Intent intent = getIntent();
        String courseNameStr = intent.getExtras().getString("courseName");
        String courseCodeStr = intent.getExtras().getString("courseCode");
        String courseLecturerStr = intent.getExtras().getString("courseLecturer");
        final int courseID = intent.getExtras().getInt("courseID");

        //Course 정보를 보여주는 TextView를 setText 해준다.
        TextView courseName = (TextView) findViewById(R.id.editText_lecture_name);
        TextView courseCode = (TextView) findViewById(R.id.editText_lecture_code);
        TextView courseLecturer = (TextView) findViewById(R.id.editText_professor);
        courseName.setText(courseNameStr);
        courseCode.setText(courseCodeStr);
        courseLecturer.setText(courseLecturerStr);


        assignments.add("Assignment 1");
        assignments.add("Assignment 2");
        assignments.add("Add new assignment");

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        ListView listview1 = findViewById(R.id.listView_notification);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, assignments);
        listview1.setAdapter(adapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(assignments.get(position).equals("Add new assignment")) {
                    Intent intent = new Intent(getApplicationContext(), RegisterAssignmentActivity.class);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), AssignmentDetailsLecturerActivity.class);
                    startActivity(intent);
                }
            }
        });
        setListViewHeightBasedOnChildren(listview1);
    }

    public void delete(View v) {
        Toast.makeText(this, "lecture deleted", Toast.LENGTH_LONG).show();
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
}
