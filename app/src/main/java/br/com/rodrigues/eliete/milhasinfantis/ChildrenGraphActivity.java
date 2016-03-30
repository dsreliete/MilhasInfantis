package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import br.com.rodrigues.eliete.milhasinfantis.Fragments.ChildrenGraphFragmentBonPen;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.ChildrenGraphFragmentGoal;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.ChildrenGraphFragmentTotal;
import br.com.rodrigues.eliete.milhasinfantis.Widget.CircleImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/30/16.
 */
public class ChildrenGraphActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.total_image)
    CircleImageView totalImage;
    @Bind(R.id.bon_pen_image)
    CircleImageView bonPenImage;
    @Bind(R.id.goal_image)
    CircleImageView goalImage;

    private int idChild;
    private String nameChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        
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
        getSupportActionBar().setTitle("Gráficos");


        totalImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initFragment(ChildrenGraphFragmentTotal.newInstance(idChild));
                return true;
            }
        });

        bonPenImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initFragment(ChildrenGraphFragmentBonPen.newInstance(idChild));
                return true;
            }
        });

        goalImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initFragment(ChildrenGraphFragmentGoal.newInstance(idChild));
                return true;
            }
        });

        
    }

    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("back")
                .commit();
    }

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
