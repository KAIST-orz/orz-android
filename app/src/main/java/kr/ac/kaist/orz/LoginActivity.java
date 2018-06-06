package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn(View v) {
        EditText idView = findViewById(R.id.editText_id);
        EditText passView = findViewById(R.id.editText_password);

        String id = idView.getText().toString();
        String pass = passView.getText().toString();

        OrzApi api = ApplicationController.getInstance().getApi();
        Map<String, String> body = new HashMap<>();
        body.put("username", id);
        body.put("password", pass);
        Call<User> call = api.signin(body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "signin success", Toast.LENGTH_LONG).show();
                    ApplicationController.getInstance().setUser(response.body());
                    if(ApplicationController.getInstance().getUser().getUserType() == 2) {
                        Intent intent = new Intent(getApplicationContext(), OrzMainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), LecturerCoursesActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Wrong id or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void registerAccount(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
        startActivity(intent);
    }
}
