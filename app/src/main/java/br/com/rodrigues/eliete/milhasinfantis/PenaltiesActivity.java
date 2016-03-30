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

import br.com.rodrigues.eliete.milhasinfantis.Adapters.PenaltiesListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/23/16.
 */
public class PenaltiesActivity extends AppCompatActivity implements ActionMode.Callback{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pen_recyclerView)
    RecyclerView penaltyRecyclerView;
    @Bind(R.id.cardView)
    CardView cardView;
    @Bind(R.id.no_penaltyText)
    TextView noPenaltyText;

    public static final String TYPE = "";
    public static final String ID = "id";
    public static final String REGISTRATION_ACTION = "registration";
    public static final String EDITION_ACTION = "edition";

    private PenaltiesDAO penaltiesDAO;
    private List<Penalties> penaltiesList;
    private PenaltiesListAdapter penaltiesListAdapter;
    private Penalties penalties;
    private ActionMode actionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalties);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.penalties));

        penaltiesDAO = new PenaltiesDAO(this);

        penaltiesList = penaltiesDAO.fetchPenaltiesList();
        if(penaltiesList != null){

            if(penaltiesList.size() == 0){
                cardView.setVisibility(View.VISIBLE);
                noPenaltyText.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.GONE);
                noPenaltyText.setVisibility(View.GONE);

                penaltiesListAdapter = new PenaltiesListAdapter(penaltiesList);
                penaltyRecyclerView.setAdapter(penaltiesListAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                penaltyRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
                penaltyRecyclerView.setLayoutManager(layoutManager);
                penaltyRecyclerView.setHasFixedSize(true);
                penaltyRecyclerView.addOnItemTouchListener(RecycleTouchListener);
            }
        }

    }

    private RecyclerView.OnItemTouchListener RecycleTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = penaltyRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null){
                int position = penaltyRecyclerView.getChildAdapterPosition(child);

               penalties = penaltiesList.get(position);

                if (actionMode != null) {
                    return false;
                }

                actionMode = startActionMode(PenaltiesActivity.this);
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
                Intent i = new Intent(this, PenaltiesRegisterActivity.class);
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
                selectedItemPositions = penaltiesListAdapter.getSelectedItems();
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);

                    if (penaltiesDAO.deletePenaltyId(penalties.getId())) {
                        penaltiesListAdapter.removeItem(currPos);
                    }
                }
                return true;
            case R.id.menu_edit:
                Intent i = new Intent(this, PenaltiesRegisterActivity.class);
                i.putExtra(TYPE, EDITION_ACTION);
                i.putExtra(ID, penalties.getId());
                startActivity(i);
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.actionMode = null;
        penaltiesListAdapter.clearSelections();
    }

    private void myToggleSelection(int pos) {
        penaltiesListAdapter.toggleSelection(pos);
    }
}
