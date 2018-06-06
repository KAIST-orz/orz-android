package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.School;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingsActivity extends AppCompatActivity {

    List<String> name_list = new ArrayList<String>();
    List<Integer> id_list = new ArrayList<Integer>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        EditText idView = findViewById(R.id.editText_id);
        EditText passView = findViewById(R.id.editText_password);
        EditText verifyView = findViewById(R.id.editText_verify);
        EditText emailView = findViewById(R.id.editText_email);
        CheckBox lecturerCheckBox = findViewById(R.id.checkBox_lecturer);

        idView.setEnabled(false);
        emailView.setEnabled(false);

        final User user = ApplicationController.getInstance().getUser();

        idView.setText(" ".concat(user.getUserName()));
        emailView.setText(user.getUserEmail());
        lecturerCheckBox.setChecked(user.getUserType() == 1);

        OrzApi api = ApplicationController.getInstance().getApi();

        final Spinner spinner = findViewById(R.id.spinner_schools);
        spinner.setEnabled(false);
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, name_list);
        spinner.setAdapter(adapter);

        final List<School> list = new ArrayList<School>();
        Call<List<School>> call = api.getSchools();
        call.enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                if(response.isSuccessful()) {
                    list.addAll(response.body());

                    for(School school : list) {
                        name_list.add(school.getName());
                        id_list.add(school.getId());
                    }

                    spinner.setSelection(user.getSchoolID());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                Toast.makeText(AccountSettingsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void update(View v) {
        EditText passView = findViewById(R.id.editText_password);
        EditText verifyView = findViewById(R.id.editText_verify);
        EditText emailView = findViewById(R.id.editText_email);
        Spinner schoolsSpinner = findViewById(R.id.spinner_schools);
        CheckBox lecturerCheckBox = findViewById(R.id.checkBox_lecturer);

        String pass = passView.getText().toString();
        String verify = verifyView.getText().toString();
        String email = emailView.getText().toString();
        String school = schoolsSpinner.getSelectedItem().toString();
        boolean isLecturer = lecturerCheckBox.isChecked();


        if(pass.length() == 0)
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
            Toast.makeText(this, "update success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), OrzMainActivity.class);
            startActivity(intent);
        }
    }

    public boolean checkPassword(String pass, String verify) {
        return pass.equals(verify);
    }

    public boolean checkEmail(String email) {
        return email.contains("@");
    }
}
