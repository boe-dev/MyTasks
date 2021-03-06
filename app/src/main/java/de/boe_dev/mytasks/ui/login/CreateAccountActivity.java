package de.boe_dev.mytasks.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.BaseActivity;
import model.User;
import utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class CreateAccountActivity extends BaseActivity {

    private static final String LOG_TAG = "CreateAccountActivity";
    private EditText eMailText, nameText;
    private Button createAccount;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initViews();
    }

    private void initViews() {
        nameText = (EditText) findViewById(R.id.create_account_edit_text_name);
        eMailText = (EditText) findViewById(R.id.create_account_edit_text_mail);
        createAccount = (Button) findViewById(R.id.create_account_button);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_check_inbox));
        mAuthProgressDialog.setCancelable(false);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameText.getText().toString().equals("")) {
                    nameText.setError(getResources().getString(R.string.no_name_entered));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(eMailText.getText().toString()).matches()) {

                    eMailText.setError(String.format(getString(R.string.error_invalid_email_not_valid), eMailText.getText().toString()));

                } else {

                    mAuthProgressDialog.show();
                    SecureRandom r = new SecureRandom();
                    String throwAwayPassword = new BigInteger(130, r).toString(32);
                    final Firebase ref = new Firebase(Constants.FIREBASE_URL);
                    ref.createUser(eMailText.getText().toString(), throwAwayPassword,
                            new Firebase.ValueResultHandler<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> stringObjectMap) {

                                    ref.resetPassword(eMailText.getText().toString(), new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            mAuthProgressDialog.dismiss();
                                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CreateAccountActivity.this);
                                            SharedPreferences.Editor sep = sp.edit();
                                            sep.putString(Constants.KEY_SIGNUP_EMAIL, eMailText.getText().toString()).apply();
                                            createUserInFirebaseHelper();
                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            Log.e(LOG_TAG, firebaseError.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    mAuthProgressDialog.dismiss();
                                    if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                                        eMailText.setError(getString(R.string.error_email_taken));
                                    } else {
                                        Log.e(LOG_TAG, firebaseError.getMessage());
                                    }
                                }
                            });

                }
            }
        });
    }

    private void createUserInFirebaseHelper() {

        final String mUserEmail = eMailText.getText().toString().replace(".", ",");
        final Firebase ref = new Firebase(Constants.FIREBASE_URL_USERS).child(mUserEmail);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                    User user = new User(nameText.getText().toString(), mUserEmail, timestampJoined);
                    ref.setValue(user);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, firebaseError.getMessage());
            }
        });
    }


}
