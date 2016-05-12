package de.boe_dev.mytasks.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.MainActivity;
import utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final String LOG_TAG = "LoginActivity";
    private ProgressDialog mAuthProgressDialog;
    private Firebase ref;
    private GoogleSignInAccount mGoogleAccount;
    private GoogleApiClient mGoogleApiClient;
    private boolean mGoogleIntentInProgress;
    public static final int RC_GOOGLE_LOGIN = 1;

    @BindView(R.id.login_edit_text_mail) EditText mail_edit_text;
    @BindView(R.id.login_edit_text_password) EditText password_edit_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ref = new Firebase(Constants.FIREBASE_URL);
        
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.login_button)
    public void login(View view) {
        mAuthProgressDialog.show();
        ref.authWithPassword(mail_edit_text.getText().toString(), password_edit_text.getText().toString(),
            new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));
    
        // startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @OnClick(R.id.login_with_google_button)
    public void googleLogin(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
        mAuthProgressDialog.show();
    }

    @OnClick(R.id.login_sign_up)
    public void signUp(View view){
        startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
    }

    private void loginWithGoogle(String token) {
        ref.authWithOAuthToken(Constants.GOOGLE_PROVIDER, token, new MyAuthResultHandler(Constants.GOOGLE_PROVIDER));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
            Log.i(LOG_TAG, provider + " auth successful");
            if (authData != null) {

                Log.v(LOG_TAG, "Uid " + authData.getUid());
                Log.v(LOG_TAG, "email " + authData.getProviderData().get("email"));

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
                    mail_edit_text.setError(getString(R.string.error_message_email_issue));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    password_edit_text.setError(firebaseError.getMessage());
                    break;
                case FirebaseError.NETWORK_ERROR:
                    Toast.makeText(LoginActivity.this, R.string.error_message_failed_sign_in_no_network, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, firebaseError.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...); */
        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            /* Signed in successfully, get the OAuth token */
            mGoogleAccount = result.getSignInAccount();
            getGoogleOAuthTokenAndLogin();


        } else {
            if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                Toast.makeText(getApplicationContext(), "The sign in was cancelled. Make sure you're connected to the internet and try again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error handling the sign in: " + result.getStatus().getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            mAuthProgressDialog.dismiss();
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String mErrorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format(getString(R.string.oauth2_format), new Scope(Scopes.PROFILE)) + " email";

                    token = GoogleAuthUtil.getToken(LoginActivity.this, mGoogleAccount.getEmail(), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(LOG_TAG, getString(R.string.google_error_auth_with_google) + transientEx);
                    mErrorMessage = getString(R.string.google_error_network_error) + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(LOG_TAG, getString(R.string.google_error_recoverable_oauth_error) + e.toString());

                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(LOG_TAG, " " + authEx.getMessage(), authEx);
                    mErrorMessage = getString(R.string.google_error_auth_with_google) + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mAuthProgressDialog.dismiss();
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    loginWithGoogle(token);
                } else if (mErrorMessage != null) {
                    Toast.makeText(getApplicationContext(), mErrorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute();
    }

}
