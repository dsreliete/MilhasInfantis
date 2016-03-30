package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import br.com.rodrigues.eliete.milhasinfantis.ChildrenDetailActivity;
import br.com.rodrigues.eliete.milhasinfantis.ChildrenEditionActivity;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.R;

/**
 * Created by eliete on 3/17/16.
 */
public class SettingsDialogFragment extends android.support.v4.app.DialogFragment {


    private ChildrenDAO childrenDAO;
    private Children children;
    private int idChildren;

    public static SettingsDialogFragment newInstance(int id){
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        Bundle b = new Bundle();
        b.putInt(ChildrenDetailActivity.ID, id);
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

        if (savedInstanceState != null){
            idChildren = savedInstanceState.getInt(ChildrenDetailActivity.ID);
        }else{
            if (getArguments() != null)
                idChildren = getArguments().getInt(ChildrenDetailActivity.ID);
        }


        if (idChildren > 0) {
            childrenDAO = new ChildrenDAO(getActivity());
            children = childrenDAO.fetchChildrenPerId(idChildren);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setIcon(R.drawable.icon_action_bar);
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.setMessage(getResources().getString(R.string.alert_option_choose));
        alert.setPositiveButton(getResources().getString(R.string.alert_button_edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), ChildrenEditionActivity.class);
                i.putExtra(ChildrenEditionActivity.ID, children.getId());
                startActivity(i);
            }
        });
        alert.setNeutralButton(getResources().getString(R.string.alert_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.alert_button_exclude), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (childrenDAO.deleteChildrenId(children.getId())) {
                    dialog.cancel();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    showSnackBar(getResources().getString(R.string.snack_del));
                }
            }
        });
        return alert.create();
    }

    public void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ChildrenDetailActivity.ID, idChildren);
    }
}
