package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.GoalsAssociationListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.IncludeExclude;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsAssociationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/9/15.
 */
//Atribuir Assciação
public class GoalsIncludeAssociationFragment extends BaseFragment implements IncludeExclude, AdapterView.OnItemSelectedListener {

    @Bind(R.id.association_goals_recyclerView)
    RecyclerView assocGoalsRecyclerView;
    @Bind(R.id.spinner_goals_category)
    Spinner goalsCategorySpinner;
    @Bind(R.id.no_goalText)
    TextView noGoalText;
    @Bind(R.id.cardView)
    CardView cardView;

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private GoalsDAO goalsDAO;
    private List<Goals> goalsList;
    private List<Goals> associateList;
    private Categories categories;
    private CategoriesDAO categoriesDAO;
    private List<Categories> categoriesList;
    private GoalsAssociationListAdapter goalsAssociationListAdapter;
    private GoalsAssociationDAO goalsAssociationDAO;
    private Associates a;
    private int idChild;
    private Goals goalsItem;

    public static GoalsIncludeAssociationFragment newInstance(int id){
        GoalsIncludeAssociationFragment fragment = new GoalsIncludeAssociationFragment();
        Bundle b = new Bundle();
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_include_association, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);
        goalsDAO = new GoalsDAO(getActivity());
        categoriesDAO = new CategoriesDAO(getActivity());
        goalsAssociationDAO = new GoalsAssociationDAO(getActivity());

        if(getArguments() != null){
            idChild = getArguments().getInt("ID");
        }

        categoriesList = categoriesDAO.consultarCategoriesList();
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setSelection(categoriesList.size() - 1);
        goalsCategorySpinner.setOnItemSelectedListener(this);

        cardView.setVisibility(View.GONE);
        noGoalText.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public String getTittle() {
        return " ";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        categories = (Categories) goalsCategorySpinner.getSelectedItem();
        int catId = categories.getId();
        goalsList = goalsDAO.consultarGoalsListPerCategory(categories.getId());
        associateList = goalsAssociationDAO.consultarAssociatesListPerCategoryPerChild(categories.getId(), idChild);

            if (associateList != null && associateList.size() > 0) {
                for (int j = 0; j < associateList.size(); j++) {
                    Goals go = associateList.get(j);
                    for (int i = 0; i < goalsList.size(); i++) {
                        Goals goals = goalsList.get(i);
                        if (go.getCatId() == catId && goals.getCatId() == catId) {
                            if (go.getId() == goals.getId()) {
                                int index = goalsList.indexOf(goals);
                                goalsList.remove(index);
                                break;
                            }
                        }
                    }
                }
            }

        if (goalsList.size() == 0 && catId != 1000){
            cardView.setVisibility(View.VISIBLE);
            noGoalText.setVisibility(View.VISIBLE);
        }else {
            cardView.setVisibility(View.GONE);
            noGoalText.setVisibility(View.GONE);
        }
        goalsAssociationListAdapter = new GoalsAssociationListAdapter(goalsList);
        assocGoalsRecyclerView.setAdapter(goalsAssociationListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        assocGoalsRecyclerView.setLayoutManager(layoutManager);
        assocGoalsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        assocGoalsRecyclerView.setHasFixedSize(true);
        assocGoalsRecyclerView.addOnItemTouchListener(RecycleTouchListener);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

        @Override public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

    });


    private RecyclerView.OnItemTouchListener RecycleTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = assocGoalsRecyclerView.findChildViewUnder(e.getX(), e.getY());

            //get category
            Categories cat = (Categories) goalsCategorySpinner.getSelectedItem();
            final int catId = cat.getId();

            if(child!=null && mGestureDetector.onTouchEvent(e)){
                final int position = assocGoalsRecyclerView.getChildAdapterPosition(child);
                goalsItem = goalsList.get(position);
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.icon_action_bar);
                alert.setTitle(getResources().getString(R.string.alert_option_selected) + " " + goalsItem.getDescription());
                alert.setMessage(getResources().getString(R.string.alert_option_choose));

                alert.setPositiveButton(getResources().getString(R.string.association_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        a = new Associates(idChild, goalsItem.getId(), catId);
                        goalsAssociationDAO.inserir(a);
                        goalsAssociationListAdapter.getGoalsList().remove(position);
                        goalsAssociationListAdapter.notifyItemRemoved(position);

                    }
                });
                alert.setNegativeButton(getResources().getString(R.string.alert_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();



            }
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    };

    @Override
    public void fragmentBecameVisible() {
        goalsCategorySpinner.setSelection(categoriesList.size() - 1);
    }
}
