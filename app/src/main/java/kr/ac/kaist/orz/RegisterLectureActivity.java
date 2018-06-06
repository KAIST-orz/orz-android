package kr.ac.kaist.orz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterLectureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_lecture);
    }

    public void create(View v) {
        EditText lecture_name = findViewById(R.id.editText_lecture_name);
        EditText lecture_code = findViewById(R.id.editText_lecture_code);
        EditText professor = findViewById(R.id.editText_professor);

        if(lecture_name.length() == 0)
            Toast.makeText(this, "lecture name can not be empty", Toast.LENGTH_LONG).show();
        else if(lecture_code.length() == 0)
            Toast.makeText(this, "lecture code can not be empty", Toast.LENGTH_LONG).show();
        else if(professor.length() == 0)
            Toast.makeText(this, "professor can not be empty", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(this, "lecture created", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
