package de.boe_dev.mytasks.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
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
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.BaseActivity;
import de.boe_dev.mytasks.ui.MainActivity;
import model.User;
import utils.Constants;
import utils.Utils;

/**
 * Created by benny on 03.05.16.
 */
public class LoginActivity extends BaseActivity{

    private static final String LOG_TAG = "LoginActivity";
    private ProgressDialog mAuthProgressDialog;
    private Firebase ref;
    private GoogleSignInAccount mGoogleAccount;
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

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor spe = sp.edit();

                if (authData.getProvider().equals(Constants.PASSWORD_PROVIDER)) {
                    setAuthenticatedUserPasswordProvieder(authData);
                } else if (authData.getProvider().equals(Constants.GOOGLE_PROVIDER)) {
                    setAuthenticatedUserGoogle(authData);
                }

                spe.putString(Constants.KEY_PROVIDER, authData.getProvider()).apply();
                spe.putString(Constants.KEY_ENCODED_EMAIL, mEncodedEmail).apply();

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

        private void setAuthenticatedUserPasswordProvieder(AuthData authData) {
            final String unprocessedEmail = authData.getProviderData().get(Constants.FIREBASE_PROPERTY_EMAIL).toString().toLowerCase();
            mEncodedEmail = Utils.encodeEmail(unprocessedEmail);
            final Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (!user.isHasLoggedInWithPassword()) {
                            ref.changePassword(unprocessedEmail, password_edit_text.getText().toString(),
                                    password_edit_text.getText().toString(), new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            userRef.child(Constants.FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD).setValue(true);
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            Log.d(LOG_TAG, firebaseError.getMessage());
                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        private void setAuthenticatedUserGoogle(AuthData authData) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor spe = sp.edit();
            String unproceddedEmail;
            if (mGoogleApiClient.isConnected()) {
                unproceddedEmail = mGoogleAccount.getEmail().toLowerCase();
                spe.putString(Constants.KEY_GOOGLE_EMAIL, unproceddedEmail).apply();
            } else {
                unproceddedEmail = sp.getString(Constants.KEY_GOOGLE_EMAIL, null);
            }

            mEncodedEmail = Utils.encodeEmail(unproceddedEmail);
            final String userName = (String) authData.getProviderData().get(Constants.PROVIDER_DATA_DISPLAY_NAME);
            final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);
            userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        HashMap<String, Object> timestampJoined = new HashMap<>();
                        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                        User user = new User(userName, mEncodedEmail, timestampJoined);
                        userLocation.setValue(user);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(LOG_TAG, firebaseError.getMessage());
                }
            });

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
                Toast.makeText(getApplicationContext(), R.string.sign_in_cancled, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_handling_sign_in + result.getStatus().getStatusMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spe = sp.edit();
        String signupEmail = sp.getString(Constants.KEY_SIGNUP_EMAIL, null);
        if (signupEmail != null) {
            mail_edit_text.setText(signupEmail);
            spe.putString(Constants.KEY_SIGNUP_EMAIL, null).apply();
        }
    }
}
