package br.com.rodrigues.eliete.milhasinfantis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.SettingsDialogFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by eliete on 3/16/16.
 */
public class ChildrenDetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.child_image)
    ImageView childImageView;
    @Bind(R.id.total_poinstTextView)
    TextView totalPointsTextView;
    @Bind(R.id.today_pointsTextView) TextView todayPointsTextView;
    @Bind(R.id.week_pointsTextView) TextView weekPointsTextView;
    @Bind(R.id.association_button)
    Button associationButton;
    @Bind(R.id.plus_point) ImageView plusImageView;
    @Bind(R.id.minus_point) ImageView minusImageView;
    @Bind(R.id.settings) ImageView settingsImageView;
    @Bind(R.id.like) ImageView likeImageView;
    @Bind(R.id.unlike) ImageView unlikeImageView;
    @Bind(R.id.graph) ImageView graphImageView;
    @Bind(R.id.history) ImageView historyImageView;
    @Bind(R.id.awards) ImageView awardsImageView;

    private ChildrenDAO childrenDAO;
    private RealizationDAO realizationDAO;
    private Children children;
    private int idChildren;
    private String nameChildren;
    private String genderChildren;
    private String time;
    private String date;
    private int totalPoints;
    private int weekTotalPoints;
    private int todayTotalPoints;
    private String endDate;
    private String iniDate;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String GENERAL_TYPE = "general";
    public static final String CHILDREN_TYPE = "children";
    public static final String TYPE = "type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        childrenDAO = new ChildrenDAO(this);

        Intent i = getIntent();
        if (i != null){
            if(i.hasExtra(ID))
                idChildren = i.getExtras().getInt(ID);
        }

        if (idChildren > 0){
            children = childrenDAO.fetchChildrenPerId(idChildren);
            nameChildren = children.getName();
            genderChildren = children.getGender();
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameChildren);

        time = Utils.getTimeNow();
        date = Utils.getTodayDate();
        iniDate = Utils.getFirstDayOfWeek();
        endDate = Utils.getLastDayOfWeek();

        if (genderChildren.equals("F")) {
            childImageView.setImageResource(R.drawable.maria);
        } else if (genderChildren.equals("M")) {
            childImageView.setImageResource(R.drawable.joao);
        } else {
            childImageView.setImageResource(R.drawable.joao);
        }

        realizationDAO = new RealizationDAO(this);
        totalPoints = realizationDAO.fetchTotalPointsPerChild(idChildren);
        todayTotalPoints = realizationDAO.fetchTotalPointsPerChildToday(idChildren, date);
        weekTotalPoints = realizationDAO.fetchTotalPointsPerChildWeek(idChildren, iniDate, endDate);
        setPoints(totalPoints, todayTotalPoints, weekTotalPoints);


    }

    private void setPoints(int total, int today, int week){
        String todaySt;
        String weekSt;
        String totalSt;

        if(total == 1){
            totalSt = "Total: " + total + " ponto";
        }else{
            totalSt = "Total: " + total + " pontos";
        }

        if(today == 1){
            todaySt = "Hoje: total " + today + " ponto";
        }else{
            todaySt = "Hoje: total " + today + " pontos";
        }

        if( week == 1){
            weekSt = "Semana: total " + week + " ponto";
        }else{
            weekSt = "Semana: total " + week + " pontos";
        }

        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableStringBuilder sb1 = new SpannableStringBuilder(todaySt);
        SpannableStringBuilder sb2 = new SpannableStringBuilder(weekSt);
        SpannableStringBuilder sb3 = new SpannableStringBuilder(totalSt);
        sb1.setSpan(bold, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb2.setSpan(bold, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb3.setSpan(bold, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        todayPointsTextView.setText(sb1);
        weekPointsTextView.setText(sb2);
        totalPointsTextView.setText(sb3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.association_button) public void associateGoal(){
        Intent i = new Intent(this, AssociationActivity.class);
        i.putExtra(ID, idChildren);
        i.putExtra(NAME, nameChildren);
        startActivity(i);
    }

    @OnClick(R.id.plus_point) public void quickBonificate(){
        boolean sucess = realizationDAO.insertRealization(idChildren, 0, 0, "ponto_extra", 1, "verde", date, time);
        if (sucess){
            totalPoints = realizationDAO.fetchTotalPointsPerChild(idChildren);
            todayTotalPoints = realizationDAO.fetchTotalPointsPerChildToday(idChildren, date);
            weekTotalPoints = realizationDAO.fetchTotalPointsPerChildWeek(idChildren, iniDate, endDate);
            setPoints(totalPoints, todayTotalPoints, weekTotalPoints);
            Toast.makeText(this, "Bonificação realizada com sucesso!!! Vale 1 ponto", Toast.LENGTH_SHORT).show();
        }else{
            showSnackBar(getResources().getString(R.string.snack_no_bonification));
        }
    }

    @OnClick(R.id.minus_point) public void quickPenalizate(){
        boolean sucess = realizationDAO.insertRealization(idChildren, 0, 0, "ponto_extra", -1, "vermelho", date, time);
        if (sucess){
            totalPoints = realizationDAO.fetchTotalPointsPerChild(idChildren);
            todayTotalPoints = realizationDAO.fetchTotalPointsPerChildToday(idChildren, date);
            weekTotalPoints = realizationDAO.fetchTotalPointsPerChildWeek(idChildren, iniDate, endDate);
            setPoints(totalPoints, todayTotalPoints, weekTotalPoints);
            Toast.makeText(this, "Penalização realizada com sucesso!!! Vale -1 ponto", Toast.LENGTH_SHORT).show();
        }else{
            showSnackBar(getResources().getString(R.string.snack_no_penalization));
        }

    }

    @OnClick(R.id.settings) public void configuration(){
        SettingsDialogFragment settingsDialogFragment = SettingsDialogFragment.newInstance(idChildren);
        settingsDialogFragment.show(getSupportFragmentManager(), "settings");
    }

    @OnClick (R.id.like) public void bonificate(){
        Intent i = new Intent(this, LikeActivity.class);
        i.putExtra(ID, idChildren);
        i.putExtra(NAME, nameChildren);
        startActivity(i);
    }

    @OnClick(R.id.unlike) public void penalizate(){
        Intent i = new Intent(this, DislikeActivity.class);
        i.putExtra(ID, idChildren);
        i.putExtra(NAME, nameChildren);
        startActivity(i);
    }

    @OnClick (R.id.graph) public void graph(){
        Intent i = new Intent(this, ChildrenGraphActivity.class);
        i.putExtra(ID, idChildren);
        i.putExtra(NAME, nameChildren);
        startActivity(i);
    }

    @OnClick(R.id.awards) public void awards(){
        Intent i = new Intent(this, ChildrenRescuePointsActivity.class);
        i.putExtra(ID, idChildren);
        i.putExtra(GENDER, genderChildren);
        i.putExtra(NAME, nameChildren);
        startActivity(i);
    }

    @OnClick(R.id.history) public void history(){
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra(TYPE, CHILDREN_TYPE);
        i.putExtra(ID, idChildren);
        i.putExtra(NAME, nameChildren);
        i.putExtra(GENDER, genderChildren);
        startActivity(i);
    }

    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("back")
                .commit();
    }

    public void showSnackBar(String message) {
        Snackbar.make(this.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

}
