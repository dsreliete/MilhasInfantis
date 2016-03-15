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
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 9/28/15.
 */
public class PenaltiesRegisterFragment extends BaseFragment {

    @Bind(R.id.descEditText) EditText descPenalties;
    @Bind(R.id.pointEditText) EditText point;
    @Bind(R.id.text_input_layout_penalty_desc)  TextInputLayout textInputLayoutDesc;
    @Bind(R.id.text_input_layout_penalty_point) TextInputLayout textInputLayoutPoint;
    @Bind(R.id.ok_button) Button okButton;

    public static final String TAG = "MilhasApplication";
    public static final String ACTION = " ";
    private PenaltiesDAO penaltiesDAO;
    private Penalties penalties;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static PenaltiesRegisterFragment newInstance(String action, Integer id) {
        PenaltiesRegisterFragment fragment = new PenaltiesRegisterFragment();
        Bundle b = new Bundle();
        b.putString(ACTION, action);
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public String getTittle() {
        return "" ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_penalties_register, container, false);
        ButterKnife.bind(this, rootView);

        penaltiesDAO = new PenaltiesDAO(getActivity());

        if (getArguments().get(ACTION).equals("REGISTRATION")){
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.penalties_add));

        }else if (getArguments().get(ACTION).equals("EDITION")){
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.penalties_edit));

            int id = getArguments().getInt("ID");
            if(id > 0) {
                penalties = penaltiesDAO.consultarPorId(id);
                if (penalties != null) {
                    descPenalties.setText(penalties.getDescription());
                    point.setText(penalties.getPoint() + "");
                }
            }
        }

        Utils.cleanErrorMessage(descPenalties, textInputLayoutDesc);
        Utils.cleanErrorMessage(point, textInputLayoutPoint);
        textInputLayoutDesc.setErrorEnabled(true);
        textInputLayoutPoint.setErrorEnabled(true);

        return rootView;
    }

    @OnClick(R.id.ok_button) public void registerAwards() {
        String description = descPenalties.getText().toString();
        String points = point.getText().toString();

        if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (points.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutPoint, getResources().getString(R.string.points_value));
        } else {
            boolean sucess = false;
            String msg = "";

            if (getArguments().get(ACTION).equals("EDITION")){
                sucess = penaltiesDAO.atualizar(penalties.getId(), description, Integer.parseInt(points));
                msg = getResources().getString(R.string.snack_no_edition);
            }else{
                if (!verifyRegistration(description)){
                    int penaltyPoint = Integer.parseInt(points);
                    penaltyPoint *= -1;
                    penalties = new Penalties(description, penaltyPoint);
                    sucess = penaltiesDAO.inserir(penalties);
                    msg = getResources().getString(R.string.snack_no_add);
                }else{
                    showSnackBar("Penalidade " + getResources().getString(R.string.snack_same));
                }
            }

            if (sucess) {
                renderPenaltiesFragment();
            } else {
                showSnackBar(msg);
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void renderPenaltiesFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PenaltiesFragment())
                .addToBackStack("back")
                .commit();
    }

    public void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)
                .show();
    }

    public boolean verifyRegistration(String description){
        List<Penalties> p = penaltiesDAO.consultarPenaltiesList();

        if (p != null){
            for (int i = 0; i < p.size(); i++){
                Penalties penalties = p.get(i);
                if (penalties.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }
}