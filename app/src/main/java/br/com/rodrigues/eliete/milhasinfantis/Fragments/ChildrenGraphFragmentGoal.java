package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.ChildrenGraphActivity;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsAssociationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/20/15.
 */
public class ChildrenGraphFragmentGoal extends Fragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.spinner1) Spinner spinner1;
    @Bind(R.id.date) EditText dateEditText;
    @Bind(R.id.text_title) TextView textTitle;
    @Bind(R.id.text_input_layout_date)
    TextInputLayout textInputLayoutdate;
    @Bind(R.id.noPoint) TextView noPoint;
    @Bind(R.id.cardView)CardView cardView;
    @Bind(R.id.legendXText) TextView legendXText;

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private RealizationDAO realizationDAO;
    private GoalsAssociationDAO goalsAssociationDAO;
    private BarChart mChart;
    private ArrayList<Integer> yData;
    private ArrayList<String> xData;
    private int idChild;
    private String dateIni;
    private String dateEnd;
    private List<Categories> spinner1List;
    private List<Goals> goalsList;

    public static ChildrenGraphFragmentGoal newInstance(int id) {
        ChildrenGraphFragmentGoal fragment = new ChildrenGraphFragmentGoal();
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

        View rootView = inflater.inflate(R.layout.fragment_children_graph_goal, container, false);

        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);

        if (getArguments() != null) {
            idChild = getArguments().getInt("ID");
            ChildrenDAO childrenDAO = new ChildrenDAO(getActivity());
            Children c = childrenDAO.fetchChildrenPerId(idChild);
            ((ChildrenGraphActivity) getActivity()).getSupportActionBar().setTitle(c.getName());
        }


        realizationDAO = new RealizationDAO(getActivity());
        CategoriesDAO categoriesDAO = new CategoriesDAO(getActivity());
        goalsAssociationDAO = new GoalsAssociationDAO(getActivity());

        textInputLayoutdate.setErrorEnabled(true);
        Utils.cleanErrorMessage(dateEditText, textInputLayoutdate);
        dateEditText.setHint(getResources().getString(R.string.pick_date_select));
        dateEditText.setFocusableInTouchMode(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ChildrenGraphFragmentGoal.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });

        spinner1.setVisibility(View.GONE);

        spinner1List = new ArrayList<>();
        spinner1List = categoriesDAO.fetchCategoriesList();
        spinner1List.add(new Categories(1000, "Selecione uma opção de categoria"));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinner1List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChart.setVisibility(View.GONE);
                textTitle.setVisibility(View.GONE);
                legendXText.setVisibility(View.GONE);
                noPoint.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
                Categories categories = (Categories) spinner1.getSelectedItem();

                ArrayList<String> xVals = new ArrayList<String>();
                ArrayList<String> xValsLegends = new ArrayList<String>();
                ArrayList<BarEntry> yValsRed = new ArrayList<BarEntry>();
                ArrayList<BarEntry> yValsYellow = new ArrayList<BarEntry>();
                ArrayList<BarEntry> yValsGreen = new ArrayList<BarEntry>();

                if (categories.getId() != 1000) {
                    goalsList = goalsAssociationDAO.fetchAssociatedGoalsListPerCategoryPerChild(categories.getId(), idChild);

                    if(goalsList.size() > 0){
                        for (int i = 0; i < goalsList.size(); i++){
                            Goals goals = goalsList.get(i);
                            xVals.add("Atividade " + i);
                            xValsLegends.add(goals.getDescription());

                            int redGoalAction = realizationDAO.GoalRedActions(idChild, goals.getId(), goals.getCatId(), dateIni, dateEnd);
                            int yellowGoalAction = realizationDAO.GoalYellowActions(idChild, goals.getId(), goals.getCatId(), dateIni, dateEnd);
                            int greenGoalAction = realizationDAO.GoalGreenActions(idChild, goals.getId(), goals.getCatId(), dateIni, dateEnd);

                            yValsRed.add(new BarEntry(redGoalAction, i));
                            yValsYellow.add(new BarEntry(yellowGoalAction, i));
                            yValsGreen.add(new BarEntry(greenGoalAction, i));
                        }

                        initGraph();

                        if (yValsRed.size() == 0 && yValsYellow.size() == 0 && yValsGreen.size() == 0) {
                            noPoint.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                        } else {
                            textTitle.setVisibility(View.VISIBLE);
                            textTitle.setText("Total de bonificações por atividade " +
                                    "e tipo de ponto no período de " +
                                    Utils.formatDate(dateIni) + " a " + Utils.formatDate(dateEnd));

                            BarDataSet setRed = new BarDataSet(yValsRed, ": (");
                            setRed.setColor(Color.rgb(229, 57, 53));
                            BarDataSet setYellow = new BarDataSet(yValsYellow, ": |");
                            setYellow.setColor(Color.rgb(255, 202, 40));
                            BarDataSet setGreen = new BarDataSet(yValsRed, ": )");
                            setGreen.setColor(Color.rgb(93, 127, 53));

                            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                            dataSets.add(setRed);
                            dataSets.add(setYellow);
                            dataSets.add(setGreen);

                            BarData data = new BarData(xVals, dataSets);
                            data.setGroupSpace(80f);
                            data.setValueFormatter(new MyValueFormatter());

                            mChart.setData(data);
                            mChart.invalidate();
                            mChart.setVisibility(View.VISIBLE);

                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < xVals.size(); i++){
                                sb.append(xVals.get(i) + ". " + xValsLegends.get(i) + " ");

                            }
                            legendXText.setVisibility(View.VISIBLE);
                            legendXText.setText(sb + "\n :( atividade não realizada, : | "+
                                    "atividade realizada de maneira pouco satisfatória e" +
                                    " : ) atividade realizada com sucesso ");

                        }
                    }else{
                        noPoint.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mChart = (BarChart) rootView.findViewById(R.id.chart);
        mChart.setVisibility(View.GONE);
        noPoint.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        return rootView;
    }

    private void initGraph() {
        mChart.setDescription("");
        mChart.animateY(4000);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.setGridBackgroundColor(getResources().getColor(R.color.white));

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setValueFormatter(new MyValueFormatter());
//        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextSize(8f);
        
    }

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

        dateEnd = Utils.concatDate(year, m, d);
        dateIni = Utils.getSevenDaysBefore(dateEnd);
        dateEditText.setText(Utils.formatDate(dateEnd));
        spinner1.setSelection(spinner1List.size() - 1);
        spinner1.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.GONE);
        textTitle.setVisibility(View.GONE);
    }

    public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value) + "";
        }

    }


}
