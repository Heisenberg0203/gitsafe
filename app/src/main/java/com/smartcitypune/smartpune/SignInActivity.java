package com.smartcitypune.smartpune;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.text.BreakIterator;
import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignInActivity";

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());
    private TextView dummyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        dummyTextView = (TextView) findViewById(R.id.dummyTextView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
//                startActivity(SignedInActivity.createIntent(this, response));
                updateUI(mAuth.getCurrentUser());
//                finish();
            } else {
                if (response == null) {
                    dummyTextView.setText("User pressed back button");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    dummyTextView.setText("no_internet_connection");
                    return;
                }

                dummyTextView.setText("unknown_error");
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        } else {
            startSignIn();
        }

    }

    private void startSignIn() {
        // Build FirebaseUI sign in intent. For documentation on this operation and all
        // possible customization see: https://github.com/firebase/firebaseui-android
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(providers)
                .setLogo(R.drawable.man)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void signOut() {
//        mAuth.signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    public void onComplete(@NonNull Task<Void> task) {
//                         user is now signed out
//                        startActivity(new Intent(SignInActivity.this, SignInActivity.class));
//                        finish();
//                    }
//                });
    }

    private void updateUI(FirebaseUser user) {
        String dummyText = null;
        if (user != null) {
            FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                // The user is new
            } else {
                // This is an existing user
            }
            dummyText = user.getDisplayName()
                    + "\nEmail: " + user.getEmail()
                    + "\nName: " + user.getDisplayName()
                    + "\nPhoneNumber: " + user.getPhoneNumber()
                    + "\nProviderId: " + user.getProviderId()
                    + "\nUid: " + user.getUid()
                    + "\nMetaData: CR = " + user.getMetadata().getCreationTimestamp()+" LSI = "+user.getMetadata().getLastSignInTimestamp()
                    + "\nProviderId: " + user.getProviderId()
                    + "\n";

        } else {
            dummyText = "null object";
        }
        dummyTextView.setText(dummyText);
    }
}
