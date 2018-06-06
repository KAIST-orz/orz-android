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

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        Spinner schoolsSpinner = findViewById(R.id.spinner_schools);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schools_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        schoolsSpinner.setAdapter(adapter);

        TextView idView = findViewById(R.id.editText_id);
        EditText passView = findViewById(R.id.editText_password);
        EditText verifyView = findViewById(R.id.editText_verify);
        EditText emailView = findViewById(R.id.editText_email);
        CheckBox lecturerCheckBox = findViewById(R.id.checkBox_lecturer);

        idView.setText(" ".concat(getUserID()));
        passView.setText(getUserPassword());
        verifyView.setText(getUserPassword());
        emailView.setText(getUserEmail());
        schoolsSpinner.setSelection(getUserSchool());
        lecturerCheckBox.setChecked(isUserLecturer());
    }

    public String getUserID() {
        return "asdf";
    }

    public String getUserPassword() {
        return "asdf";
    }

    public String getUserEmail() {
        return "asdf@asdf";
    }

    public int getUserSchool() {
        return 1;
    }

    public boolean isUserLecturer() {
        return false;
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
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
