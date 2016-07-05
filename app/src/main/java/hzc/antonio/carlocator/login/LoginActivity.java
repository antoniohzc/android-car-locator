package hzc.antonio.carlocator.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.test.TestActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        navigateToMainScreen();
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(this, TestActivity.class));
    }
}
