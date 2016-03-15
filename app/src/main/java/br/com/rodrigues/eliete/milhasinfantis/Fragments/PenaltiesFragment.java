package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.PenaltiesListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 22/07/15.
 */
public class PenaltiesFragment extends BaseFragment {

    @Bind(R.id.pen_recyclerView) RecyclerView penaltyRecyclerView;
    @Bind(R.id.cardView) CardView cardView;
    @Bind(R.id.no_penaltyText) TextView noPenaltyText;

    public static final String TAG = "MilhasApplication";
    public static final String REGISTRATION_ACTION = "REGISTRATION";
    public static final String EDITION_ACTION = "EDITION";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private PenaltiesDAO penaltiesDAO;
    private List<Penalties> penaltiesList;
    private PenaltiesListAdapter penaltiesListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_penalties, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.penalties));
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        penaltiesDAO = new PenaltiesDAO(getActivity());

        penaltiesList = penaltiesDAO.consultarPenaltiesList();
        if(penaltiesList != null){

            if(penaltiesList.size() == 0){
                cardView.setVisibility(View.VISIBLE);
                noPenaltyText.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.GONE);
                noPenaltyText.setVisibility(View.GONE);

                penaltiesListAdapter = new PenaltiesListAdapter(penaltiesList);
                penaltyRecyclerView.setAdapter(penaltiesListAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                penaltyRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                penaltyRecyclerView.setLayoutManager(layoutManager);
                penaltyRecyclerView.setHasFixedSize(true);
                penaltyRecyclerView.addOnItemTouchListener(RecycleTouchListener);
            }
        }
            return rootView;
    }

    final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

        @Override public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

    });

    private RecyclerView.OnItemTouchListener RecycleTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = penaltyRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && mGestureDetector.onTouchEvent(e)){
                int position = penaltyRecyclerView.getChildAdapterPosition(child);

                final Penalties pen = penaltiesList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.icon_action_bar);
                alert.setTitle("Você selecionou a penalidade " + pen.getDescription());
                alert.setMessage("Escolha uma das opções para: ");

                alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, PenaltiesRegisterFragment.newInstance(EDITION_ACTION,
                                        pen.getId()), FRAGMENT_TAG)
                                .addToBackStack("back")
                                .commit();

                    }
                });
                alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (penaltiesDAO.deletarId(pen.getId())) {
                            penaltiesList = penaltiesDAO.consultarPenaltiesList();
                            penaltiesListAdapter.notifyDataSetChanged();
                            penaltiesListAdapter = new PenaltiesListAdapter(penaltiesList);
                            penaltyRecyclerView.setAdapter(penaltiesListAdapter);
                            penaltiesListAdapter.notifyDataSetChanged();
                        }
                    }
                });
                alert.show();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_goals, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_plus_goals) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PenaltiesRegisterFragment.newInstance(REGISTRATION_ACTION, 0))
                    .addToBackStack("back")
                    .commit();
            return true;
        } else if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getTittle() {
        return getResources().getString(R.string.penalties);
    }
}
