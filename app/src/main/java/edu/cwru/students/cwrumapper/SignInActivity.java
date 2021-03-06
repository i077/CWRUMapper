package edu.cwru.students.cwrumapper;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;


/**
 * A login screen that offers login via email/password via Google, or guest sign-in with
 * no credentials other than time of stay.
 */
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        OnClickListener {

    private static final String TAG = "SignInActivity";
    public static final int SIGNIN_OK = 1;
    public static final int SIGNIN_FAILED = 2;

    //Request Code to make sure sign-in error validation is complete
    private static final int OUR_REQUEST_CODE = 49404;

    //core Google Play services Client
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    // A progress dialog to display when the user is connecting in
    // case there is a delay in any of the dialogs being ready.
    private ProgressDialog mConnectionProgressDialog;


    /**
     * Creates the Sign-in activity -- sets up buttons, loading dialog, etc.
     * @param savedInstanceState The instance to create (not important, as this does not vary).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_sign_in);

        // Connect buttons with OnClickListeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.guest_sign_in_button).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this /* Context */)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Configure the ProgressDialog (only shown if there is a delay between screens)
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Loading...");
    }


    /**
     * Runs when SignInActivity starts. Manages the actual sign-in process.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Go back to MainActivity, we're already signed in
            finish();
        }
    }


    /**
     * Sets what happens when each UI element is clicked.
     * @param v The view that you are currently looking at, switched between depending on case.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Different cases for each button that can be tapped.
            // Each case is accompanied by a log message and an activity result.
            case R.id.sign_in_button:
                Log.v(TAG, "Sign-in tapped");
                //Show dialog while signing in
                mConnectionProgressDialog.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, OUR_REQUEST_CODE);
                break;

            case R.id.guest_sign_in_button:
                Log.v(TAG, "Guest Sign-in tapped");
                // Alert user that guest sign-in is not fully implemented
                Snackbar.make(v, "Guest sign in not fully implemented yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                break;

            default:
                //Unknown button press, for debugging purposes
                Log.v(TAG, "Unknown button tapped");
        }
    }


    /**
     * Method that runs when connection goes wrong.
     * @param connectionResult The result passed from ConnectionResult(), only used if there is an
     *                         error.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Unresolvable error, api is unavailable
        Log.d(TAG,"onConnectionFailed:" + connectionResult);
    }


    /**
     * Pulls codes from the sign in intent/request and decides what to do with the sign in.
     * @param requestCode An integer used to gate sign-in.
     * @param responseCode Unimportant, an integer used to mark client response.
     * @param intent The Sign-in intent created to complete sign-in flow using google client.
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        Log.v(TAG, "ActivityResult:" + requestCode);

        if (requestCode == OUR_REQUEST_CODE) {
            //hide progress dialog
            mConnectionProgressDialog.dismiss();

            //Resolve intent into GoogleSignInResult
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            handleSignInResult(task);
        }
    }


    /**
     * Helper method to trigger retrieving the server auth code if we've signed in.
     * @param completedTask Task containing Google authentication info.
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                mConnectionProgressDialog.dismiss();
                Log.v(TAG, "Sign in successful! Account: " + account.getEmail());
                Intent signInData = new Intent();
                signInData.putExtra("name", account.getDisplayName());
                setResult(SIGNIN_OK, signInData);
                finish();
            }
        }
        catch (ApiException e) {
            Log.w(TAG, "Signing in failed, code=" + e.getStatusCode());
        }
        Log.w(TAG, "Signing in failed");
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}

