package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Adapters.DislikeListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 3/23/16.
 */
public class DislikeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
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

    private PenaltiesDAO penaltiesDAO;
    private RealizationDAO realizationDAO;
    private List<Penalties> penaltiesList;
    private DislikeListAdapter unlikeListAdapter;
    private int idChild;
    private String nameChild;
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dislike);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            idChild = savedInstanceState.getInt(ChildrenDetailActivity.ID);
            nameChild = savedInstanceState.getString(ChildrenDetailActivity.NAME);
        } else {
            if (getIntent() != null) {
                idChild = getIntent().getIntExtra(ChildrenDetailActivity.ID, 0);
                nameChild = getIntent().getExtras().getString(ChildrenDetailActivity.NAME);
            }
        }

        getSupportActionBar().setTitle(nameChild);
        realizationDAO = new RealizationDAO(this);

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
                DatePickerDialog dpd = DatePickerDialog.newInstance(DislikeActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.setAccentColor(getResources().getColor(R.color.primaryColor));
                dpd.show(getFragmentManager(), null);
            }
        });

        penaltiesDAO = new PenaltiesDAO(this);

        penaltiesList = penaltiesDAO.fetchPenaltiesList();
        if(penaltiesList != null){
            if(penaltiesList.size() == 0){
                cardView.setVisibility(View.VISIBLE);
                noPenaltyText.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }else {
                cardView.setVisibility(View.GONE);
                noPenaltyText.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);

                unlikeListAdapter = new DislikeListAdapter(penaltiesList);
                penaltyRecyclerView.setAdapter(unlikeListAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);
                penaltyRecyclerView.setLayoutManager(layoutManager);
                penaltyRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
                penaltyRecyclerView.setHasFixedSize(true);
                penaltyRecyclerView.addOnItemTouchListener(RecycleTouchListener);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ChildrenDetailActivity.class);
                i.putExtra(ChildrenDetailActivity.ID, idChild);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView.OnItemTouchListener RecycleTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = penaltyRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null){
                int position = penaltyRecyclerView.getChildAdapterPosition(child);
                boolean sucess = false;
                Penalties pen = penaltiesList.get(position);

                int point = pen.getPoint();
                point *= -1;

                if(dateEditText.getText().length() == 0){
                    Utils.setEmptyMessage(textInputLayoutdate, getResources().getString(R.string.date));
                }else{
                    sucess = realizationDAO.insertRealization(idChild, pen.getId(), 0, "penalizar", pen.getPoint(), "vermelho", date, time);
                }

                if (dateEditText.getText().length() > 0) {
                    if (sucess) {
                        Toast.makeText(DislikeActivity.this, "Penalização realizada com sucesso!!! Vale -" + point + " pontos", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(DislikeActivity.this.findViewById(android.R.id.content), getResources().getString(R.string.snack_no_penalization),
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ChildrenDetailActivity.ID, idChild);
        outState.putString(ChildrenDetailActivity.NAME, nameChild);
    }

}
