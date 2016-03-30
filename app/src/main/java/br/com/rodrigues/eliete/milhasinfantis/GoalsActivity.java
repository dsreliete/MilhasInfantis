package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.GoalsListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsAssociationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.AlertDialogFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/22/16.
 */
public class GoalsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ActionMode.Callback {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.goals_recyclerView)
    RecyclerView goalsRecyclerView;
    @Bind(R.id.spinner_goals_category)
    Spinner goalsCategorySpinner;
    @Bind(R.id.no_goalText)
    TextView noGoalText;
    @Bind(R.id.cardView)
    CardView cardView;

    public static final String REGISTRATION_ACTION = "registration";
    public static final String EDITION_ACTION = "edition";
    public static final String TYPE = "";
    public static final String ID = "id";
    public static final String SPINNER_POSITION = "spinnerPosition";

    private GoalsDAO goalsDAO;
    private CategoriesDAO categoriesDAO;
    private ChildrenDAO childrenDAO;
    private GoalsAssociationDAO goalsAssociationDAO;
    private List<Goals> goalsList;
    private GoalsListAdapter goalsListAdapter;
    private List<Categories> categoriesList;
    private Categories category;
    private int categoryId;
    private Goals goals;
    private ActionMode actionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.goals));

        goalsDAO = new GoalsDAO(this);
        categoriesDAO = new CategoriesDAO(this);
        goalsAssociationDAO = new GoalsAssociationDAO(this);
        childrenDAO = new ChildrenDAO(this);

        categoriesList = categoriesDAO.fetchCategoriesList();
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setOnItemSelectedListener(this);

        if(savedInstanceState != null){
            categoryId = savedInstanceState.getInt(ID);
        }else{
            Intent intent = getIntent();
            if(intent != null){
                categoryId = intent.getExtras().getInt(ID);
                if(categoryId > 0){
                    goalsCategorySpinner.setSelection(categoryId - 1);
                }else{
                    goalsCategorySpinner.setSelection(categoriesList.size() - 1);
                }
            }
        }

        cardView.setVisibility(View.GONE);
        noGoalText.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.action_plus_goals) {
            intent = new Intent(this, GoalsRegisterActivity.class);
            intent.putExtra(TYPE, REGISTRATION_ACTION);
            intent.putExtra(SPINNER_POSITION, goalsCategorySpinner.getSelectedItemPosition());
            intent.putExtra(ID, category.getId());
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = categoriesList.get(position);
        goalsList = goalsDAO.fetchGoalsListPerCategory(category.getId());
        if(goalsList != null){

            if(goalsList.size() == 0 && category.getId() != 1000){
                cardView.setVisibility(View.VISIBLE);
                noGoalText.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.GONE);
                noGoalText.setVisibility(View.GONE);
            }
            goalsListAdapter = new GoalsListAdapter(goalsList);
            goalsRecyclerView.setAdapter(goalsListAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            goalsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            goalsRecyclerView.setLayoutManager(layoutManager);
            goalsRecyclerView.setHasFixedSize(true);
            goalsRecyclerView.addOnItemTouchListener(RecycleTouchListener);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    private RecyclerView.OnItemTouchListener RecycleTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = goalsRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null){
                final int position = goalsRecyclerView.getChildAdapterPosition(child);
                goals = goalsList.get(position);

                if (actionMode != null) {
                    return false;
                }

                actionMode = startActionMode(GoalsActivity.this);
                myToggleSelection(position);
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    };

    public int verifyChildAssociated(int idActivity, int idCat){
        List<Associates> associatesList = goalsAssociationDAO.fetchAssociatesListPerCategory(idCat);
        if(associatesList.size() > 0){
            for (Associates a : associatesList){
                if(a.getIdActivity() == idActivity)
                    return a.getIdChildren();
            }
        }
        return 0;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ID, categoryId);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_deletion, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        List<Integer> selectedItemPositions;
        int currPos;
        switch (item.getItemId()) {
            case R.id.menu_delete:
                selectedItemPositions = goalsListAdapter.getSelectedItems();
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);
                    int idChild = verifyChildAssociated(goals.getId(), goals.getCatId());
                    Children child = childrenDAO.fetchChildrenPerId(idChild);
                    if(child != null) {
                        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance
                                (getResources().getString(R.string.alert_goal_associated) + " " + child.getName(),
                                        "OK");
                        alertDialogFragment.show(getSupportFragmentManager(), "alert");
                    }else {
                        if (goalsDAO.deleteGoalId(goals.getId())) {
                            goalsListAdapter.removeItem(currPos);
                            mode.finish();
                        }
                    }

                }
                return true;
            case R.id.menu_edit:
                Intent i = new Intent(this, GoalsRegisterActivity.class);
                i.putExtra(TYPE, EDITION_ACTION);
                i.putExtra(ID, goals.getId());
                i.putExtra(SPINNER_POSITION, goalsCategorySpinner.getSelectedItemPosition());
                mode.finish();
                startActivity(i);
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.actionMode = null;
        goalsListAdapter.clearSelections();}

    private void myToggleSelection(int pos) {
        goalsListAdapter.toggleSelection(pos);
    }

}
