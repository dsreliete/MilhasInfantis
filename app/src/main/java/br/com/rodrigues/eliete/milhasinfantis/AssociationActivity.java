package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.GoalsAssociationListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsAssociationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/25/16.
 */
public class AssociationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.association_goals_recyclerView)
    RecyclerView assocGoalsRecyclerView;
    @Bind(R.id.spinner_goals_category)
    Spinner goalsCategorySpinner;
    @Bind(R.id.no_goalText)
    TextView noGoalText;
    @Bind(R.id.cardView)
    CardView cardView;
    @Bind(R.id.text)
    TextView textView;

    private List<Goals> goalsList;
    private List<Associates> associatesList;
    private List<Categories> categoriesList;
    private CategoriesDAO categoriesDAO;
    private GoalsDAO goalsDAO;
    private GoalsAssociationDAO goalsAssociationDAO;
    private GoalsAssociationListAdapter goalsAssociationListAdapter;
    private int idChild;
    private String nameChild;
    private Associates associates;
    private Goals goalsItem;
    private Categories categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association);

        ButterKnife.bind(this);

        if (savedInstanceState != null){
            idChild = savedInstanceState.getInt(ChildrenDetailActivity.ID);
            nameChild = savedInstanceState.getString(ChildrenDetailActivity.NAME);
        }else{
            if (getIntent() != null){
                idChild = getIntent().getExtras().getInt(ChildrenDetailActivity.ID);
                nameChild = getIntent().getExtras().getString(ChildrenDetailActivity.NAME);
            }
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameChild);

        goalsDAO = new GoalsDAO(this);
        categoriesDAO = new CategoriesDAO(this);
        goalsAssociationDAO = new GoalsAssociationDAO(this);

        categoriesList = categoriesDAO.fetchCategoriesList();
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setSelection(categoriesList.size() - 1);
        goalsCategorySpinner.setOnItemSelectedListener(this);

        cardView.setVisibility(View.GONE);
        noGoalText.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categories = (Categories) goalsCategorySpinner.getSelectedItem();
        textView.setVisibility(View.VISIBLE);
        goalsList = goalsDAO.fetchGoalsListPerCategory(categories.getId());
        associatesList = goalsAssociationDAO.fetchAssociatesListPerCategoryPerChild(categories.getId(), idChild);

        if(goalsList.size() == 0 && categories.getId() != 1000){
            cardView.setVisibility(View.VISIBLE);
            noGoalText.setVisibility(View.VISIBLE);
            noGoalText.setText(getResources().getString(R.string.association_no));
        }else {
            cardView.setVisibility(View.GONE);
            noGoalText.setVisibility(View.GONE);
        }

        goalsAssociationListAdapter = new GoalsAssociationListAdapter(goalsList, associatesList, onItemClickCallback);
        assocGoalsRecyclerView.setAdapter(goalsAssociationListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        assocGoalsRecyclerView.setLayoutManager(layoutManager);
        assocGoalsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        assocGoalsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public static class OnItemClickListener implements CompoundButton.OnCheckedChangeListener {
        private int position;
        private OnItemClickCallback onItemClickCallback;

        public OnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
            this.position = position;
            this.onItemClickCallback = onItemClickCallback;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onItemClickCallback.onItemClicked(buttonView, isChecked, position);
        }

        public interface OnItemClickCallback {
            void onItemClicked(View view, boolean isChecked, int position);
        }
    }

    //bonificate
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, boolean isChecked, int position) {
            goalsItem = goalsList.get(position);
            boolean sucess, association = false;
            if (isChecked){
                associates = new Associates(idChild, goalsItem.getId(), goalsItem.getCatId(), 1);
                sucess = goalsAssociationDAO.insertAssociation(associates);
                association = true;
            }else{
                associates = new Associates(idChild, goalsItem.getId(), goalsItem.getCatId(), 0);
                sucess = goalsAssociationDAO.deleteAssociationId(associates);
            }

            if (sucess && association)
                Toast.makeText(AssociationActivity.this, "Associação realizada com sucesso", Toast.LENGTH_SHORT).show();
            else if (sucess && !association){
                Toast.makeText(AssociationActivity.this, "Associação desfeita com sucesso", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AssociationActivity.this, "Operação não realizada, tente novamente", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ChildrenDetailActivity.ID, idChild);
        outState.putString(ChildrenDetailActivity.NAME, nameChild);
    }
}
