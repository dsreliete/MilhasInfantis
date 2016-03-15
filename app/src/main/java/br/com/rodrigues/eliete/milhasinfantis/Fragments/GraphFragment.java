package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;

/**
 * Created by eliete on 10/31/15.
 */
public class GraphFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {


    private ChildrenDAO childrenDAO;
    private RealizationDAO realizationDAO;
    private BarChart mChart;
    private TextView textTitle;
    private String dateEnd;
    private String dateIni;

    private EditText dateEditText;
    private TextView noPoint;
    private CardView cardView;
    private TextView legendXText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph2, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        TextInputLayout textInputLayoutdate = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_date);
        textInputLayoutdate.setErrorEnabled(true);

        dateEditText = (EditText) rootView.findViewById(R.id.date);
        Utils.cleanErrorMessage(dateEditText, textInputLayoutdate);
        dateEditText.setHint(getResources().getString(R.string.pick_date_select));
        dateEditText.setFocusableInTouchMode(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(GraphFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });

        legendXText = (TextView) rootView.findViewById(R.id.legendXText);
        legendXText.setVisibility(View.GONE);

        cardView = (CardView) rootView.findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);

        noPoint = (TextView) rootView.findViewById(R.id.noPoint);
        noPoint.setVisibility(View.GONE);

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setPinchZoom(true);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.animateY(4000);
        mChart.setVisibility(View.GONE);

        textTitle = (TextView) rootView.findViewById(R.id.text_title);
        textTitle.setVisibility(View.GONE);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);

        mChart.getAxisRight().setEnabled(false);

        childrenDAO = new ChildrenDAO(getActivity());
        realizationDAO = new RealizationDAO(getActivity());

        return rootView;
    }


    @Override
    public String getTittle() {
        return "Milhas Infantis";
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

        textTitle.setText("Gráfico do total de bonificações, penalizações e pontos extras por filho " +
                "e tipo de ponto no período de "
                + Utils.formatDate(dateIni) + " a " + Utils.formatDate(dateEnd));

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yValsRed = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yValsYellow = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yValsGreen = new ArrayList<BarEntry>();

        List<Children> childList = childrenDAO.consultarChildrenList();
        if (childList != null && childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++){
                Children c = childList.get(i);
                String[] completeName = c.getName().split(" ");
                String name = completeName[0];
                xVals.add(name);

                int redPoint = realizationDAO.consultarTotalRedActions(c.getId(), dateIni, dateEnd);
                int yellowPoint = realizationDAO.consultarTotalYellowActions(c.getId(), dateIni, dateEnd);
                int greenPoint = realizationDAO.consultarTotalGreenActions(c.getId(), dateIni, dateEnd);

                yValsRed.add(new BarEntry(redPoint, i));
                yValsYellow.add(new BarEntry(yellowPoint, i));
                yValsGreen.add(new BarEntry(greenPoint, i));

            }

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
            textTitle.setVisibility(View.VISIBLE);
            noPoint.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);

            legendXText.setVisibility(View.VISIBLE);
            legendXText.setText(" :( atividade não realizada, : | " +
                    "atividade realizada de maneira pouco satisfatória e" +
                    " : ) atividade realizada com sucesso ");

        }else{
            noPoint.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
            textTitle.setVisibility(View.GONE);
        }


    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
