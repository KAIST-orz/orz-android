package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        if(checkLogin(id, pass)) {
            Toast.makeText(this, "signin success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), OrzMainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "signin failed", Toast.LENGTH_LONG).show();
    }

    public boolean checkLogin(String id, String pass) {
        return pass.equals(getPassword(id));
    }

    public String getPassword(String id) {
        if(id.equals("asdf"))
            return "asdf";
        else
            return "zxcv";
    }

    public void registerAccount(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
        startActivity(intent);
    }
}
