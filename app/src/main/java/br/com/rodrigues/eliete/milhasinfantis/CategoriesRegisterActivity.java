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

import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/17/16.
 */
public class CategoriesRegisterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.descEditText)
    EditText descCategories;
    @Bind(R.id.text_input_layout_cat_desc)
    TextInputLayout textInputLayoutDesc;

    private CategoriesDAO categoriesDAO;
    private Categories categories;
    private String type;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories_register);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.lightPrimaryColor));

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
            getSupportActionBar().setTitle(getResources().getString(R.string.categories));
        }else if (type.equals(CategoriesActivity.EDITION_ACTION)){
            getSupportActionBar().setTitle(getResources().getString(R.string.categories_edit));
        }

        populateCategory();
        Utils.cleanErrorMessage(descCategories, textInputLayoutDesc);
        textInputLayoutDesc.setErrorEnabled(true);

    }

    public void populateCategory(){
        categoriesDAO = new CategoriesDAO(this);
        if (id > 0){
            categories = categoriesDAO.fetchCategoryPerId(id);
            if(categories != null){
                descCategories.setText(categories.getDescription());
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
                registerCategories();
                return true;
            case R.id.action_cancel:
                intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerCategories() {
        Intent intent;
        String description = descCategories.getText().toString();

        if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else {
            boolean sucess = false;

            if (type.equals(CategoriesActivity.EDITION_ACTION)) {
                sucess = categoriesDAO.updateCategory(categories.getId(), description);
                if (!sucess){
                    showSnackBar(getResources().getString(R.string.snack_no_edition));
                }
                startActivity(new Intent(this, CategoriesActivity.class));
            }else{
                if (!verifyRegistration(description)){
                    categories = new Categories(description);
                    sucess = categoriesDAO.insertCategory(categories);

                    if (sucess) {
                        if(type.equals(CategoriesActivity.GOAL_ACTION)){
                            intent = new Intent(this, GoalsRegisterActivity.class);
                            intent.putExtra(CategoriesActivity.TYPE, CategoriesActivity.GOAL_ACTION);
                            intent.putExtra(CategoriesActivity.ID, 0);
                            intent.putExtra(GoalsActivity.SPINNER_POSITION, 0);
                            startActivity(intent);
                        }else if (type.equals(CategoriesActivity.REGISTRATION_ACTION)){
                            startActivity(new Intent(this, CategoriesActivity.class));
                        }
                    }else{
                        showSnackBar(getResources().getString(R.string.snack_no_add));
                    }
                }else{
                    showSnackBar("Categoria " + getResources().getString(R.string.snack_same));
                }
            }

            descCategories.setText(" ");
        }
    }

    public void showSnackBar(String message){
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String description){
        List<Categories> c = categoriesDAO.fetchCategoriesList();

        if (c != null){
            for (int i = 0; i < c.size(); i++){
                Categories cat = c.get(i);
                if (cat.getDescription().equals(description)){
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
