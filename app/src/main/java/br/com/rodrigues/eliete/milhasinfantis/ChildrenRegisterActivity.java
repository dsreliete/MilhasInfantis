package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/16/16.
 */
public class ChildrenRegisterActivity extends ChildrenRegisterBaseActivity implements DatePickerDialog.OnDateSetListener {

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
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private Children child;
    private ChildrenDAO childDAO;
    private String date;
    private int id;
    public static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_register);

        ButterKnife.bind(this);

        childDAO = new ChildrenDAO(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.child_add));

        Intent it = getIntent();
        if(it != null) {
            id = it.getExtras().getInt(ID);
            if (id != 0) {
                child = childDAO.fetchChildrenPerId(id);
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
                DatePickerDialog dpd = DatePickerDialog.newInstance(ChildrenRegisterActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getFragmentManager(), null);
            }
        });


    }

    public void registerChild() {
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
            boolean sucess = false;
            boolean registration = verifyRegistration(name);

            if(!registration){
                child = new Children(name, date, sexId);
                sucess = childDAO.insertChild(child);
                String msg = getResources().getString(R.string.snack_no_add);

                if (sucess) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    showSnackBar(msg);
                }
            }else{
                showSnackBar(name + " " + getResources().getString(R.string.snack_same));
            }

            nameChild.setText(" ");
            dtNascChild.setText(" ");
            radioGroup.clearCheck();
        }
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String name){
        List<Children> childrenList = childDAO.fetchChildrenList();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            case R.id.action_done:
                registerChild();
                return true;
            case R.id.action_cancel:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
