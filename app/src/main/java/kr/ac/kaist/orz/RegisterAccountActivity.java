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

public class RegisterAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        Spinner spinner = findViewById(R.id.spinner_schools);
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
        int pass = passView.getText().toString().hashCode();
        int verify = verifyView.getText().toString().hashCode();
        String email = emailView.getText().toString();
        String school = schoolsSpinner.getSelectedItem().toString();
        boolean isLecturer = lecturerCheckBox.isChecked();

        if(id.length() == 0)
            Toast.makeText(this, "The ID is empty", Toast.LENGTH_LONG).show();

        else if(pass == 0)
            Toast.makeText(this, "The password is empty", Toast.LENGTH_LONG).show();

        else if(verify == 0)
            Toast.makeText(this, "The password is empty", Toast.LENGTH_LONG).show();

        else if(email.length() == 0)
            Toast.makeText(this, "The email is empty", Toast.LENGTH_LONG).show();

        else if(!checkID(id))
            Toast.makeText(this, "The ID already exists", Toast.LENGTH_LONG).show();

        else if(!checkPassword(pass, verify))
            Toast.makeText(this, "The passwords does not match", Toast.LENGTH_LONG).show();

        else if(!checkEmail(email))
            Toast.makeText(this, "Inappropriate email format", Toast.LENGTH_LONG).show();

        else {
            Toast.makeText(this, "register success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    public boolean checkID(String id) {
        return !id.equals("asdf");
    }

    public boolean checkPassword(int pass, int verify) {
        return pass == verify;
    }

    public boolean checkEmail(String email) {
        return email.contains("@");
    }

    // change to int, and handle various situations?
}
