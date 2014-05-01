package edu.purdue.cs307.scry;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import edu.purdue.cs307.scry.model.Task;

public class SigninActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>,
        View.OnClickListener, CreateNdefMessageCallback {

    private static final String TAG = "android-plus-quickstart";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    // STATE_DEFAULT: The default state of the application before the user
    // has clicked 'sign in', or after they have clicked
    // 'sign out'. In this state we will not attempt to
    // resolve sign in errors and so will display our
    // Activity in a signed out state.
    // STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    // in', so resolve successive errors preventing sign in
    // until the user has successfully authorized an account
    // for our app.
    // STATE_IN_PROGRESS: This state indicates that we have started an intent to
    // resolve an error, and so we should not start further
    // intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play
    // services
    // until the user clicks 'sign in'.
    private int mSignInError;

    private SignInButton mSignInButton;

    NfcAdapter mNfcAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_signin);

	mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
	mSignInButton.setOnClickListener(this);

	if (savedInstanceState != null) {
	    mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS,
		    STATE_DEFAULT);
	}

	mGoogleApiClient = buildGoogleApiClient();

	Button bypass = (Button) findViewById(R.id.btn_bypass);
	bypass.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(SigninActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	    }

	});
	
	mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

	if (mNfcAdapter == null) {
	    Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
		    .show();
	    finish();
	    return;
	}

	mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    private GoogleApiClient buildGoogleApiClient() {
	// When we build the GoogleApiClient we specify where connected and
	// connection failed callbacks should be returned, which Google APIs our
	// app uses and which OAuth 2.0 scopes our app requests.
	return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this).addApi(Plus.API, null)
	        .addScope(Plus.SCOPE_PLUS_LOGIN)
	        .addScope(Plus.SCOPE_PLUS_PROFILE).build();
    }

    @Override
    protected void onStart() {
	super.onStart();
	mGoogleApiClient.connect();
    }
    
    @Override
    protected void onResume()
    {
	super.onResume();
	if (getIntent().getAction() != null)
	    Log.v("Main", getIntent().getAction());
	if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	    processIntent(getIntent());
	}
    }

    @Override
    protected void onStop() {
	super.onStop();

	if (mGoogleApiClient.isConnected()) {
	    mGoogleApiClient.disconnect();
	}
    }
    
    @Override
    public NdefMessage createNdefMessage(NfcEvent arg0) {
	String text;
	Task t = (Task) getIntent().getExtras().getParcelable("Task");
	text = t.getOwner() + ", " + t.toString() + ", " + t.getCategory()
	        + ", " + t.getLocation();
	NdefMessage msg = new NdefMessage(new NdefRecord[] {
	        NdefRecord.createMime(
	                "application/edu.purdue.cs307.scry.EditTaskActivity",
	                text.getBytes()),
	        NdefRecord.createApplicationRecord("edu.purdue.cs307.scry") });
	Log.wtf("NDEF MADE", t.toString());
	return msg;
    }

    @Override
    public void onNewIntent(Intent intent) {
	// onResume gets called after this to handle the intent
	setIntent(intent);
    }

    
    void processIntent(Intent intent) {
	Parcelable[] rawMsgs = intent
	        .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	NdefMessage msg = (NdefMessage) rawMsgs[0];
	String s = new String(msg.getRecords()[0].getPayload());
	Log.wtf("MSG", s);
	String[] tokens = s.split("[, ]");

	Task t = new Task();
	t.title = tokens[1];
	t.category = tokens[2];
	t.lat_location = Double.parseDouble(tokens[3]);
	t.long_location = Double.parseDouble(tokens[4]);
	t.ownerId = tokens[0];

	Log.d("NFC", "I got shit!"); 
	//datasource.commitTask(t);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
	if (!mGoogleApiClient.isConnecting()) {
	    // We only process button clicks when GoogleApiClient is not
	    // transitioning
	    // between connected and not connected.
	    switch (v.getId()) {
		case R.id.sign_in_button:
		    resolveSignInError();
		    break;
	    }
	}
    }

    @Override
    public void onConnected(Bundle connectionHint) {
	// Reaching onConnected means we consider the user signed in.
	Log.wtf(TAG, "SOMEBODY CONNECTED!!!!!!!!!!");

	// Update the user interface to reflect that the user is signed in.
	mSignInButton.setEnabled(false);

	// Retrieve some profile information to personalize our app for the
	// user.
	Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

	Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
	        this);
	;

	String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
	String displayName = currentUser.getDisplayName();
	String userID = currentUser.getId();
	Log.wtf("Display Name -------", displayName);
	Log.wtf("Account -------", email);
	Log.wtf("Used ID -------", userID);

	// Pushing info to shared preferences
	SharedPreferences settings = getSharedPreferences("pref_profile", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putString("userID", userID).commit();
	editor.putString("email", email).commit();
	editor.putString("name", displayName).commit();

	// Indicate that the sign in process is complete.
	mSignInProgress = STATE_DEFAULT;
	Intent i = new Intent(SigninActivity.this, MainActivity.class);
	startActivity(i);
	finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
	// Refer to the javadoc for ConnectionResult to see what error codes
	// might
	// be returned in onConnectionFailed.
	Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
	        + result.getErrorCode());

	if (mSignInProgress != STATE_IN_PROGRESS) {
	    // We do not have an intent in progress so we should store the
	    // latest
	    // error resolution intent for use when the sign in button is
	    // clicked.
	    mSignInIntent = result.getResolution();
	    mSignInError = result.getErrorCode();

	    if (mSignInProgress == STATE_SIGN_IN) {
		// STATE_SIGN_IN indicates the user already clicked the sign in
		// button
		// so we should continue processing errors until the user is
		// signed in
		// or they click cancel.
		resolveSignInError();
	    }
	}
	// In this sample we consider the user signed out whenever they do not
	// have
	// a connection to Google Play services.
	onSignedOut();
    }

    /*
     * Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in. This could be
     * a dialog allowing the user to select an account, an activity allowing the
     * user to consent to the permissions being requested by your app, a setting
     * to enable device networking, etc.
     */
    @SuppressWarnings("deprecation")
    private void resolveSignInError() {
	if (mSignInIntent != null) {
	    // We have an intent which will allow our user to sign in or
	    // resolve an error. For example if the user needs to
	    // select an account to sign in with, or if they need to consent
	    // to the permissions your app is requesting.

	    try {
		// Send the pending intent that we stored on the most recent
		// OnConnectionFailed callback. This will allow the user to
		// resolve the error currently preventing our connection to
		// Google Play services.
		mSignInProgress = STATE_IN_PROGRESS;
		startIntentSenderForResult(mSignInIntent.getIntentSender(),
		        RC_SIGN_IN, null, 0, 0, 0);
	    } catch (SendIntentException e) {
		Log.i(TAG,
		        "Sign in intent could not be sent: "
		                + e.getLocalizedMessage());
		// The intent was canceled before it was sent. Attempt to
		// connect to
		// get an updated ConnectionResult.
		mSignInProgress = STATE_SIGN_IN;
		mGoogleApiClient.connect();
	    }
	} else {
	    // Google Play services wasn't able to provide an intent for some
	    // error types, so we show the default Google Play services error
	    // dialog which may still start an intent on our behalf if the
	    // user can resolve the issue.
	    showDialog(DIALOG_PLAY_SERVICES_ERROR, null);
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	switch (requestCode) {
	    case RC_SIGN_IN:
		if (resultCode == RESULT_OK) {
		    // If the error resolution was successful we should continue
		    // processing errors.
		    mSignInProgress = STATE_SIGN_IN;
		} else {
		    // If the error resolution was not successful or the user
		    // canceled,
		    // we should stop processing errors.
		    mSignInProgress = STATE_DEFAULT;
		}

		if (!mGoogleApiClient.isConnecting()) {
		    // If Google Play services resolved the issue with a dialog then
		    // onStart is not called so we need to re-attempt connection
		    // here.
		    mGoogleApiClient.connect();
		}
		break;
	}
    }

    /* Go to create_task */
    @Override
    public void onResult(LoadPeopleResult peopleData) {
	if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
	    try {

	    } finally {

	    }

	} else {
	    Log.e(TAG, "Error loading create task page");
	}
    }

    private void onSignedOut() {
	// Update the UI to reflect that the user is signed out.
	mSignInButton.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int cause) {
	// The connection to Google Play services was lost for some reason.
	// We call connect() to attempt to re-establish the connection or get a
	// ConnectionResult that we can attempt to resolve.
	mGoogleApiClient.connect();
    }

}