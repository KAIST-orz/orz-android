package kr.ac.kaist.orz;

import android.accounts.Account;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        EditText nameView = findViewById(R.id.editText_name);
        EditText emailView = findViewById(R.id.editText_email);
        CheckBox lecturerCheckBox = findViewById(R.id.checkBox_lecturer);

        idView.setEnabled(false);

        final User user = ApplicationController.getInstance().getUser();

        idView.setText(user.getUserName());
        nameView.setText(user.getName());
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

                    spinner.setSelection(id_list.indexOf(user.getSchoolID()));
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
        EditText nameView = findViewById(R.id.editText_name);
        EditText emailView = findViewById(R.id.editText_email);

        String pass = passView.getText().toString();
        String verify = verifyView.getText().toString();
        final String name = nameView.getText().toString();
        final String email = emailView.getText().toString();

        if(name.length() == 0)
            Toast.makeText(this, "The name is empty", Toast.LENGTH_LONG).show();

        else if(email.length() == 0)
            Toast.makeText(this, "The email is empty", Toast.LENGTH_LONG).show();

        else if(!checkPassword(pass, verify))
            Toast.makeText(this, "The passwords does not match", Toast.LENGTH_LONG).show();

        else if(!checkEmail(email))
            Toast.makeText(this, "Inappropriate email format", Toast.LENGTH_LONG).show();

        else {

            OrzApi api = ApplicationController.getInstance().getApi();
            final User user = ApplicationController.getInstance().getUser();
            Map<String, Object> body = new HashMap<>();
            body.put("name", name);
            body.put("email", email);
            if(pass.length() != 0)
                body.put("password", pass);
            Call<Void> call = api.updateUserAccount(user.getID(), body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful() && response.code()==200) {
                        user.setName(name);
                        user.setUserEmail(email);
                        Toast.makeText(AccountSettingsActivity.this, "update success", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        Toast.makeText(AccountSettingsActivity.this, "update failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AccountSettingsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void sign_out(View v) {
        finish();
        ApplicationController.getInstance().setUser(null);

        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void unregister(View v) {
        final OrzApi api = ApplicationController.getInstance().getApi();
        final User user = ApplicationController.getInstance().getUser();

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure to unregister?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call = api.deleteUserAccount(user.getID());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() && response.code()==200) {
                            Toast.makeText(AccountSettingsActivity.this, "successfully unregistered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AccountSettingsActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(AccountSettingsActivity.this, "unregister failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AccountSettingsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        adb.setNegativeButton("no", null);
        adb.show();
    }

    public boolean checkPassword(String pass, String verify) {
        return pass.equals(verify);
    }

    public boolean checkEmail(String email) {
        return email.contains("@");
    }
}
