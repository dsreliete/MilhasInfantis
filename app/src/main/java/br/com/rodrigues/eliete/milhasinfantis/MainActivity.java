package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.ChildrenListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ParentsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Parents;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.settings)
    ImageView settingsNavView;
    @Bind(R.id.plus_navView)
    ImageView plusNavView;
    @Bind(R.id.name_textView)
    TextView nameTextView;
    @Bind(R.id.family_textView)
    TextView familyTextView;

    @Bind(R.id.child_recyclerView)
    RecyclerView childRecyclerView;
    @Bind(R.id.fab_add_children)
    FloatingActionButton fabAddChild;

    private ParentsDAO parentsDAO;
    private Parents p;
    private ChildrenListAdapter childrenListAdapter;
    private ChildrenDAO childrenDAO;
    private List<Children> childrenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setIcon(R.drawable.icon_action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        parentsDAO = new ParentsDAO(this);
        p = parentsDAO.fetchParents();

        if (p != null){
            StringBuilder sb = new StringBuilder(getResources().getString(R.string.parents_textview1));
            sb.append(" ");
            sb.append(p.getNameFamily());

            nameTextView.setText(p.getName());
            familyTextView.setText(sb);
            settingsNavView.setVisibility(View.VISIBLE);
            plusNavView.setVisibility(View.INVISIBLE);
        }else{
            nameTextView.setText(getResources().getString(R.string.parents_textview2));
            familyTextView.setText(getResources().getString(R.string.parents_textview3));
            plusNavView.setVisibility(View.VISIBLE);
            settingsNavView.setVisibility(View.INVISIBLE);
        }

        childrenDAO = new ChildrenDAO(this);
        childrenList = childrenDAO.fetchChildrenList();
        if (childrenList != null && childrenList.size() > 0) {
            childrenListAdapter = new ChildrenListAdapter(childrenList);
            childRecyclerView.setAdapter(childrenListAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            childRecyclerView.setLayoutManager(layoutManager);
            childRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            childRecyclerView.setHasFixedSize(true);
            childRecyclerView.addOnItemTouchListener(this);
        }
    }

    @OnClick (R.id.settings) public void editParents(){
        initParentsActivity(false);
        mDrawerLayout.closeDrawers();
    }

    @OnClick (R.id.plus_navView) public void addParents() {
        initParentsActivity(true);
        mDrawerLayout.closeDrawers();
    }

    @OnClick (R.id.fab_add_children) public void addChild(View v){
        Intent i = new Intent(this, ChildrenRegisterActivity.class);
        i.putExtra(ChildrenRegisterActivity.ID, 0);
        startActivity(i);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        Intent intent = null;

        switch(menuItem.getItemId()) {
            case R.id.nav_init:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case R.id.nav_goals:
                intent = new Intent(this, GoalsActivity.class);
                intent.putExtra(GoalsActivity.ID, 0);
                break;
            case R.id.nav_penalties:
                intent = new Intent(this, PenaltiesActivity.class);
                break;
            case R.id.nav_categories:
                intent = new Intent(this, CategoriesActivity.class);
                break;
            case R.id.nav_awards:
                intent = new Intent(this, AwardsActivity.class);
                break;
            case R.id.nav_histories:
                intent = new Intent(this, HistoryActivity.class);
                intent.putExtra(HistoryActivity.TYPE, HistoryActivity.GENERAL_TYPE);
                intent.putExtra(HistoryActivity.ID, 0);
                intent.putExtra(HistoryActivity.NAME, "");
                break;
            case R.id.nav_graphs:
                intent = new Intent(this, GraphAllChildActivity.class);
                break;
            case R.id.nav_app:
                intent = new Intent(this, AboutAppActivity.class);
                break;
        }

        if (intent != null)
            startActivity(intent);

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initParentsActivity(boolean condition){
        Intent i = new Intent(this, ParentsActivity.class);
        if (condition){
            i.putExtra(ParentsActivity.TYPE, ParentsActivity.REGISTRATION_TYPE);
        }else{
            i.putExtra(ParentsActivity.TYPE, ParentsActivity.EDITION_TYPE);
        }
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }

        super.onBackPressed();

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = childRecyclerView.findChildViewUnder(e.getX(),e.getY());
        if(child!=null){
            int position = childRecyclerView.getChildAdapterPosition(child);

            final Children c = childrenList.get(position);
            Intent i = new Intent(this, ChildrenDetailActivity.class);
            i.putExtra(ChildrenDetailActivity.ID, c.getId());
            startActivity(i);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}


