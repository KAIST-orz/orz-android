package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn(View v) {
        //Intent intent = new Intent(getApplicationContext(), );
        //startActivity(intent);
    }

    public void registerAccount(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
        startActivity(intent);
    }
}
