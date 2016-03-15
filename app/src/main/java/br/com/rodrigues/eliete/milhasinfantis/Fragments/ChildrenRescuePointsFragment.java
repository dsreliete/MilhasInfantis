package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.RescueAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Realizates;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/30/15.
 */
public class ChildrenRescuePointsFragment extends BaseFragment {

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
    private Children children;
    private int totalPoints;
    private String time;
    private String date;
    private RescueAdapter rescueAdapter;

    public static ChildrenRescuePointsFragment newInstance(int id){
        ChildrenRescuePointsFragment fragment = new ChildrenRescuePointsFragment();
        Bundle b = new Bundle();
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.rescue_points));
        View rootView = inflater.inflate(R.layout.fragment_children_rescue, container, false);
        ButterKnife.bind(this, rootView);

        childrenDAO = new ChildrenDAO(getActivity());
        idChild = getArguments().getInt("ID");
        if (idChild > 0) {
            children = childrenDAO.consultarPorId(idChild);
        }

        if (children.getGender().equals("F")) {
            childImageView.setImageResource(R.drawable.maria);
        } else if (children.getGender().equals("M")) {
            childImageView.setImageResource(R.drawable.joao);
        } else {
            childImageView.setImageResource(R.drawable.joao);
        }

        childNameTextView.setText(children.getName());

        realizationDAO = new RealizationDAO(getActivity());
        totalPoints = realizationDAO.consultarTotalPointsPerChild(idChild);
        totalPointsTextView.setText(totalPoints + " pontos");

        awardsDAO = new AwardsDAO(getActivity());
        List<Awards> awardsList = awardsDAO.consultarAwardsList();
        if(awardsList.size() > 0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            rescueRecyclerView.setLayoutManager(layoutManager);
            rescueRecyclerView.setHasFixedSize(true);
            rescueRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            rescueAdapter = new RescueAdapter(getActivity(), awardsList, onItemClickCallback, idChild);
            rescueRecyclerView.setAdapter(rescueAdapter);
            cardView.setVisibility(View.GONE);
            noAwards.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.VISIBLE);
            noAwards.setVisibility(View.VISIBLE);
        }

        time = Utils.getTimeNow();
        date = Utils.getTodayDate();

        return rootView;
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
            sucess = realizationDAO.inserir(idChild, awardId, 0, "premiar", awardsPoint, "azul", date, time);
            if(sucess){
                rescueAdapter.notifyDataSetChanged();
                totalPoints = realizationDAO.consultarTotalPointsPerChild(idChild);
                if(totalPoints == 1){
                    totalPointsTextView.setText(totalPoints + " ponto");
                }else{
                    totalPointsTextView.setText(totalPoints + " pontos");
                }
                List<Realizates> realizateList = realizationDAO.consultarRealizacaoPerChild(idChild);
                showAlert("Objetivo alcançado. Parabéns!!!", "OK");
            }
        }
    };

    public void showAlert(String message, String posButton){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());
        alert.setIcon(R.drawable.icon_action_bar);
        alert.setTitle("Milhas Infantis");
        alert.setMessage(message);
        alert.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

        @Override
    public String getTittle() {
        return "";
    }
}
