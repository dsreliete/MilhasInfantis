package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 9/23/15.
 */
public class GoalsRegisterFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.descEditText) EditText descGoals;
    @Bind(R.id.redPointEditText) EditText redPoint;
    @Bind(R.id.yellowPointEditText) EditText yellowPoint;
    @Bind(R.id.greenPointEditText) EditText greenPoint;
    @Bind(R.id.text_input_layout_goals_desc) TextInputLayout textInputLayoutDesc;
    @Bind(R.id.text_input_layout_goals_greenPoint) TextInputLayout textInputLayoutGreenPoint;
    @Bind(R.id.text_input_layout_goals_yellowPoint) TextInputLayout textInputLayoutYellowPoint;
    @Bind(R.id.text_input_layout_goals_redPoint) TextInputLayout textInputLayoutRedPoint;
    @Bind(R.id.spinner_goals_category) Spinner goalsCategorySpinner;
    @Bind(R.id.ok_button) Button okButton;
    @Bind(R.id.happy) ImageView happyImageView;
    @Bind(R.id.soso) ImageView sosoImageView;
    @Bind(R.id.angry) ImageView angryImageView;

    public static final String TAG = "MilhasApplication";
    public static final String ACTION = " ";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private GoalsDAO goalsDAO;
    private CategoriesDAO categoriesDAO;
    private Goals goals;
    private List<Categories> categoriesList;
    private ArrayAdapter<Categories> arrayAdapter;
    private int categoryId;
    private Categories cat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static GoalsRegisterFragment newInstance(String action, Integer id, int position){
        GoalsRegisterFragment fragment = new GoalsRegisterFragment();
        Bundle b = new Bundle();
        b.putString(ACTION, action);
        b.putInt("ID", id);
        b.putInt("SPINNER", position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_goals_register, container, false);
        ButterKnife.bind(this, rootView);

        goalsDAO = new GoalsDAO(getActivity());
        categoriesDAO = new CategoriesDAO(getActivity());
        categoriesList = categoriesDAO.consultarCategoriesList();
        categoriesList.add(new Categories(999, getResources().getString(R.string.category_desc5)));
        categoriesList.add(new Categories(1000, getResources().getString(R.string.category_desc6)));

        arrayAdapter = new ArrayAdapter<Categories>(getActivity(), android.R.layout.simple_spinner_item, categoriesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsCategorySpinner.setAdapter(arrayAdapter);
        goalsCategorySpinner.setOnItemSelectedListener(this);


        if(getArguments() != null){
            categoryId = getArguments().getInt("ID");
            int position = getArguments().getInt("SPINNER");
            if(categoryId == 1000){
                goalsCategorySpinner.setSelection(categoriesList.size() - 1);
            }else if (categoryId == 0){
                goalsCategorySpinner.setSelection(categoriesList.size() - 3);
            }else{
                goalsCategorySpinner.setSelection(position);
            }
        }

        Log.e("Eliete", "GOALS REGISTER FRAGMENT");
        for (Categories c : categoriesList){
            Log.e("Eliete", "categories DESCRIPTION " + c.getDescription());
            Log.e("Eliete", "categories ID " + c.getId());
        }

        if(getArguments().get(ACTION).equals("EDITION")){
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.goals_edit));
            categoryId = getArguments().getInt("ID");
            if(categoryId > 0){
                goals = goalsDAO.consultarGoalPorId(categoryId);
                if(goals != null){
                    descGoals.setText(goals.getDescription());
                    String a = goals.getDescription();
                    redPoint.setText(goals.getRedPoint() + "");
                    greenPoint.setText(goals.getGreenPoint() + "");
                    yellowPoint.setText(goals.getYellowPoint() + "");
                }
            }
        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.goals_add));
        }



        Utils.cleanErrorMessage(descGoals, textInputLayoutDesc);
        Utils.cleanErrorMessage(redPoint, textInputLayoutRedPoint);
        Utils.cleanErrorMessage(greenPoint, textInputLayoutGreenPoint);
        Utils.cleanErrorMessage(yellowPoint, textInputLayoutYellowPoint);
        textInputLayoutDesc.setErrorEnabled(true);
        textInputLayoutRedPoint.setErrorEnabled(true);
        textInputLayoutYellowPoint.setErrorEnabled(true);
        textInputLayoutGreenPoint.setErrorEnabled(true);

        return rootView;
    }

    @OnClick(R.id.ok_button) public void registerAwards() {
        String description = descGoals.getText().toString();
        String redPoints = redPoint.getText().toString();
        String yellowPoints = yellowPoint.getText().toString();
        String greenPoints = greenPoint.getText().toString();

        //get category
        Categories cat = (Categories) goalsCategorySpinner.getSelectedItem();
        int catId = cat.getId();

        if (catId == 1000) {
            showSnackBar(getResources().getString(R.string.category_desc6));
        }else if(catId == 999){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CategoriesRegisterFragment.newInstance("GOALS", 0))
                    .addToBackStack("back")
                    .commit();
        } else if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (yellowPoints.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutYellowPoint, getResources().getString(R.string.points_value));
        } else if (redPoints.isEmpty()){
            Utils.setEmptyMessage(textInputLayoutRedPoint, getResources().getString(R.string.points_value));
        } else if (greenPoints.isEmpty()){
            Utils.setEmptyMessage(textInputLayoutGreenPoint, getResources().getString(R.string.points_value));
        } else {
            boolean sucess = false;
            String msg = "";

            if (getArguments().get(ACTION).equals("EDITION")) {
                sucess = goalsDAO.atualizar(goals.getId(), description, Integer.parseInt(redPoints), Integer.parseInt(yellowPoints), Integer.parseInt(greenPoints), catId);
                msg = getResources().getString(R.string.snack_no_edition);

            }else{
                if (!verifyRegistration(description)){
                    goals = new Goals(description, Integer.parseInt(redPoints), Integer.parseInt(yellowPoints), Integer.parseInt(greenPoints), catId);
                    sucess = goalsDAO.inserir(goals);
                    msg = getResources().getString(R.string.snack_no_add);
                }else{
                    showSnackBar("Atividade " + getResources().getString(R.string.snack_same));
                }
            }

            if (sucess) {
                renderGoalsFragment(catId);
            } else {
                showSnackBar(msg);
            }

            descGoals.setText(" ");
            redPoint.setText(" ");
            greenPoint.setText(" ");
            yellowPoint.setText(" ");
            goalsCategorySpinner.setSelection(categoriesList.size() - 1);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void renderGoalsFragment(int id){
        Log.e("Eliete", "catId " + id);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, GoalsFragment.newInstance(id))
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
        List<Goals> g = goalsDAO.consultarGoalsListPerCategory(categoryId);

        if (g != null){
            for (Goals goal : g){
                if (goal.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }

    public String getTittle() {
        return getResources().getString(R.string.goals);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Categories cat = (Categories) goalsCategorySpinner.getSelectedItem();
        int catId = cat.getId();
        if(catId == 999){
            descGoals.setVisibility(View.INVISIBLE);
            redPoint.setVisibility(View.INVISIBLE);
            yellowPoint.setVisibility(View.INVISIBLE);
            greenPoint.setVisibility(View.INVISIBLE);
            textInputLayoutDesc.setVisibility(View.INVISIBLE);
            textInputLayoutGreenPoint.setVisibility(View.INVISIBLE);
            textInputLayoutYellowPoint.setVisibility(View.INVISIBLE);
            textInputLayoutRedPoint.setVisibility(View.INVISIBLE);
            happyImageView.setVisibility(View.INVISIBLE);
            sosoImageView.setVisibility(View.INVISIBLE);
            angryImageView.setVisibility(View.INVISIBLE);
        }else{
            descGoals.setVisibility(View.VISIBLE);
            redPoint.setVisibility(View.VISIBLE);
            yellowPoint.setVisibility(View.VISIBLE);
            greenPoint.setVisibility(View.VISIBLE);
            textInputLayoutDesc.setVisibility(View.VISIBLE);
            textInputLayoutGreenPoint.setVisibility(View.VISIBLE);
            textInputLayoutYellowPoint.setVisibility(View.VISIBLE);
            textInputLayoutRedPoint.setVisibility(View.VISIBLE);
            happyImageView.setVisibility(View.VISIBLE);
            sosoImageView.setVisibility(View.VISIBLE);
            angryImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
