package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.GoalsListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsAssociationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 22/07/15.
 */
public class GoalsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.goals_recyclerView) RecyclerView goalsRecyclerView;
    @Bind(R.id.spinner_goals_category) Spinner goalsCategorySpinner;
    @Bind(R.id.no_goalText) TextView noGoalText;
    @Bind(R.id.cardView) CardView cardView;

    public static final String TAG = "MilhasApplication";
    public static final String REGISTRATION_ACTION = "REGISTRATION";
    public static final String EDITION_ACTION = "EDITION";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private GoalsDAO goalsDAO;
    private GoalsAssociationDAO goalsAssociationDAO;
    private CategoriesDAO categoriesDAO;
    private List<Goals> goalsList;
    private GoalsListAdapter goalsListAdapter;
    private List<Categories> categoriesList;
    private Categories c;
    private ChildrenDAO childrenDAO;
    private int catId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static GoalsFragment newInstance(Integer id){
        GoalsFragment fragment = new GoalsFragment();
        Bundle b = new Bundle();
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.goals));
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        goalsDAO = new GoalsDAO(getActivity());
        categoriesDAO = new CategoriesDAO(getActivity());
        goalsAssociationDAO = new GoalsAssociationDAO(getActivity());
        childrenDAO = new ChildrenDAO(getActivity());

        categoriesList = categoriesDAO.consultarCategoriesList();
        Log.e("Eliete", "GOALS FRAGMENT");
        for (Categories c : categoriesList){
            Log.e("Eliete", "categories DESCRIPTION " + c.getDescription());
            Log.e("Eliete", "categories ID " + c.getId());
        }
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setOnItemSelectedListener(this);

        if(getArguments() != null){
            catId = getArguments().getInt("ID");
            Log.e("Eliete", "catId " + catId);
            if(catId > 0){
                Log.e("Eliete", "catDescription " + categoriesList.get(catId).getDescription());
                goalsCategorySpinner.setSelection(catId - 1);
            }else{
                goalsCategorySpinner.setSelection(categoriesList.size() - 1);
            }

        }

        cardView.setVisibility(View.GONE);
        noGoalText.setVisibility(View.GONE);

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
            View child = goalsRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && mGestureDetector.onTouchEvent(e)){
                final int position = goalsRecyclerView.getChildAdapterPosition(child);

                final Goals goals = goalsList.get(position);
                final Categories c = categoriesList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.icon_action_bar);
                alert.setTitle(getResources().getString(R.string.alert_option_selected) + " " + goals.getDescription());
                alert.setMessage(getResources().getString(R.string.alert_option_choose));

                alert.setPositiveButton(getResources().getString(R.string.alert_button_edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, GoalsRegisterFragment.newInstance(EDITION_ACTION, goals.getId(), goalsCategorySpinner.getSelectedItemPosition()))
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
                        int idChild = verifyChildAssociated(goals.getId(), goals.getCatId());
                        Children child = childrenDAO.consultarPorId(idChild);
                        if(child != null)
                            showAlert(getResources().getString(R.string.alert_goal_associated)+ " " + child.getName(), "OK");
                        else {
                            if (goalsDAO.deletarId(goals.getId())) {
                                goalsListAdapter.getGoalsList().remove(position);
                                goalsListAdapter.notifyItemRemoved(position);
                            }
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
            initFragment(GoalsRegisterFragment.newInstance(REGISTRATION_ACTION, c.getId(), goalsCategorySpinner.getSelectedItemPosition()));
            return true;
        } else if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        c = categoriesList.get(position);
        goalsList = goalsDAO.consultarGoalsListPerCategory(c.getId());
        if(goalsList != null){

            if(goalsList.size() == 0 && c.getId() != 1000){
                cardView.setVisibility(View.VISIBLE);
                noGoalText.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.GONE);
                noGoalText.setVisibility(View.GONE);
            }
            goalsListAdapter = new GoalsListAdapter(goalsList);
            goalsRecyclerView.setAdapter(goalsListAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            goalsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            goalsRecyclerView.setLayoutManager(layoutManager);
            goalsRecyclerView.setHasFixedSize(true);
            goalsRecyclerView.addOnItemTouchListener(RecycleTouchListener);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public String getTittle() {
        return getResources().getString(R.string.goals);
    }

    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack("back")
                .commit();
    }

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

    public int verifyChildAssociated(int idActivity, int idCat){
        List<Associates> associatesList = goalsAssociationDAO.consultarAssociatesListPerCategory(idCat);

        if(associatesList.size() > 0){
            for (Associates a : associatesList){
                if(a.getIdActivity() == idActivity)
                    return a.getIdChildren();
            }
        }
        return 0;
    }
}
