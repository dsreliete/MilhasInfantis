package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 11/6/15.
 */
public class ChildrenRegisterFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener{


    @Bind(R.id.nameChildEditText)
    EditText nameChild;
    @Bind(R.id.dateNascChildEditText)
    EditText dtNascChild;
    @Bind(R.id.text_input_layout_name)
    TextInputLayout textInputLayoutName;
    @Bind(R.id.text_input_layout_dtNasc)
    TextInputLayout textInputLayoutdtNasc;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.men)
    RadioButton men;
    @Bind(R.id.women) RadioButton women;
    @Bind(R.id.children_ok)
    Button childrenOk;

    private Children child;
    private ChildrenDAO childDAO;
    private String date;
    private String type;
    private int id;

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    public static ChildrenRegisterFragment newInstance(String type, int id) {
        ChildrenRegisterFragment fragment = new ChildrenRegisterFragment();
        Bundle b = new Bundle();
        b.putString("TYPE", type);
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_children_register, container, false);

        ButterKnife.bind(this, rootView);

        childDAO = new ChildrenDAO(getActivity());

        if(getArguments() != null) {
            type = getArguments().getString("TYPE");
            if (getArguments().getInt("ID") != 0) {
                id = getArguments().getInt("ID");
                child = childDAO.consultarPorId(id);
            }

            if (type.equals("EDITION")) {
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.child_edit));

                if (child != null) {
                    nameChild.setText(child.getName());
                    date = child.getDataNasc();
                    dtNascChild.setText(Utils.formatDate(date));

                    if (child.getGender().equals("F")) women.setChecked(true);
                    else if (child.getGender().equals("M")) men.setChecked(true);
                    else men.setChecked(true);
                }

            } else if (type.equals("ADDITION")) {
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.child_add));
            }

        }

        textInputLayoutName.setErrorEnabled(true);
        textInputLayoutdtNasc.setErrorEnabled(true);
        Utils.cleanErrorMessage(nameChild, textInputLayoutName);
        Utils.cleanErrorMessage(dtNascChild, textInputLayoutdtNasc);

        dtNascChild.setFocusableInTouchMode(false);
        dtNascChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ChildrenRegisterFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });


        return rootView;
    }

    @Override
    public String getTittle() {
        return "Filhos";
    }

    @OnClick(R.id.children_ok) public void registerChild() {
        String name = nameChild.getText().toString();
        String sexId = "";
        switch (radioGroup.getCheckedRadioButtonId()) {
            case (R.id.men):
                sexId = "M";
                break;
            case (R.id.women):
                sexId = "F";
                break;
        }

        if (name.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutName, getResources().getString(R.string.name));
        } else if (!Utils.validateText(name)){
            Utils.setTextMessage(textInputLayoutName, getResources().getString(R.string.name));
        } else if (dtNascChild.getText().length() == 0){
            Utils.setEmptyMessage(textInputLayoutdtNasc, getResources().getString(R.string.birth_date));
        } else {
            boolean sucess= false;
            Intent i;
            String msg = "";

            boolean registration = verifyRegistration(name);

            if (type.equals("EDITION")) {
                sucess = childDAO.atualizar(child.getId(), name, date, sexId);
                msg = getResources().getString(R.string.snack_no_edition);

                if (sucess) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, ChildrenDetailsFragment.newInstance(id), FRAGMENT_TAG)
                            .addToBackStack("back")
                            .commit();
                } else {
                    showSnackBar(msg);
                }

            } else {
                if(!registration){
                    child = new Children(name, date, sexId);
                    sucess = childDAO.inserir(child);
                    msg = getResources().getString(R.string.snack_no_add);

                    if (sucess) {
                        i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                    } else {
                        showSnackBar(msg);
                    }
                }else{
                    showSnackBar(name + " " + getResources().getString(R.string.snack_same));
                }

            }

            nameChild.setText(" ");
            dtNascChild.setText(" ");
            radioGroup.clearCheck();
        }
    }

    public void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String name){
        List<Children> childrenList = childDAO.consultarChildrenList();

        if (childrenList != null) {
            for (int i = 0; i < childrenList.size(); i++){
                Children c = childrenList.get(i);
                if (c.getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String d = "";
        String m = "";
        if(day < 10) {
            d = "0" + day;
        }else{
            d = "" + day;
        }
        if(++month < 10){
            m = "0" + (month);
        }else{
            m = "" + (month);
        }

        date = Utils.concatDate(year, m, d);
        dtNascChild.setText(Utils.formatDate(date));
    }
}
