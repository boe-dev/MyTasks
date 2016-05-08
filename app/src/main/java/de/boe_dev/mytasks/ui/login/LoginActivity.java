package de.boe_dev.mytasks.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.MainActivity;

/**
 * Created by benny on 03.05.16.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_edit_text_mail) EditText mail_edit_text;
    @BindView(R.id.login_edit_text_password) EditText password_edit_text;
    @BindView(R.id.login_with_google_button) EditText google_login_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
    }

    @OnClick(R.id.login_button)
    public void login(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @OnClick(R.id.login_sign_up)
    public void signUp(View view){
        startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
    }
}
