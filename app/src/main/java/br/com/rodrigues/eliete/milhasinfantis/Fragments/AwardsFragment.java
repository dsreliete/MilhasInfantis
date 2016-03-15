package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.AwardsListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 22/07/15.
 */
public class AwardsFragment extends BaseFragment {

    @Bind(R.id.awards_recyclerView) RecyclerView awdsRecyclerView;

    public static final String TAG = "MilhasApplication";
    public static final String REGISTRATION_ACTION = "REGISTRATION";
    public static final String EDITION_ACTION = "EDITION";

    private AwardsDAO awardsDAO;
    private List<Awards> awardsList;
    private AwardsListAdapter awdsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_awards, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.awards));
        ButterKnife.bind(this, rootView);

        awardsDAO = new AwardsDAO(getActivity());
        awardsList = awardsDAO.consultarAwardsList();
        if(awardsList != null){
            awdsListAdapter = new AwardsListAdapter(awardsList);
            awdsRecyclerView.setAdapter(awdsListAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            awdsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            awdsRecyclerView.setLayoutManager(layoutManager);
            awdsRecyclerView.setHasFixedSize(true);
            awdsRecyclerView.addOnItemTouchListener(RecycleTouchListener);
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
            View child = awdsRecyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && mGestureDetector.onTouchEvent(e)){
                int position = awdsRecyclerView.getChildAdapterPosition(child);

                final Awards awards = awardsList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.icon_action_bar);
                alert.setTitle(getResources().getString(R.string.alert_option_selected) + " " + awards.getDescription());
                alert.setMessage(getResources().getString(R.string.alert_option_choose) + " ");

                alert.setPositiveButton(getResources().getString(R.string.alert_button_edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, AwardsRegisterFragment.newInstance(EDITION_ACTION, awards.getId()))
                                .addToBackStack("back")
                                .commit();
                    }
                });
                alert.setNeutralButton(getResources().getString(R.string.alert_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setNegativeButton(getResources().getString(R.string.alert_button_exclude), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(awardsDAO.deletarId(awards.getId())){
                            awardsList = awardsDAO.consultarAwardsList();
                            awdsListAdapter.notifyDataSetChanged();
                            awdsListAdapter = new AwardsListAdapter(awardsList);
                            awdsRecyclerView.setAdapter(awdsListAdapter);
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
        inflater.inflate(R.menu.menu_awards, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_plus_awds) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, AwardsRegisterFragment.newInstance(REGISTRATION_ACTION, 0))
                    .addToBackStack("back")
                    .commit();
            return true;
        } else if (id == android.R.id.home) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, new AwardsFragment())
                    .addToBackStack("back")
                    .commit();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getTittle() {
        return getResources().getString(R.string.awards);
    }
}
