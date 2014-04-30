package edu.purdue.cs307.scry.RottenTomatoes;

import edu.purdue.cs307.scry.MainActivity;
import edu.purdue.cs307.scry.R;
import edu.purdue.cs307.scry.model.Task;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RTErrorDialog extends DialogFragment {
	public final String errorMessage = "Rotten Tomatoes does not recognize this movie. " +
			                           "Please put the task in the form: 'watch <Movie Title>'";
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(errorMessage)
        	   .setTitle("Movie Error")
               .setPositiveButton("Ok" , new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //TODO: Close message
                   }
               })
               .setNegativeButton("Edit Task", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   //TODO: Go to edit task screen
                	   //((MainActivity) getActivity()).openEditForTask(t);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
