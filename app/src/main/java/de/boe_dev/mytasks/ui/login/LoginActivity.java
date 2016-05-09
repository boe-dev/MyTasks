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
    
    private ProgressDialog mAuthProgressDialog;

    @BindView(R.id.login_edit_text_mail) EditText mail_edit_text;
    @BindView(R.id.login_edit_text_password) EditText password_edit_text;
    @BindView(R.id.login_with_google_button) EditText google_login_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
        
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.login_button)
    public void login(View view) {
        mAuthProgressDialog.show();
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        ref.authWithPassword(mail_edit_text.getText.toString, password_edit_text.getText().toString),
            new MyAuthResultHandler(Constants.PASSWORD_PROVIDER)});
    
        // startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @OnClick(R.id.login_sign_up)
    public void signUp(View view){
        startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
    }
    
    private class MyAuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public MyAuthResultHandler(String provider) {
            this.provider = provider;
        }

        /**
         * On successful authentication call setAuthenticatedUser if it was not already
         * called in
         */
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();
            Log.i(LOG_TAG, provider + " " + getString(R.string.log_message_auth_successful));
            if (authData != null) {
                /* Go to main activity */
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEditTextEmailInput.setError(getString(R.string.error_message_email_issue));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mEditTextPasswordInput.setError(firebaseError.getMessage());
                    break;
                case FirebaseError.NETWORK_ERROR:
                    showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                    break;
                default:
                    showErrorToast(firebaseError.toString());
            }
        }
    }
    
}
