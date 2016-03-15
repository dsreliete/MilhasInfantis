package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 10/1/15.
 */
public class ChildrenDetailsFragment extends BaseFragment {

    @Bind(R.id.child_image) ImageView childImageView;
    @Bind(R.id.total_poinstTextView) TextView totalPointsTextView;
    @Bind(R.id.today_pointsTextView) TextView todayPointsTextView;
    @Bind(R.id.week_pointsTextView) TextView weekPointsTextView;
    @Bind(R.id.association_button) Button associationButton;
    @Bind(R.id.plus_point) ImageView plusImageView;
    @Bind(R.id.minus_point) ImageView minusImageView;
    @Bind(R.id.settings) ImageView settingsImageView;
    @Bind(R.id.like) ImageView likeImageView;
    @Bind(R.id.unlike) ImageView unlikeImageView;
    @Bind(R.id.graph) ImageView graphImageView;
    @Bind(R.id.history) ImageView historyImageView;
    @Bind(R.id.awards) ImageView awardsImageView;

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private ChildrenDAO childrenDAO;
    private RealizationDAO realizationDAO;
    private Children children;
    private int idChildren;
    private String time;
    private String date;
    private int totalPoints;
    private int weekTotalPoints;
    private int todayTotalPoints;
    private String endDate;
    private String iniDate;

    public static ChildrenDetailsFragment newInstance(int id){
        ChildrenDetailsFragment fragment = new ChildrenDetailsFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_children_details, container, false);
        ButterKnife.bind(this, rootView);

        childrenDAO = new ChildrenDAO(getActivity());
        idChildren = getArguments().getInt("ID");
        if(idChildren > 0) {
            children = childrenDAO.consultarPorId(idChildren);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(children.getName());
        }

        time = Utils.getTimeNow();
        date = Utils.getTodayDate();
        iniDate = Utils.getFirstDayOfWeek();
        endDate = Utils.getLastDayOfWeek();

        if (children.getGender().equals("F")) {
            childImageView.setImageResource(R.drawable.maria);
        } else if (children.getGender().equals("M")) {
            childImageView.setImageResource(R.drawable.joao);
        } else {
            childImageView.setImageResource(R.drawable.joao);
        }

        realizationDAO = new RealizationDAO(getActivity());
        totalPoints = realizationDAO.consultarTotalPointsPerChild(idChildren);
        todayTotalPoints = realizationDAO.consultarTotalPointsPerChildToday(idChildren, date);
        weekTotalPoints = realizationDAO.consultarTotalPointsPerChildWeek(idChildren, iniDate, endDate);
        setPoints(totalPoints, todayTotalPoints, weekTotalPoints);

        return rootView;
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
    public String getTittle() {
        return getResources().getString(R.string.app_name);
    }

    @OnClick (R.id.association_button) public void associateGoal(){
        initFragment(GoalsAssociationFragment.newInstance(idChildren));
    }

    @OnClick(R.id.plus_point) public void quickBonificate(){
        boolean sucess = realizationDAO.inserir(idChildren, 0, 0, "ponto_extra", 1, "verde", date, time);
        if (sucess){
            totalPoints = realizationDAO.consultarTotalPointsPerChild(idChildren);
            todayTotalPoints = realizationDAO.consultarTotalPointsPerChildToday(idChildren, date);
            weekTotalPoints = realizationDAO.consultarTotalPointsPerChildWeek(idChildren, iniDate, endDate);
            setPoints(totalPoints, todayTotalPoints, weekTotalPoints);
            Toast.makeText(getActivity(), "Bonificação realizada com sucesso!!! Vale 1 ponto", Toast.LENGTH_SHORT).show();
        }else{
            showSnackBar(getResources().getString(R.string.snack_no_bonification));
        }
    }

    @OnClick(R.id.minus_point) public void quickPenalizate(){
        boolean sucess = realizationDAO.inserir(idChildren, 0, 0, "ponto_extra", -1, "vermelho", date, time);
        if (sucess){
            totalPoints = realizationDAO.consultarTotalPointsPerChild(idChildren);
            todayTotalPoints = realizationDAO.consultarTotalPointsPerChildToday(idChildren, date);
            weekTotalPoints = realizationDAO.consultarTotalPointsPerChildWeek(idChildren, iniDate, endDate);
            setPoints(totalPoints, todayTotalPoints, weekTotalPoints);
            Toast.makeText(getActivity(), "Penalização realizada com sucesso!!! Vale -1 ponto", Toast.LENGTH_SHORT).show();
        }else{
            showSnackBar(getResources().getString(R.string.snack_no_penalization));
        }

    }

    @OnClick(R.id.settings) public void configuration(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setIcon(R.drawable.icon_action_bar);
            alert.setTitle(getResources().getString(R.string.app_name));
            alert.setMessage(getResources().getString(R.string.alert_option_choose));
            alert.setPositiveButton(getResources().getString(R.string.alert_button_edit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initFragment(ChildrenRegisterFragment.newInstance("EDITION", children.getId()));
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
                    if(childrenDAO.deletarId(children.getId())){
                        dialog.cancel();
                        initFragment(new ChildrenFragment());
                    }else{
                        showSnackBar(getResources().getString(R.string.snack_del));
                    }
                }
            });
            alert.show();
    }

    @OnClick (R.id.like) public void bonificate(){
        initFragment(LikeFragment.newInstance(idChildren));
    }

    @OnClick(R.id.unlike) public void penalizate(){
        initFragment(UnlikeFragment.newInstance(idChildren));
    }

    @OnClick (R.id.graph) public void graph(){
        initFragment(ChildrenGraphFragment.newInstance(idChildren));
    }

    @OnClick(R.id.awards) public void awards(){
        initFragment(ChildrenRescuePointsFragment.newInstance(idChildren));
    }

    @OnClick(R.id.history) public void history(){
        initFragment(ChildrenHistoryFragment.newInstance(idChildren));
    }

    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack("back")
                .commit();
    }

    public void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }
}
