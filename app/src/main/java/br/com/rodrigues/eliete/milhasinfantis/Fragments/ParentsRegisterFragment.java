package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ParentsDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Parents;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 11/6/15.
 */
public class ParentsRegisterFragment extends BaseFragment {

    @Bind(R.id.nameEditText)
    EditText nameParents;
    @Bind(R.id.familyEditText) EditText familyParents;
    @Bind(R.id.text_input_layout_name)
    TextInputLayout textInputLayoutName;
    @Bind(R.id.text_input_layout_family)
    TextInputLayout textInputLayoutFamily;

    private Parents parents;
    private ParentsDAO parentsDAO;
    private String type;

    public static ParentsRegisterFragment newInstance(String type) {
        ParentsRegisterFragment fragment = new ParentsRegisterFragment();
        Bundle b = new Bundle();
        b.putString("TYPE", type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_parents, container, false);
        ButterKnife.bind(this, rootView);

        parentsDAO = new ParentsDAO(getActivity());

        if(getArguments() != null){
            type = getArguments().getString("TYPE");
            if (type.equals("EDITION")){
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.parents_edit));

                parents = parentsDAO.consultarParents();
                if(parents != null){
                    nameParents.setText(parents.getName());
                    familyParents.setText(parents.getNomeFamilia());
                }
            }else if (type.equals("REGISTRATION")){
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.parents_add));
            }
        }


        Utils.cleanErrorMessage(nameParents, textInputLayoutName);
        Utils.cleanErrorMessage(familyParents, textInputLayoutFamily);
        textInputLayoutName.setErrorEnabled(true);
        textInputLayoutFamily.setErrorEnabled(true);

        return rootView;
    }

    @OnClick(R.id.ok_reg) public void registerParent() {
        String name = nameParents.getText().toString();
        String family = familyParents.getText().toString();

        //VALIDATIONS
        if (name.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutName, getResources().getString(R.string.name));
        } else if (!Utils.validateText(name)) {
            Utils.setTextMessage(textInputLayoutName, getResources().getString(R.string.name));
        } else if (family.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutFamily, getResources().getString(R.string.family_name));
        }else if (!Utils.validateText(family)){
            Utils.setTextMessage(textInputLayoutFamily, getResources().getString(R.string.family_name));
        }else {
            boolean sucess = false;
            Intent i;
            String msg = "";

            if (type.equals("EDITION")) {
                sucess = parentsDAO.atualizar(parents.getId(), name, family);
                msg = getResources().getString(R.string.snack_no_edition);

            } else {
                if (!verifyRegistration(name)) {
                    parents = new Parents(name, family);
                    sucess = parentsDAO.inserir(parents);
                    msg = getResources().getString(R.string.snack_no_add);
                }
            }

            if (sucess) {
                i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            } else {
                showSnackBar(msg);
            }

            nameParents.setText(" ");
            familyParents.setText(" ");
        }
    }

    public boolean verifyRegistration(String name){
        Parents p = parentsDAO.consultarParents();

        if (p != null){
            if (p.getName().equals(name)){
                showSnackBar(name + " " + getResources().getString(R.string.snack_same));
                return true;
            }
        }
        return false;
    }

    public void showSnackBar(String message){
        Snackbar.make(getActivity().findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }


    @Override
    public String getTittle() {
        return "Responsável";
    }
}
