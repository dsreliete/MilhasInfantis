package br.com.rodrigues.eliete.milhasinfantis;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.rodrigues.eliete.milhasinfantis.Dao.ParentsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.AboutAppFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.AwardsFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.BaseFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.CategoriesFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.ChildrenFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.GoalsFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.GraphFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.HistoryGeneralFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.ParentsRegisterFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.PenaltiesFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Parents;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private ParentsDAO parentsDAO;
    private Parents p;

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
        p = parentsDAO.consultarParents();

        if (p != null){
            nameTextView.setText(p.getName());
            familyTextView.setText(getResources().getString(R.string.parents_textview1) + " " + p.getNomeFamilia());
            settingsNavView.setVisibility(View.VISIBLE);
            plusNavView.setVisibility(View.INVISIBLE);
        }else{
            nameTextView.setText(getResources().getString(R.string.parents_textview2));
            familyTextView.setText(getResources().getString(R.string.parents_textview3));
            plusNavView.setVisibility(View.VISIBLE);
            settingsNavView.setVisibility(View.INVISIBLE);
        }

        initFragment(new ChildrenFragment());

    }

    @OnClick (R.id.settings) public void editParents(){
        initFragment(ParentsRegisterFragment.newInstance("EDITION"));
        mDrawerLayout.closeDrawers();
    }

    @OnClick (R.id.plus_navView) public void addParents() {
        initFragment(ParentsRegisterFragment.newInstance("REGISTRATION"));
        mDrawerLayout.closeDrawers();
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

        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.nav_init:
                fragment = new ChildrenFragment();
                break;
            case R.id.nav_goals:
                fragment = GoalsFragment.newInstance(0);
                break;
            case R.id.nav_penalties:
                fragment = new PenaltiesFragment();
                break;
            case R.id.nav_categories:
                fragment = new CategoriesFragment();
                break;
            case R.id.nav_awards:
                fragment = new AwardsFragment();
                break;
            case R.id.nav_histories:
                fragment = new HistoryGeneralFragment();
                break;
            case R.id.nav_graphs:
                fragment = new GraphFragment();
                break;
            case R.id.nav_app:
                fragment = new AboutAppFragment();
                break;
        }

        if(fragment != null) {
            initFragment(fragment);
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
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

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        BaseFragment currentFragment = (BaseFragment) this.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        getSupportActionBar().setTitle(currentFragment.getTittle());

        super.onBackPressed();

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack("back")
                .commit();
    }


}


