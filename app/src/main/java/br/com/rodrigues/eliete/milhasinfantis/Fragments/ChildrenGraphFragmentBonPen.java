package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

import br.com.rodrigues.eliete.milhasinfantis.ChildrenGraphActivity;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/20/15.
 */
public class ChildrenGraphFragmentBonPen extends Fragment implements DatePickerDialog.OnDateSetListener {

     @Bind(R.id.date) EditText dateEditText;
    @Bind(R.id.text_title) TextView textTitle;
    @Bind(R.id.text_input_layout_date)
    TextInputLayout textInputLayoutdate;
    @Bind(R.id.calendar)
    ImageView calendar;
    @Bind(R.id.noPoint) TextView noPoint;
    @Bind(R.id.cardView)CardView cardView;
    @Bind(R.id.legendXText) TextView legend;

    private RealizationDAO realizationDAO;
    private PieChart mChart;
    private ArrayList<Integer> yData;
    private ArrayList<String> xData;
    private int idChild;
    private String dateIni;
    private String dateEnd;

    public static ChildrenGraphFragmentBonPen newInstance(int id) {
        ChildrenGraphFragmentBonPen fragment = new ChildrenGraphFragmentBonPen();
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

        View rootView = inflater.inflate(R.layout.fragment_children_graph_bonpen, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);

        if (getArguments() != null) {
            idChild = getArguments().getInt("ID");
            ChildrenDAO childrenDAO = new ChildrenDAO(getActivity());
            Children c = childrenDAO.fetchChildrenPerId(idChild);
            ((ChildrenGraphActivity) getActivity()).getSupportActionBar().setTitle(c.getName());
        }

        realizationDAO = new RealizationDAO(getActivity());

        textInputLayoutdate.setErrorEnabled(true);
        Utils.cleanErrorMessage(dateEditText, textInputLayoutdate);
        dateEditText.setHint(getResources().getString(R.string.pick_date_select));
        dateEditText.setFocusableInTouchMode(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ChildrenGraphFragmentBonPen.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });

        textTitle.setVisibility(View.GONE);

        mChart = (PieChart) rootView.findViewById(R.id.parent_chart);
        mChart.setVisibility(View.GONE);
        noPoint.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        return rootView;
    }

    private void initGraph() {
        mChart.setUsePercentValues(false);
        mChart.setDescription(" ");
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
        l.setTextSize(12);
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
        color.add(getResources().getColor(R.color.green_warranty));
        color.add(getResources().getColor(R.color.red));
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
        mChart.setVisibility(View.GONE);
        textTitle.setVisibility(View.GONE);

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

        yData = new ArrayList<Integer>();
        int bonAction = realizationDAO.fetchBonActions(idChild, dateIni, dateEnd);
        int penAction = realizationDAO.fetchPenActions(idChild, dateIni, dateEnd);
        if(bonAction == 0 && penAction == 0){
            noPoint.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }else {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText("Gráfico do total de Bonificações x Penalizações no período de " +
                    Utils.formatDate(dateIni) + " a " + Utils.formatDate(dateEnd));

            yData.add(bonAction);
            yData.add(penAction);

            xData = new ArrayList<String>();
            xData.add(" Bonificações ");
            xData.add(" Penalizações ");
            setData();
            mChart.setVisibility(View.VISIBLE);
            legend.setVisibility(View.VISIBLE);
            legend.setText("O total de bonificações corresponde a soma entre os pontos vermelhos, amarelos e verdes");

            initGraph();
        }

    }

    public class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value) + "";
        }
    }


}
