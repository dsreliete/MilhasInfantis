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
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.CategoriesListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.AlertDialogFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/17/16.
 */
public class CategoriesActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, ActionMode.Callback{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.cat_recyclerView)
    RecyclerView catRecyclerView;
    @Bind(R.id.no_catText)
    TextView noCatText;
    @Bind(R.id.cardView)
    CardView cardView;

    public static final String TYPE = "";
    public static final String ID = "id";
    public static final String REGISTRATION_ACTION = "registration";
    public static final String EDITION_ACTION = "edition";
    public static final String GOAL_ACTION = "goal";

    private CategoriesDAO categoriesDAO;
    private GoalsDAO goalsDAO;
    private List<Categories> categoriesList;
    private CategoriesListAdapter categoriesListAdapter;
    private ActionMode actionMode;
    private Categories category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.categories));

        categoriesDAO = new CategoriesDAO(this);
        goalsDAO = new GoalsDAO(this);

        categoriesList = categoriesDAO.fetchCategoriesList();

        if(categoriesList != null){
            if(categoriesList.size() == 0){
                cardView.setVisibility(View.VISIBLE);
                noCatText.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.GONE);
                noCatText.setVisibility(View.GONE);

                categoriesListAdapter = new CategoriesListAdapter(categoriesList);
                catRecyclerView.setAdapter(categoriesListAdapter);
                catRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                catRecyclerView.setLayoutManager(layoutManager);
                catRecyclerView.setHasFixedSize(true);
                catRecyclerView.addOnItemTouchListener(this);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_awards, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_plus_awds:
                Intent i = new Intent(this, CategoriesRegisterActivity.class);
                i.putExtra(TYPE, REGISTRATION_ACTION);
                i.putExtra(ID, 0);
                startActivity(i);
                return true;
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = catRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if(child!=null) {
            int position = catRecyclerView.getChildAdapterPosition(child);
            category = categoriesList.get(position);

            if (actionMode != null) {
                return false;
            }

            actionMode = startActionMode(CategoriesActivity.this);
            myToggleSelection(position);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

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
                selectedItemPositions = categoriesListAdapter.getSelectedItems();
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);

                    List<Goals> gList = goalsDAO.fetchGoalsListPerCategory(category.getId());
                    if (gList.size() == 0) {
                        if (categoriesDAO.deleteCategoryId(category.getId())) {
                            categoriesListAdapter.removeItem(currPos);
                            mode.finish();
                        }
                    }
                    else {
                        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance
                                ("Impossível deletar, há atividades associadas",
                                        getResources().getString(R.string.alert_button_cancel));
                        alertDialogFragment.show(getSupportFragmentManager(), "alert");
                    }
                }
                return true;
            case R.id.menu_edit:
                Intent i = new Intent(this, CategoriesRegisterActivity.class);
                i.putExtra(TYPE, EDITION_ACTION);
                i.putExtra(ID, category.getId());
                mode.finish();
                startActivity(i);
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.actionMode = null;
        categoriesListAdapter.clearSelections();
    }

    private void myToggleSelection(int pos) {
        categoriesListAdapter.toggleSelection(pos);
    }

}
