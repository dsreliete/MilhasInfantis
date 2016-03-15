package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import br.com.rodrigues.eliete.milhasinfantis.Adapters.CategoriesListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/7/15.
 */
public class CategoriesFragment extends BaseFragment{

    @Bind(R.id.cat_recyclerView) RecyclerView catRecyclerView;
    @Bind(R.id.no_catText) TextView noCatText;
    @Bind(R.id.cardView) CardView cardView;

    public static final String TAG = "MilhasApplication";
    public static final String REGISTRATION_ACTION = "REGISTRATION";
    public static final String EDITION_ACTION = "EDITION";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private CategoriesDAO categoriesDAO;
    private GoalsDAO goalsDAO;
    private List<Categories> categoriesList;
    private CategoriesListAdapter categoriesListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.categories));
        ButterKnife.bind(this, rootView);

        categoriesDAO = new CategoriesDAO(getActivity());
        goalsDAO = new GoalsDAO(getActivity());

        categoriesList = categoriesDAO.consultarCategoriesList();

        if(categoriesList != null){
            if(categoriesList.size() == 0){
                cardView.setVisibility(View.VISIBLE);
                noCatText.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.GONE);
                noCatText.setVisibility(View.GONE);

                categoriesListAdapter = new CategoriesListAdapter(categoriesList);
                catRecyclerView.setAdapter(categoriesListAdapter);
                catRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                catRecyclerView.setLayoutManager(layoutManager);
                catRecyclerView.setHasFixedSize(true);
                catRecyclerView.addOnItemTouchListener(RecycleTouchListener);
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
            View child = catRecyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && mGestureDetector.onTouchEvent(e)){
                int position = catRecyclerView.getChildAdapterPosition(child);

                final Categories cat = categoriesList.get(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.icon_action_bar);
                alert.setTitle(getResources().getString(R.string.alert_option_selected) + " " + cat.getDescription());
                alert.setMessage(getResources().getString(R.string.alert_option_choose));

                alert.setPositiveButton(getResources().getString(R.string.alert_button_edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, CategoriesRegisterFragment.newInstance(EDITION_ACTION, cat.getId()))
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
                        List<Goals> gList = goalsDAO.consultarGoalsListPerCategory(cat.getId());
                        if (gList.size() == 0) {
                            if (categoriesDAO.deletarId(cat.getId())) {
                                categoriesList = categoriesDAO.consultarCategoriesList();
                                categoriesListAdapter.notifyDataSetChanged();
                                categoriesListAdapter = new CategoriesListAdapter(categoriesList);
                                catRecyclerView.setAdapter(categoriesListAdapter);
                                categoriesListAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setIcon(R.drawable.icon_action_bar);
                            alert.setTitle(getResources().getString(R.string.app_name));
                            alert.setMessage(getResources().getString(R.string.alert_association));
                            alert.setPositiveButton(getResources().getString(R.string.alert_button_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
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
                    .add(R.id.container, CategoriesRegisterFragment.newInstance(REGISTRATION_ACTION, 0), FRAGMENT_TAG)
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
        return getResources().getString(R.string.categories);
    }
}
