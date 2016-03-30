package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.rodrigues.eliete.milhasinfantis.Dao.ParentsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Parents;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/15/16.
 */
public class ParentsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nameEditText)
    EditText nameParents;
    @Bind(R.id.familyEditText)
    EditText familyParents;
    @Bind(R.id.text_input_layout_name)
    TextInputLayout textInputLayoutName;
    @Bind(R.id.text_input_layout_family)
    TextInputLayout textInputLayoutFamily;

    private Parents parents;
    private ParentsDAO parentsDAO;
    private String type;

    public static final String EDITION_TYPE = "edition";
    public static final String REGISTRATION_TYPE = "registration";
    public static final String TYPE = "type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);

        ButterKnife.bind(this);

        parentsDAO = new ParentsDAO(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null){
            type = intent.getStringExtra(TYPE);
        }

        if (type.equals(EDITION_TYPE)){
            getSupportActionBar().setTitle(getResources().getString(R.string.parents_edit));
            parents = parentsDAO.fetchParents();
            if(parents != null){
                nameParents.setText(parents.getName());
                familyParents.setText(parents.getNameFamily());
            }
        }else if (type.equals(REGISTRATION_TYPE)){
            getSupportActionBar().setTitle(getResources().getString(R.string.parents_add));
        }

        Utils.cleanErrorMessage(nameParents, textInputLayoutName);
        Utils.cleanErrorMessage(familyParents, textInputLayoutFamily);
        textInputLayoutName.setErrorEnabled(true);
        textInputLayoutFamily.setErrorEnabled(true);
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
                registerParent();
                return true;
            case R.id.action_cancel:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerParent() {
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

            if (type.equals(EDITION_TYPE)) {
                sucess = parentsDAO.updateParent(parents.getId(), name, family);
                msg = getResources().getString(R.string.snack_no_edition);

            } else {
                if (!verifyRegistration(name)) {
                    parents = new Parents(name, family);
                    sucess = parentsDAO.insertParents(parents);
                    msg = getResources().getString(R.string.snack_no_add);
                }
            }

            if (sucess) {
                i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                showSnackBar(msg);
            }

            nameParents.setText(" ");
            familyParents.setText(" ");
        }
    }

    public boolean verifyRegistration(String name){
        Parents p = parentsDAO.fetchParents();

        if (p != null){
            if (p.getName().equals(name)){
                showSnackBar(name + " " + getResources().getString(R.string.snack_same));
                return true;
            }
        }
        return false;
    }

    public void showSnackBar(String message){
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

}
