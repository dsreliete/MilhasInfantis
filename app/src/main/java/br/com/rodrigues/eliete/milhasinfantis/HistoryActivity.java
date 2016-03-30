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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.HistoryAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Model.Realizates;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/25/16.
 */
public class HistoryActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.hist_general_recyclerView)
    RecyclerView history_recyclerView;
    @Bind(R.id.noAction)
    TextView noAction;
    @Bind(R.id.text) TextView text;
    @Bind(R.id.cardView)
    CardView cardView;

    private RealizationDAO realizationDAO;
    private PenaltiesDAO penaltiesDAO;
    private ChildrenDAO childrenDAO;
    private GoalsDAO goalsDAO;
    private AwardsDAO awardsDAO;
    private List<String> histList;
    private HistoryAdapter historyAdapter;
    private int idChild;
    private String nameChild;
    private String genderChild;
    private String type;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String GENERAL_TYPE = "general";
    public static final String CHILDREN_TYPE = "children";
    public static final String TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null){
            idChild = savedInstanceState.getInt(ID);
            nameChild = savedInstanceState.getString(NAME);
            genderChild = savedInstanceState.getString(GENDER);
            type = savedInstanceState.getString(TYPE);
        }else{
            if (getIntent() != null){
                idChild = getIntent().getExtras().getInt(ID);
                nameChild = getIntent().getExtras().getString(NAME);
                genderChild = getIntent().getExtras().getString(GENDER);
                type = getIntent().getExtras().getString(TYPE);
            }
        }

        childrenDAO = new ChildrenDAO(this);
        awardsDAO = new AwardsDAO(this);
        penaltiesDAO = new PenaltiesDAO(this);
        goalsDAO = new GoalsDAO(this);
        realizationDAO = new RealizationDAO(this);

        List<Realizates> listRealization = new ArrayList<>();
        if (type != null){
            if (type.equals(GENERAL_TYPE)){
                getSupportActionBar().setTitle(getResources().getString(R.string.history_general));
                listRealization = realizationDAO.fetchRealization();

            }else if (type.equals(CHILDREN_TYPE)){
                if (!nameChild.isEmpty())
                    getSupportActionBar().setTitle(nameChild);
                if(idChild > 0)
                    listRealization = realizationDAO.fetchRealizationPerChild(idChild);
            }
        }

        text.setVisibility(View.GONE);
        if (listRealization != null)
            histList = getHistoryList(listRealization);
        Collections.reverse(histList);
        if(histList.size() > 0){
            noAction.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            historyAdapter = new HistoryAdapter(histList);
            history_recyclerView.setAdapter(historyAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            history_recyclerView.setLayoutManager(layoutManager);
            history_recyclerView.setHasFixedSize(true);
            history_recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        }else{
            noAction.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }
    }

    private List<String> getHistoryList(List<Realizates> realizatesList){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < realizatesList.size(); i++) {
            Realizates realizates = realizatesList.get(i);

            StringBuilder sb = new StringBuilder();
            String childPhrase = "";
            String pointPhrase = " pontos por ";
            String actionDescription = "";
            String actionType = realizates.getActionType();
            String gender = null;
            String g;
            String childName = null;
            Children children;

            if (type.equals(GENERAL_TYPE)){
                children = childrenDAO.fetchChildrenPerId(realizates.getIdChild());
                gender = children.getGender().equals("F") ? "filha" : "filho";
                childName = realizates.getName();

            }else if (type.equals(CHILDREN_TYPE)){
                if (!genderChild.isEmpty())
                    gender = genderChild.equals("F") ? "filha" : "filho";
            }

            switch (actionType){
                case "bonificar":
                    Goals goals = goalsDAO.fetchGoalPerId(realizates.getActionId());
                    g = gender.equals("F") ? "bonificada" : "bonificado";
                    childPhrase = " foi " + g + " com ";
                    actionDescription = goals.getDescription();
                    break;
                case "penalizar":
                    Penalties penalties = penaltiesDAO.fetchPenaltyPerId(realizates.getActionId());
                    g = gender.equals("F") ? "penalizada" : "penalizado";
                    childPhrase = " foi " + g + " com ";
                    actionDescription = penalties.getDescription();
                    break;
                case "ponto_extra":
                    if (realizates.getPointType().equals("vermelho")) {
                        g = gender.equals("F") ? "penalizada" : "penalizado";
                        childPhrase = " foi " + g + " com um ";
                    }else {
                        g = gender.equals("F") ? "bonificada" : "bonificado";
                        childPhrase = " foi " + g + " com um ";
                    }
                    break;
                case  "premiar":
                    Awards awards = awardsDAO.fetchAwardsPerId(realizates.getActionId());
                    g = gender.equals("F") ? "premiada" : "premiado";
                    childPhrase = " foi " + g + " com ";
                    actionDescription = awards.getDescription();
                    break;
            }


            sb.append("Em ");
            sb.append(realizates.getDate());
            sb.append(" às ");
            sb.append(realizates.getHour());
            sb.append("h, ");
            if (type.equals(CHILDREN_TYPE)){
                sb.append(gender);
            }else if(type.equals(GENERAL_TYPE)){
                sb.append(childName);
            }
            sb.append(childPhrase);
            if(actionType.equals("premiar")){
                sb.append(actionDescription);
            }else if (actionType.equals("ponto_extra")){
                sb.append("ponto extra");
            }else{
                sb.append(realizates.getPoint());
                sb.append(pointPhrase);
                sb.append(actionDescription);
            }
            list.add(sb.toString());

        }
        return list;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (type.equals(GENERAL_TYPE)){
                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                }else if (type.equals(CHILDREN_TYPE)){
                    intent = new Intent(this, ChildrenDetailActivity.class);
                    intent.putExtra(ChildrenDetailActivity.ID, idChild);
                }
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
