package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.RescueAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.AlertDialogFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Model.Realizates;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/25/16.
 */
public class ChildrenRescuePointsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.child_image)
    ImageView childImageView;
    @Bind(R.id.total_poinstTextView)
    TextView totalPointsTextView;
    @Bind(R.id.child_name)
    TextView childNameTextView;
    @Bind(R.id.rescue_recyclerView)
    RecyclerView rescueRecyclerView;
    @Bind(R.id.no_awards)
    TextView noAwards;
    @Bind(R.id.cardView)
    CardView cardView;

    private ChildrenDAO childrenDAO;
    private RealizationDAO realizationDAO;
    private AwardsDAO awardsDAO;
    private int idChild;
    private String nameChild;
    private String genderChild;
    private int totalPoints;
    private String time;
    private String date;
    private RescueAdapter rescueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_children_rescue);
        ButterKnife.bind(this);

        if (savedInstanceState != null){
            idChild = savedInstanceState.getInt(ChildrenDetailActivity.ID);
            nameChild = savedInstanceState.getString(ChildrenDetailActivity.NAME);
            genderChild = savedInstanceState.getString(ChildrenDetailActivity.GENDER);
        }else{
            if (getIntent() != null){
                idChild = getIntent().getExtras().getInt(ChildrenDetailActivity.ID);
                nameChild = getIntent().getExtras().getString(ChildrenDetailActivity.NAME);
                genderChild = getIntent().getExtras().getString(ChildrenDetailActivity.GENDER);
            }
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameChild);


        if (genderChild.equals("F")) {
            childImageView.setImageResource(R.drawable.maria);
        } else if (genderChild.equals("M")) {
            childImageView.setImageResource(R.drawable.joao);
        } else {
            childImageView.setImageResource(R.drawable.joao);
        }

        childNameTextView.setText(nameChild);

        realizationDAO = new RealizationDAO(this);
        totalPoints = realizationDAO.fetchTotalPointsPerChild(idChild);
        totalPointsTextView.setText(totalPoints + " pontos");

        awardsDAO = new AwardsDAO(this);
        List<Awards> awardsList = awardsDAO.fetchAwardsList();
        if(awardsList.size() > 0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            rescueRecyclerView.setLayoutManager(layoutManager);
            rescueRecyclerView.setHasFixedSize(true);
            rescueRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            rescueAdapter = new RescueAdapter(ChildrenRescuePointsActivity.this, awardsList, onItemClickCallback, idChild);
            rescueRecyclerView.setAdapter(rescueAdapter);
            cardView.setVisibility(View.GONE);
            noAwards.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.VISIBLE);
            noAwards.setVisibility(View.VISIBLE);
        }

        time = Utils.getTimeNow();
        date = Utils.getTodayDate();
    }

    public static class OnItemClickListener implements View.OnClickListener {
        private int position;
        private OnItemClickCallback onItemClickCallback;
        private int childPoint;
        private int awardsPoint;
        private int awardId;


        public OnItemClickListener(int position, OnItemClickCallback onItemClickCallback, int childPoint, int awardsPoint, int awardId) {
            this.position = position;
            this.onItemClickCallback = onItemClickCallback;
            this.childPoint = childPoint;
            this.awardsPoint = awardsPoint;
            this.awardId = awardId;
        }

        @Override
        public void onClick(View view) {
            onItemClickCallback.onItemClicked(view, position, childPoint, awardsPoint, awardId);
        }

        public interface OnItemClickCallback {
            void onItemClicked(View view, int position, int childPoint, int awardsPoint, int awardId);
        }
    }

    //bonificate
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position, int childPoint, int awardsPoint, int awardId) {
            awardsPoint *= -1;
            boolean sucess;
            sucess = realizationDAO.insertRealization(idChild, awardId, 0, "premiar", awardsPoint, "azul", date, time);
            if(sucess){
                rescueAdapter.notifyDataSetChanged();
                totalPoints = realizationDAO.fetchTotalPointsPerChild(idChild);
                if(totalPoints == 1){
                    totalPointsTextView.setText(totalPoints + " ponto");
                }else{
                    totalPointsTextView.setText(totalPoints + " pontos");
                }
                List<Realizates> realizateList = realizationDAO.fetchRealizationPerChild(idChild);

                AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance
                        ("Objetivo alcançado. Parabéns!!!", "OK");
                alertDialogFragment.show(getSupportFragmentManager(), "alert");
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ChildrenDetailActivity.class);
                i.putExtra(ChildrenDetailActivity.ID, idChild);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
