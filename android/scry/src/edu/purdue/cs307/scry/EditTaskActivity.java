package edu.purdue.cs307.scry;

import java.nio.charset.Charset;
import android.annotation.TargetApi;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.fragments.TaskEditFragment;
import edu.purdue.cs307.scry.model.Task;

public class EditTaskActivity extends FragmentActivity implements
        TaskDatasourceActivity, CreateNdefMessageCallback {

    private TaskDataSource datasource;
    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.root_fragment);
	FragmentManager fm = getSupportFragmentManager();
	
	mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//	if (mNfcAdapter == null) {
//	    Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
//		    .show();
//	    finish();
//	    return;
//	}
//
	mNfcAdapter.setNdefPushMessageCallback(this, this);

	Task t = (Task) getIntent().getExtras().getParcelable("Task");

	TaskEditFragment editFrag = TaskEditFragment.newInstance(t);

	fm.beginTransaction().replace(R.id.mainlayout, editFrag)
	        .addToBackStack("TaskEditFragment").commit();

	datasource = new TaskDataSource(this.getApplicationContext());
	datasource.open();
    }

    @Override
    public TaskDataSource getDataSource() {
	return datasource;
    }

    @Override
    public void onResume() {
	datasource.open();
	super.onResume();
//	if (getIntent().getAction() != null)
//	    Log.v("Main", getIntent().getAction());
//	if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//	    processIntent(getIntent());
//	}
    }

    @Override
    public void onPause() {
	datasource.close();
	super.onPause();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent arg0) {
	String text;
	Task t = (Task) getIntent().getExtras().getParcelable("Task");
	text = t.getOwner() + ", " + t.toString() + ", " + t.getCategory()
	        + ", " + t.getLocation();
	NdefMessage msg = new NdefMessage(new NdefRecord[] {
	        NdefRecord.createMime(
	                "application/edu.purdue.cs307.scry.SigninActivity",
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
//
//    
//    void processIntent(Intent intent) {
//	Parcelable[] rawMsgs = intent
//	        .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//	NdefMessage msg = (NdefMessage) rawMsgs[0];
//	String s = new String(msg.getRecords()[0].getPayload());
//	Log.wtf("MSG", s);
//	String[] tokens = s.split("[, ]");
//
//	Task t = new Task();
//	t.title = tokens[1];
//	t.category = tokens[2];
//	t.lat_location = Double.parseDouble(tokens[3]);
//	t.long_location = Double.parseDouble(tokens[4]);
//	t.ownerId = tokens[0];
//
//	datasource.commitTask(t);
//    }

}
