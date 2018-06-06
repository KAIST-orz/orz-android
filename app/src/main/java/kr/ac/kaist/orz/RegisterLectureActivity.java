package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            OrzApi api = ApplicationController.getInstance().getApi();
            User user = ApplicationController.getInstance().getUser();
            Map<String, String> body = new HashMap<>();
            body.put("name", lecture_name.getText().toString());
            body.put("code", lecture_code.getText().toString());
            body.put("professor", professor.getText().toString());
            Call<Void> call = api.registerLecture(user.getID(), body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful() && response.code()==200) {
                        Toast.makeText(RegisterLectureActivity.this, "register lecture success", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        Toast.makeText(RegisterLectureActivity.this, "register lecture failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterLectureActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                }
            });

            Toast.makeText(this, "lecture created", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
