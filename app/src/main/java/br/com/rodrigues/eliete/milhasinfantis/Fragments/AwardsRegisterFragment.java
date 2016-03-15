package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 9/20/15.
 */
public class AwardsRegisterFragment extends BaseFragment {

    @Bind(R.id.descEditText) EditText descAwards;
    @Bind(R.id.pointsEditText) EditText pointsAwards;
    @Bind(R.id.text_input_layout_awds_desc) TextInputLayout textInputLayoutDesc;
    @Bind(R.id.text_input_layout_awds_points) TextInputLayout textInputLayoutPoints;
    @Bind(R.id.ok_button) Button okButton;

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    public static final String TAG = "MilhasApplication";
    public static final String ACTION = " ";
    private AwardsDAO awardsDAO;
    private Awards awards;

    public static AwardsRegisterFragment newInstance(String action, Integer id){
        AwardsRegisterFragment fragment = new AwardsRegisterFragment();
        Bundle b = new Bundle();
        b.putString(ACTION, action);
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_awards_register, container, false);
        ButterKnife.bind(this, rootView);

        awardsDAO = new AwardsDAO(getActivity());

        if(getArguments().get(ACTION).equals("EDITION")) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.awards_edit));
            int id = getArguments().getInt("ID");
            if (id > 0) {
                awards = awardsDAO.consultarPorId(id);
                if (awards != null) {
                    descAwards.setText(awards.getDescription());
                    pointsAwards.setText(awards.getPoints() + "");
                }
            }
        }else if (getArguments().get(ACTION).equals("REGISTRATION")){
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.awards_add));
        }

        Utils.cleanErrorMessage(descAwards, textInputLayoutDesc);
        Utils.cleanErrorMessage(pointsAwards, textInputLayoutPoints);
        textInputLayoutDesc.setErrorEnabled(true);
        textInputLayoutPoints.setErrorEnabled(true);

        return rootView;
    }

    @OnClick(R.id.ok_button) public void registerAwards() {
        String description = descAwards.getText().toString();
        String points = pointsAwards.getText().toString();

        if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (points.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutPoints, getResources().getString(R.string.points_value));
        } else {
            boolean sucess = false;
            String msg = "";

            if (getArguments().get(ACTION).equals("EDITION")) {
                sucess = awardsDAO.atualizar(awards.getId(), description, Integer.parseInt(points));
                msg = getResources().getString(R.string.snack_no_edition);
            }else{
                if(!verifyRegistration(description)){
                    awards = new Awards(description, Integer.parseInt(points));
                    sucess = awardsDAO.inserir(awards);
                    msg = getResources().getString(R.string.snack_no_add);
                }else{
                    showSnackBar("prêmio  " + getResources().getString(R.string.snack_same));
                }
            }

            if (sucess) {
                renderAwardsFragment();
            } else {
                showSnackBar(msg);
            }
            descAwards.setText(" ");
            pointsAwards.setText(" ");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void renderAwardsFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AwardsFragment(), FRAGMENT_TAG)
                .addToBackStack("back")
                .commit();
    }

    public void showSnackBar(String message){
        Snackbar.make(getActivity().findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String description){
        List<Awards> awardsList = awardsDAO.consultarAwardsList();

        if (awardsList != null) {
            for (int i = 0; i < awardsList.size(); i++){
                Awards a = awardsList.get(i);
                if (a.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTittle() {
        return " ";
    }

}
