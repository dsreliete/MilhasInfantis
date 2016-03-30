package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import br.com.rodrigues.eliete.milhasinfantis.R;

/**
 * Created by eliete on 3/23/16.
 */
public class AlertDialogFragment extends android.support.v4.app.DialogFragment {

    public static final String MESSAGE = "message";
    public static final String BUTTON = "buttonMessage";

    private String message;
    private String buttonMsg;

    public static AlertDialogFragment newInstance(String message, String button){
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle b = new Bundle();
        b.putString(MESSAGE, message);
        b.putString(BUTTON, button);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState != null){
            message = savedInstanceState.getString(MESSAGE);
            buttonMsg = savedInstanceState.getString(BUTTON);
        }else{
            if(getArguments() != null){
                message = getArguments().getString(MESSAGE);
                buttonMsg = getArguments().getString(BUTTON);
            }
        }

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());
        alert.setIcon(R.drawable.icon_action_bar);
        alert.setTitle("Milhas Infantis");
        alert.setMessage(message);
        alert.setPositiveButton(buttonMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return alert.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MESSAGE, message);
        outState.putString(BUTTON, buttonMsg);
    }
}
