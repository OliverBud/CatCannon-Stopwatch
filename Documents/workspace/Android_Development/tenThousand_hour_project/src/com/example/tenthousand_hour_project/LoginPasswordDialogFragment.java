/*
 * Dialogue that apperas when User enters the wrong password
 * at login
 */
package com.example.tenthousand_hour_project;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("NewApi")
public class LoginPasswordDialogFragment extends DialogFragment {
    
	/* "The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it."
     */
    public interface NoticeDialogListener {
        public void onDialogPasswordOkClick(DialogFragment dialog);
    }
    
    int type = 1;
    
    //method returns new instance of the dialogue
    static LoginPasswordDialogFragment newInstance() {
        return new LoginPasswordDialogFragment();
    }
    
    // "Use this instance of the interface to deliver action events"
    NoticeDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
    
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        //set the message to display, onClick listener, and actuon to take upon click
        builder.setMessage(R.string.password_mismatch)
               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   //calls this function from the class that instantiated the dialog
                	   mListener.onDialogPasswordOkClick(LoginPasswordDialogFragment.this);                   }
               });
               
        // Create the AlertDialog object and return it
        return builder.create();
    }
}