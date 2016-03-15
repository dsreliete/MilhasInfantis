package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.LikeListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsAssociationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/10/15.
 */
public class LikeFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.association_goals_recyclerView) RecyclerView assocGoalsRecyclerView;
    @Bind(R.id.spinner_goals_category) Spinner goalsCategorySpinner;
    @Bind(R.id.no_goalText) TextView noGoalText;
    @Bind(R.id.cardView) CardView cardView;
    @Bind(R.id.text) TextView textView;
    @Bind(R.id.date) EditText dateEditText;
    @Bind(R.id.text_input_layout_date)
    TextInputLayout textInputLayoutdate;

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private CategoriesDAO categoriesDAO;
    private GoalsAssociationDAO goalsAssociationDAO;
    private RealizationDAO realizationDAO;
    private LikeListAdapter likeListAdapter;
    private Categories categories;
    private List<Categories> categoriesList;
    private List<Goals> associateList;
    private int idChild;
    private String date;
    private String time;

    public static LikeFragment newInstance(int id) {
        LikeFragment fragment = new LikeFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_like, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);

        if(getArguments() != null){
            idChild = getArguments().getInt("ID");
        }

        time =  Utils.getTimeNow();
        date = Utils.getTodayDate();

        realizationDAO = new RealizationDAO(getActivity());

        textInputLayoutdate.setErrorEnabled(true);
        Utils.cleanErrorMessage(dateEditText, textInputLayoutdate);

        dateEditText.setText(Utils.formatDate(Utils.getTodayDate()));
        dateEditText.setFocusableInTouchMode(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(LikeFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });

        categoriesDAO = new CategoriesDAO(getActivity());
        goalsAssociationDAO = new GoalsAssociationDAO(getActivity());
        categoriesList = categoriesDAO.consultarCategoriesList();
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setSelection(categoriesList.size() - 1);

        goalsCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categories = (Categories) goalsCategorySpinner.getSelectedItem();
                associateList = goalsAssociationDAO.consultarAssociatesListPerCategoryPerChild(categories.getId(), idChild);

                if (associateList.size() == 0 && categories.getId() != 1000) {
                    cardView.setVisibility(View.VISIBLE);
                    noGoalText.setVisibility(View.VISIBLE);
                } else {
                    if (categories.getId() == 1000) {
                        textView.setText("Bonificação");
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Escolha uma das opções para bonificar");
                    }
                    cardView.setVisibility(View.GONE);
                    noGoalText.setVisibility(View.GONE);
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                assocGoalsRecyclerView.setLayoutManager(layoutManager);
                assocGoalsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                assocGoalsRecyclerView.setHasFixedSize(true);
                likeListAdapter = new LikeListAdapter(associateList, onItemClickCallback);
                assocGoalsRecyclerView.setAdapter(likeListAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    @Override
    public String getTittle() {
        return " ";
    }

    //get Data to bonificate :)
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String d = "";
        String m = "";
        if(day < 10) {
            d = "0" + day;
        }else{
            d = "" + day;
        }
        if(++month < 10){
            m = "0" + (month);
        }else{
            m = "" + (month);
        }

        date = Utils.concatDate(year, m, d);
        String formatDate = Utils.formatDate(date);
        dateEditText.setText(formatDate);

    }


    public static class OnItemClickListener implements View.OnClickListener {
        private int position;
        private OnItemClickCallback onItemClickCallback;

        public OnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
            this.position = position;
            this.onItemClickCallback = onItemClickCallback;
        }

        @Override
        public void onClick(View view) {
            onItemClickCallback.onItemClicked(view, position);
        }

        public interface OnItemClickCallback {
            void onItemClicked(View view, int position);
        }
    }

    //bonificate
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            int point = 0;
            String pointType = "";
            boolean sucess = false;
            Goals goal = associateList.get(position);

             switch(view.getId()){
                 case R.id.happy:
                     point = goal.getGreenPoint();
                     pointType = "verde";
                     break;
                 case R.id.soso:
                     point = goal.getYellowPoint();
                     pointType = "amarelo";
                     break;
                 case R.id.angry:
                     point = goal.getRedPoint();
                     pointType = "vermelho";
                     break;
             }

            if(dateEditText.getText().length() == 0){
                Utils.setEmptyMessage(textInputLayoutdate, getResources().getString(R.string.date));
            }else{
                sucess = realizationDAO.inserir(idChild, goal.getId(), categories.getId(), "bonificar", point, pointType, date, time);
            }

            if (dateEditText.getText().length() > 0) {
                if (sucess) {
                    Toast.makeText(getActivity(), "Bonificação realizada com sucesso!!! Vale " + point + " pontos", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.snack_no_bonification),
                            Snackbar.LENGTH_SHORT)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }

        }
    };



}
