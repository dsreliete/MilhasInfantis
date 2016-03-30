package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/17/16.
 */
public class AwardsRegisterActivity extends AppCompatActivity{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.descEditText)
    EditText descAwards;
    @Bind(R.id.pointsEditText) EditText pointsAwards;
    @Bind(R.id.text_input_layout_awds_desc)
    TextInputLayout textInputLayoutDesc;
    @Bind(R.id.text_input_layout_awds_points) TextInputLayout textInputLayoutPoints;

    private String type;
    private int id;
    private AwardsDAO awardsDAO;
    private Awards awards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards_register);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.lightPrimaryColor));

        awardsDAO = new AwardsDAO(this);

        if (savedInstanceState != null){
            type = savedInstanceState.getString(CategoriesActivity.TYPE);
            id = savedInstanceState.getInt(CategoriesActivity.ID);
        }else{
            Intent i = getIntent();
            if(i != null){
                type = i.getExtras().getString(CategoriesActivity.TYPE);
                id = i.getExtras().getInt(CategoriesActivity.ID);
            }
        }

        if (type.equals(CategoriesActivity.REGISTRATION_ACTION) || type.equals(CategoriesActivity.GOAL_ACTION)){
            getSupportActionBar().setTitle(getResources().getString(R.string.awards));
        }else if (type.equals(CategoriesActivity.EDITION_ACTION)){
            getSupportActionBar().setTitle(getResources().getString(R.string.awards_edit));
            populateAwards();
        }

        Utils.cleanErrorMessage(descAwards, textInputLayoutDesc);
        Utils.cleanErrorMessage(pointsAwards, textInputLayoutPoints);
        textInputLayoutDesc.setErrorEnabled(true);
        textInputLayoutPoints.setErrorEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            case R.id.action_done:
                registerAwards();
                return true;
            case R.id.action_cancel:
                intent = new Intent(this, AwardsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateAwards() {
        if (id > 0) {
            awards = awardsDAO.fetchAwardsPerId(id);
            if (awards != null) {
                descAwards.setText(awards.getDescription());
                pointsAwards.setText(awards.getPoints() + "");
            }
        }
    }

    public void registerAwards() {
        String description = descAwards.getText().toString();
        String points = pointsAwards.getText().toString();

        if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (points.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutPoints, getResources().getString(R.string.points_value));
        } else {
            boolean sucess = false;
            String msg = "";

            if (type.equals(AwardsActivity.EDITION_ACTION)) {
                sucess = awardsDAO.updateAward(awards.getId(), description, Integer.parseInt(points));
                msg = getResources().getString(R.string.snack_no_edition);
            }else if (type.equals(AwardsActivity.REGISTRATION_ACTION)){
                if(!verifyRegistration(description)){
                    awards = new Awards(description, Integer.parseInt(points));
                    sucess = awardsDAO.insertAwards(awards);
                    msg = getResources().getString(R.string.snack_no_add);
                }else{
                    showSnackBar("prêmio  " + getResources().getString(R.string.snack_same));
                }
            }

            if (sucess) {
                startActivity(new Intent(this, AwardsActivity.class));
            } else {
                showSnackBar(msg);
            }
            descAwards.setText(" ");
            pointsAwards.setText(" ");
        }
    }

    public void showSnackBar(String message){
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String description){
        List<Awards> awardsList = awardsDAO.fetchAwardsList();

        if (awardsList != null) {
            for (int i = 0; i < awardsList.size(); i++){
                Awards a = awardsList.get(i);
                if (a.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(AwardsActivity.ID, id);
        outState.putString(AwardsActivity.TYPE, type);
    }
}
