package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/20/15.
 */
public class ChildrenGraphFragmentCategory extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.spinner1) Spinner spinner1;
    @Bind(R.id.date) EditText dateEditText;
    @Bind(R.id.text_title) TextView textTitle;
    @Bind(R.id.text_input_layout_date)
    TextInputLayout textInputLayoutdate;
    @Bind(R.id.calendar)
    ImageView calendar;
    @Bind(R.id.noPoint) TextView noPoint;
    @Bind(R.id.cardView)CardView cardView;

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private RealizationDAO realizationDAO;
    private PieChart mChart;
    private ArrayList<Integer> yData;
    private ArrayList<String> xData;
    private int idChild;
    private String dateIni;
    private String dateEnd;
    private List<Categories> spinner1List;

    public static ChildrenGraphFragmentCategory newInstance(int id) {
        ChildrenGraphFragmentCategory fragment = new ChildrenGraphFragmentCategory();
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

        View rootView = inflater.inflate(R.layout.fragment_children_graph, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);

        if (getArguments() != null) {
            idChild = getArguments().getInt("ID");
            ChildrenDAO childrenDAO = new ChildrenDAO(getActivity());
            Children c = childrenDAO.consultarPorId(idChild);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(c.getName());
        }

        realizationDAO = new RealizationDAO(getActivity());
        CategoriesDAO categoriesDAO = new CategoriesDAO(getActivity());

        noPoint.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);

        textInputLayoutdate.setErrorEnabled(true);
        Utils.cleanErrorMessage(dateEditText, textInputLayoutdate);
        dateEditText.setHint(getResources().getString(R.string.pick_date_select));
        dateEditText.setFocusableInTouchMode(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ChildrenGraphFragmentCategory.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });

        spinner1.setVisibility(View.GONE);
        spinner1List = new ArrayList<>();
        spinner1List = categoriesDAO.consultarCategoriesList();
        spinner1List.add(new Categories(1000, "Selecione uma opção"));
        ArrayAdapter<Categories> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinner1List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChart.setVisibility(View.GONE);
                textTitle.setVisibility(View.GONE);

                if (position != spinner1List.size() - 1) {
                    Categories categories = spinner1List.get(position);

                    int redCatAction = realizationDAO.CatRedActions(idChild, categories.getId(), dateIni, dateEnd);
                    int yellowCatAction = realizationDAO.CatYellowActions(idChild, categories.getId(), dateIni, dateEnd);
                    int greenCatAction = realizationDAO.CatGreenActions(idChild, categories.getId(), dateIni, dateEnd);

                    initGraph();

                    if(redCatAction == 0 && yellowCatAction == 0 && greenCatAction == 0) {
                        noPoint.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                    }else{
                        textTitle.setVisibility(View.VISIBLE);
                        textTitle.setText("Gráfico do total de bonificações por categoria " +
                                "e tipo de ponto no período de " +
                                Utils.formatDate(dateIni) + " a " + Utils.formatDate(dateEnd));

                        mChart.setVisibility(View.VISIBLE);

                        yData = new ArrayList<Integer>();
                        yData.add(redCatAction);
                        yData.add(yellowCatAction);
                        yData.add(greenCatAction);

                        xData = new ArrayList<String>();
                        xData.add(" :( ");
                        xData.add(" :| ");
                        xData.add(" :) ");


                        noPoint.setVisibility(View.GONE);
                        cardView.setVisibility(View.GONE);
                        setData();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mChart = (PieChart) rootView.findViewById(R.id.parent_chart);
        mChart.setVisibility(View.GONE);

        return rootView;
    }

    private void initGraph() {
        mChart.setUsePercentValues(false);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(false);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawSliceText(false);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextSize(16);
    }

    public void setData(){

        ArrayList<Entry> yValues = new ArrayList<>();
        for(int i = 0; i < yData.size(); i++){
            yValues.add(new Entry(yData.get(i), i));
        }

        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < xData.size(); i++){
            xValues.add(xData.get(i));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(2f);

        ArrayList<Integer> color = new ArrayList<>();
        color.add(getResources().getColor(R.color.red));
        color.add(getResources().getColor(R.color.yellow));
        color.add(getResources().getColor(R.color.green_warranty));
        dataSet.setColors(color);

        PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.DKGRAY);
        mChart.highlightValues(null);
        mChart.setData(data);
        mChart.animateX(1400);
        mChart.invalidate();


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


        @Override
    public String getTittle() {
        return " ";
    }
}
