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
        int pass = passView.getText().toString().hashCode();

        if(checkLogin(id, pass)) {
            Toast.makeText(this, "signin success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), OrzMainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "signin failed", Toast.LENGTH_LONG).show();
    }

    public boolean checkLogin(String id, int pass) {
        if(pass == getPassword(id))
            return true;
        return false;
    }

    public int getPassword(String id) {
        if(id.equals("asdf"))
            return "asdf".hashCode();
        else
            return -1;
    }

    public void registerAccount(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
        startActivity(intent);
    }
}
