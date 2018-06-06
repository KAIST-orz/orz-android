package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        Spinner spinner = findViewById(R.id.spinner_schools);
        // TODO: fetch schools from server
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schools_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void register(View v) {
        EditText idView = findViewById(R.id.editText_id);
        EditText passView = findViewById(R.id.editText_password);
        EditText verifyView = findViewById(R.id.editText_verify);
        EditText emailView = findViewById(R.id.editText_email);
        Spinner schoolsSpinner = findViewById(R.id.spinner_schools);
        CheckBox lecturerCheckBox = findViewById(R.id.checkBox_lecturer);

        String id = idView.getText().toString();
        String pass = passView.getText().toString();
        String verify = verifyView.getText().toString();
        String email = emailView.getText().toString();
        int schoolID = Integer.parseInt(schoolsSpinner.getSelectedItem().toString());
        boolean isLecturer = lecturerCheckBox.isChecked();

        if(id.length() == 0)
            Toast.makeText(this, "The ID is empty", Toast.LENGTH_LONG).show();

        else if(pass.length() == 0)
            Toast.makeText(this, "The password is empty", Toast.LENGTH_LONG).show();

        else if(verify.length() == 0)
            Toast.makeText(this, "The password is empty", Toast.LENGTH_LONG).show();

        else if(email.length() == 0)
            Toast.makeText(this, "The email is empty", Toast.LENGTH_LONG).show();

        else if(!checkPassword(pass, verify))
            Toast.makeText(this, "The passwords does not match", Toast.LENGTH_LONG).show();

        else if(!checkEmail(email))
            Toast.makeText(this, "Inappropriate email format", Toast.LENGTH_LONG).show();

        else {
            OrzApi api = ApplicationController.getInstance().getApi();
            Map<String, Object> body = new HashMap<>();
            body.put("username", id);
            body.put("email", email);
            body.put("password", pass);
            body.put("isLecturer", isLecturer);
            body.put("schoolID", schoolID);
            Call<Void> call = api.signup(body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful() && response.code()==200) {
                        Toast.makeText(RegisterAccountActivity.this, "register success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(RegisterAccountActivity.this, "The ID already exists", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterAccountActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public boolean checkPassword(String pass, String verify) {
        return pass.equals(verify);
    }

    public boolean checkEmail(String email) {
        return email.contains("@");
    }

    // change to int, and handle various situations?
}
