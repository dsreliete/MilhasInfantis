package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.UnlikeListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/21/15.
 */
public class UnlikeFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener{

    @Bind(R.id.penalty_recyclerView)
    RecyclerView penaltyRecyclerView;
    @Bind(R.id.no_penaltyText)
    TextView noPenaltyText;
    @Bind(R.id.cardView)
    CardView cardView;
    @Bind(R.id.text)
    TextView textView;
    @Bind(R.id.date)
    EditText dateEditText;
    @Bind(R.id.text_input_layout_date)
    TextInputLayout textInputLayoutdate;

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private PenaltiesDAO penaltiesDAO;
    private RealizationDAO realizationDAO;
    private List<Penalties> penaltiesList;
    private UnlikeListAdapter unlikeListAdapter;
    private int idChild;
    private String date;
    private String time;

    public static UnlikeFragment newInstance(int id) {
        UnlikeFragment fragment = new UnlikeFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_unlike, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);

        if(getArguments() != null){
            idChild = getArguments().getInt("ID");
        }

        realizationDAO = new RealizationDAO(getActivity());

        textInputLayoutdate.setErrorEnabled(true);
        Utils.cleanErrorMessage(dateEditText, textInputLayoutdate);

        time =  Utils.getTimeNow();
        date = Utils.getTodayDate();

        dateEditText.setText(Utils.formatDate(Utils.getTodayDate()));
        dateEditText.setFocusableInTouchMode(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(UnlikeFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getActivity().getFragmentManager(), null);
            }
        });

        penaltiesDAO = new PenaltiesDAO(getActivity());

        penaltiesList = penaltiesDAO.consultarPenaltiesList();
        if(penaltiesList != null){
            if(penaltiesList.size() == 0){
                cardView.setVisibility(View.VISIBLE);
                noPenaltyText.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }else {
                cardView.setVisibility(View.GONE);
                noPenaltyText.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);

                unlikeListAdapter = new UnlikeListAdapter(penaltiesList);
                penaltyRecyclerView.setAdapter(unlikeListAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                penaltyRecyclerView.setLayoutManager(layoutManager);
                penaltyRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                penaltyRecyclerView.setHasFixedSize(true);
                penaltyRecyclerView.addOnItemTouchListener(RecycleTouchListener);
            }
        }

        return rootView;
    }



    @Override
    public String getTittle() {
        return " ";
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
                boolean sucess = false;
                Penalties pen = penaltiesList.get(position);

                int point = pen.getPoint();
                point *= -1;

                if(dateEditText.getText().length() == 0){
                    Utils.setEmptyMessage(textInputLayoutdate, getResources().getString(R.string.date));
                }else{
                    sucess = realizationDAO.inserir(idChild, pen.getId(), 0, "penalizar", pen.getPoint(), "vermelho", date, time);
                }

                if (dateEditText.getText().length() > 0) {
                    if (sucess) {
                        Toast.makeText(getActivity(), "Penalização realizada com sucesso!!! Vale -" + point + " pontos", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.snack_no_penalization),
                                Snackbar.LENGTH_SHORT)
                                .setActionTextColor(Color.WHITE)
                                .show();
                    }
                }
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
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String d;
        String m;
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
}
