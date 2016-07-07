package hzc.antonio.carlocator.login.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.login.LoginPresenter;
import hzc.antonio.carlocator.main.ui.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.txtEmail) EditText txtEmail;
    @BindView(R.id.txtPassword) EditText txtPassword;
    @BindView(R.id.btnSignin) Button btnSignin;
    @BindView(R.id.btnSignup) Button btnSignup;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.container) RelativeLayout container;

    private CarLocatorApp app;
    @Inject
    public SharedPreferences sharedPreferences;
    @Inject
    public LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        app = (CarLocatorApp) getApplication();
        setupInjection();
        presenter.onCreate();
        presenter.validateLogin(null, null);
    }

    private void setupInjection() {
        app.getLoginComponent(this).inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    private void setInputs(boolean enabled) {
        txtEmail.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        btnSignin.setEnabled(enabled);
        btnSignup.setEnabled(enabled);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    @OnClick(R.id.btnSignin)
    public void handleSignIn() {
        presenter.validateLogin(txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    @Override
    @OnClick(R.id.btnSignup)
    public void handleSignUp() {
        presenter.registerNewUser(txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginError(String error) {
        txtPassword.setText("");
        String strError = String.format(getString(R.string.login_error_message_signin), error);
        txtPassword.setError(strError);
    }

    @Override
    public void newUserSuccess() {
        Snackbar.make(container, R.string.login_notice_message_signup, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void newUserError(String error) {
        txtPassword.setText("");
        String strError = String.format(getString(R.string.login_error_message_signup), error);
        txtPassword.setError(strError);
    }

    @Override
    public void setUserEmail(String email) {
        if (email != null) {
            sharedPreferences.edit().putString(app.getSharedPrefEmailKey(), email).commit();
        }
    }
}
