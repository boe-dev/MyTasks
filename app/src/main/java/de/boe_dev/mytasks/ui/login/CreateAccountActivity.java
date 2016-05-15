package de.boe_dev.mytasks.ui.login;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.boe_dev.mytasks.R;
import model.User;
import utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private EditText eMailText, passwordText, passwordCompareText, nameText;
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
        passwordText = (EditText) findViewById(R.id.create_account_edit_text_password);
        passwordCompareText = (EditText) findViewById(R.id.create_account_edit_text_compare_password);
        createAccount = (Button) findViewById(R.id.create_account_button);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameText.getText().toString().equals("")) {
                    nameText.setError(getResources().getString(R.string.no_name_entered));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(eMailText.getText().toString()).matches()) {

                    eMailText.setError(String.format(getString(R.string.error_invalid_email_not_valid), eMailText.getText().toString()));

                } else if(!passwordText.getText().toString().equals(passwordCompareText.getText().toString())) {

                    passwordText.setError(getResources().getString(R.string.error_password_does_not_match));
                    passwordCompareText.setError(getResources().getString(R.string.error_password_does_not_match));

                } else if (passwordText.getText().toString().length() < 6) {

                    passwordText.setError(getResources().getString(R.string.error_invalid_password_not_valid));

                } else {

                    mAuthProgressDialog.show();
                    Firebase ref = new Firebase(Constants.FIREBASE_URL);
                    ref.createUser(eMailText.getText().toString(), passwordText.getText().toString(),
                            new Firebase.ValueResultHandler<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> stringObjectMap) {
                                    mAuthProgressDialog.dismiss();
                                    String uid = (String) stringObjectMap.get("uid");
                                    createUserInFirebaseHelper(uid);
                                }

                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    mAuthProgressDialog.dismiss();
                                    if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                                        eMailText.setError(getString(R.string.error_email_taken));
                                    } else {
                                        Toast.makeText(CreateAccountActivity.this,
                                                firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void createUserInFirebaseHelper(String uid) {

        Firebase ref = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);

        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        User user = new User(nameText.getText().toString(), eMailText.getText().toString(), timestampJoined);
        ref.setValue(user);

    }


}
