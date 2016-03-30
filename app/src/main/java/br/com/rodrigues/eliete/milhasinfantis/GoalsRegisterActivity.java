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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/22/16.
 */
public class GoalsRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.descEditText)
    EditText descGoals;
    @Bind(R.id.redPointEditText) EditText redPoint;
    @Bind(R.id.yellowPointEditText) EditText yellowPoint;
    @Bind(R.id.greenPointEditText) EditText greenPoint;
    @Bind(R.id.text_input_layout_goals_desc)
    TextInputLayout textInputLayoutDesc;
    @Bind(R.id.text_input_layout_goals_greenPoint) TextInputLayout textInputLayoutGreenPoint;
    @Bind(R.id.text_input_layout_goals_yellowPoint) TextInputLayout textInputLayoutYellowPoint;
    @Bind(R.id.text_input_layout_goals_redPoint) TextInputLayout textInputLayoutRedPoint;
    @Bind(R.id.spinner_goals_category)
    Spinner goalsCategorySpinner;
    @Bind(R.id.happy)
    ImageView happyImageView;
    @Bind(R.id.soso) ImageView sosoImageView;
    @Bind(R.id.angry) ImageView angryImageView;

    private GoalsDAO goalsDAO;
    private CategoriesDAO categoriesDAO;
    private Goals goals;
    private List<Categories> categoriesList;
    private ArrayAdapter<Categories> arrayAdapter;
    private int categoryId;
    private int spinnerPosition;
    private String type;
    private Categories category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_register);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.lightPrimaryColor));

        goalsDAO = new GoalsDAO(this);
        categoriesDAO = new CategoriesDAO(this);

        if(savedInstanceState != null){
            categoryId = savedInstanceState.getInt(GoalsActivity.ID);
            type = savedInstanceState.getString(GoalsActivity.TYPE);
            spinnerPosition = savedInstanceState.getInt(GoalsActivity.SPINNER_POSITION);
        }else {
            Intent intent = getIntent();
            if (intent != null) {
                categoryId = intent.getExtras().getInt(GoalsActivity.ID);
                spinnerPosition = intent.getExtras().getInt(GoalsActivity.SPINNER_POSITION);
                type = intent.getExtras().getString(GoalsActivity.TYPE);
            }
        }

        categoriesList = categoriesDAO.fetchCategoriesList();
        categoriesList.add(new Categories(999, getResources().getString(R.string.category_desc5)));
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));
        arrayAdapter = new ArrayAdapter<Categories>(this, android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setOnItemSelectedListener(GoalsRegisterActivity.this);

        if(categoryId == 1000){
            goalsCategorySpinner.setSelection(categoriesList.size() - 1); // selecione uma categoria
        }else if (categoryId == 0){
            goalsCategorySpinner.setSelection(categoriesList.size() - 3); // última categoria inserida
        }else{
            goalsCategorySpinner.setSelection(spinnerPosition);
        }

        if (type.equals(GoalsActivity.EDITION_ACTION)){
            getSupportActionBar().setTitle(getResources().getString(R.string.goals_edit));
            if(categoryId > 0){
               populateGoals();
            }
        }else if (type.equals(GoalsActivity.REGISTRATION_ACTION) || type.equals(CategoriesActivity.GOAL_ACTION)){
            getSupportActionBar().setTitle(getResources().getString(R.string.goals_add));
        }

        Utils.cleanErrorMessage(descGoals, textInputLayoutDesc);
        Utils.cleanErrorMessage(redPoint, textInputLayoutRedPoint);
        Utils.cleanErrorMessage(greenPoint, textInputLayoutGreenPoint);
        Utils.cleanErrorMessage(yellowPoint, textInputLayoutYellowPoint);
        textInputLayoutDesc.setErrorEnabled(true);
        textInputLayoutRedPoint.setErrorEnabled(true);
        textInputLayoutYellowPoint.setErrorEnabled(true);
        textInputLayoutGreenPoint.setErrorEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            case R.id.action_done:
                registerGoals();
                return true;
            case R.id.action_cancel:
                intent = new Intent(this, GoalsActivity.class);
                intent.putExtra(GoalsActivity.ID, goals.getId());
                return true;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void populateGoals(){
        goals = goalsDAO.fetchGoalPerId(categoryId);
        if(goals != null){
            descGoals.setText(goals.getDescription());
            String a = goals.getDescription();
            redPoint.setText(goals.getRedPoint() + "");
            greenPoint.setText(goals.getGreenPoint() + "");
            yellowPoint.setText(goals.getYellowPoint() + "");
        }
    }

    public void registerGoals() {
        String description = descGoals.getText().toString();
        String redPoints = redPoint.getText().toString();
        String yellowPoints = yellowPoint.getText().toString();
        String greenPoints = greenPoint.getText().toString();
        Intent intent = null;
        category = (Categories) goalsCategorySpinner.getSelectedItem();
        int catId = category.getId();

        if (catId == 1000) {
            showSnackBar(getResources().getString(R.string.category_desc6));
        } else if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (yellowPoints.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutYellowPoint, getResources().getString(R.string.points_value));
        } else if (redPoints.isEmpty()){
            Utils.setEmptyMessage(textInputLayoutRedPoint, getResources().getString(R.string.points_value));
        } else if (greenPoints.isEmpty()){
            Utils.setEmptyMessage(textInputLayoutGreenPoint, getResources().getString(R.string.points_value));
        } else {
            boolean sucess = false;
            String msg = "";

            if (type.equals(GoalsActivity.EDITION_ACTION)) {
                sucess = goalsDAO.updateGoal(goals.getId(), description, Integer.parseInt(redPoints), Integer.parseInt(yellowPoints), Integer.parseInt(greenPoints), catId);
                msg = getResources().getString(R.string.snack_no_edition);

            }else if (type.equals(GoalsActivity.REGISTRATION_ACTION) || type.equals(CategoriesActivity.GOAL_ACTION)){
                if (!verifyRegistration(description)){
                    goals = new Goals(description, Integer.parseInt(redPoints), Integer.parseInt(yellowPoints), Integer.parseInt(greenPoints), catId);
                    sucess = goalsDAO.insertGoals(goals);
                    msg = getResources().getString(R.string.snack_no_add);
                }else{
                    showSnackBar("Atividade " + getResources().getString(R.string.snack_same));
                }
            }

            if (sucess) {
                intent = new Intent(this, GoalsActivity.class);
                intent.putExtra(GoalsActivity.ID, catId);
                startActivity(intent);
            } else {
                showSnackBar(msg);
            }

            descGoals.setText(" ");
            redPoint.setText(" ");
            greenPoint.setText(" ");
            yellowPoint.setText(" ");
            goalsCategorySpinner.setSelection(categoriesList.size() - 1);
        }
    }

    public void showSnackBar(String message) {
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String description){
        List<Goals> g = goalsDAO.fetchGoalsListPerCategory(categoryId);

        if (g != null){
            for (Goals goal : g){
                if (goal.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Categories cat = (Categories) goalsCategorySpinner.getSelectedItem();
        int catId = cat.getId();
        if(catId == 999){
            descGoals.setVisibility(View.INVISIBLE);
            redPoint.setVisibility(View.INVISIBLE);
            yellowPoint.setVisibility(View.INVISIBLE);
            greenPoint.setVisibility(View.INVISIBLE);
            textInputLayoutDesc.setVisibility(View.INVISIBLE);
            textInputLayoutGreenPoint.setVisibility(View.INVISIBLE);
            textInputLayoutYellowPoint.setVisibility(View.INVISIBLE);
            textInputLayoutRedPoint.setVisibility(View.INVISIBLE);
            happyImageView.setVisibility(View.INVISIBLE);
            sosoImageView.setVisibility(View.INVISIBLE);
            angryImageView.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(this, CategoriesRegisterActivity.class);
            intent.putExtra(CategoriesActivity.TYPE, CategoriesActivity.GOAL_ACTION);
            intent.putExtra(CategoriesActivity.ID, 0);
            startActivity(intent);
        }else{
            descGoals.setVisibility(View.VISIBLE);
            redPoint.setVisibility(View.VISIBLE);
            yellowPoint.setVisibility(View.VISIBLE);
            greenPoint.setVisibility(View.VISIBLE);
            textInputLayoutDesc.setVisibility(View.VISIBLE);
            textInputLayoutGreenPoint.setVisibility(View.VISIBLE);
            textInputLayoutYellowPoint.setVisibility(View.VISIBLE);
            textInputLayoutRedPoint.setVisibility(View.VISIBLE);
            happyImageView.setVisibility(View.VISIBLE);
            sosoImageView.setVisibility(View.VISIBLE);
            angryImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(GoalsActivity.TYPE, type);
        outState.putInt(GoalsActivity.SPINNER_POSITION, spinnerPosition);
        outState.putInt(GoalsActivity.ID, categoryId);
    }
}
