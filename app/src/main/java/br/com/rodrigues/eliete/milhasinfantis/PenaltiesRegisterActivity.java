package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/23/16.
 */
public class PenaltiesRegisterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.descEditText)
    EditText descPenalties;
    @Bind(R.id.pointEditText) EditText point;
    @Bind(R.id.text_input_layout_penalty_desc)
    TextInputLayout textInputLayoutDesc;
    @Bind(R.id.text_input_layout_penalty_point)
    TextInputLayout textInputLayoutPoint;

    private PenaltiesDAO penaltiesDAO;
    private Penalties penalties;
    private String type;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalties_register);

        ButterKnife.bind(this);

        penaltiesDAO = new PenaltiesDAO(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.lightPrimaryColor));

        if (savedInstanceState != null){
            type = savedInstanceState.getString(PenaltiesActivity.TYPE);
            id = savedInstanceState.getInt(PenaltiesActivity.ID);
        }else{
            Intent i = getIntent();
            if(i != null){
                type = i.getExtras().getString(PenaltiesActivity.TYPE);
                id = i.getExtras().getInt(PenaltiesActivity.ID);
            }
        }

        if (type.equals(PenaltiesActivity.REGISTRATION_ACTION)){
            getSupportActionBar().setTitle(getResources().getString(R.string.penalties));
        }else if (type.equals(PenaltiesActivity.EDITION_ACTION)){
            populatePenalties();
            getSupportActionBar().setTitle(getResources().getString(R.string.penalties_edit));
        }

        Utils.cleanErrorMessage(descPenalties, textInputLayoutDesc);
        Utils.cleanErrorMessage(point, textInputLayoutPoint);
        textInputLayoutDesc.setErrorEnabled(true);
        textInputLayoutPoint.setErrorEnabled(true);

    }

    private void populatePenalties() {
        if(id > 0) {
            penalties = penaltiesDAO.fetchPenaltyPerId(id);
            if (penalties != null) {
                descPenalties.setText(penalties.getDescription());
                point.setText(penalties.getPoint() + "");
            }
        }
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
                registerPenalties();
                return true;
            case R.id.action_cancel:
                intent = new Intent(this, PenaltiesActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerPenalties() {
        String description = descPenalties.getText().toString();
        String points = point.getText().toString();

        if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (points.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutPoint, getResources().getString(R.string.points_value));
        } else {
            boolean sucess = false;
            String msg = "";

            if (type.equals(PenaltiesActivity.EDITION_ACTION)){
                sucess = penaltiesDAO.updatePenalty(penalties.getId(), description, Integer.parseInt(points));
                msg = getResources().getString(R.string.snack_no_edition);
            }else if (type.equals(PenaltiesActivity.REGISTRATION_ACTION)){
                if (!verifyRegistration(description)){
                    int penaltyPoint = Integer.parseInt(points);
                    penaltyPoint *= -1;
                    penalties = new Penalties(description, penaltyPoint);
                    sucess = penaltiesDAO.insertPenalty(penalties);
                    msg = getResources().getString(R.string.snack_no_add);
                }else{
                    showSnackBar("Penalidade " + getResources().getString(R.string.snack_same));
                }
            }

            if (sucess) {
                startActivity(new Intent(this, PenaltiesActivity.class));
            } else {
                showSnackBar(msg);
            }

        }
    }

    public void showSnackBar(String message) {
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String description){
        List<Penalties> p = penaltiesDAO.fetchPenaltiesList();

        if (p != null){
            for (int i = 0; i < p.size(); i++){
                Penalties penalties = p.get(i);
                if (penalties.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CategoriesActivity.ID, id);
        outState.putString(CategoriesActivity.TYPE, type);
    }


}
