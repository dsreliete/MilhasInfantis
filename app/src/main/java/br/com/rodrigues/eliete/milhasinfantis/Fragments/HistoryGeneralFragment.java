package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.HistoryAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Model.Realizates;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 22/07/15.
 */
public class HistoryGeneralFragment extends BaseFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.history_general));
        ButterKnife.bind(this, rootView);

        childrenDAO = new ChildrenDAO(getActivity());
        awardsDAO = new AwardsDAO(getActivity());
        penaltiesDAO = new PenaltiesDAO(getActivity());
        goalsDAO = new GoalsDAO(getActivity());
        realizationDAO = new RealizationDAO(getActivity());

        text.setVisibility(View.GONE);

        List<Realizates> listRealization = realizationDAO.consultarRealizacao();
        histList = getHistoryList(listRealization);
        Collections.reverse(histList);
        if(histList.size() > 0){
            noAction.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            historyAdapter = new HistoryAdapter(histList);
            history_recyclerView.setAdapter(historyAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            history_recyclerView.setLayoutManager(layoutManager);
            history_recyclerView.setHasFixedSize(true);
            history_recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        }else{
            noAction.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private List<String> getHistoryList(List<Realizates> realizatesList){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < realizatesList.size(); i++){
            Realizates realizates = realizatesList.get(i);

            Children c = childrenDAO.consultarPorId(realizates.getIdChild());
            String gender = c.getGender();
            String g;
            StringBuilder sb = new StringBuilder();
            String child = realizates.getName();
            String childPhrase = "";
            String pointPhrase = " pontos por ";
            String actionDescription = "";
            String actionType = realizates.getActionType();
            switch (actionType){
                case "bonificar":
                    Goals goals = goalsDAO.consultarGoalPorId(realizates.getActionId());
                    g = gender.equals("F") ? "bonificada" : "bonificado";
                    childPhrase = " foi " + g + " com ";
                    actionDescription = goals.getDescription();
                    break;
                case "penalizar":
                    Penalties penalties = penaltiesDAO.consultarPorId(realizates.getActionId());
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
                    Awards awards = awardsDAO.consultarPorId(realizates.getActionId());
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
            sb.append(child);
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
    public String getTittle() {
        return getResources().getString(R.string.history_general);
    }
}
