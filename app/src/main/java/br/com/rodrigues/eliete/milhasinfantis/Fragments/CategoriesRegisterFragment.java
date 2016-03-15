package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 9/20/15.
 */
public class CategoriesRegisterFragment extends BaseFragment {
    @Bind(R.id.descEditText)
    EditText descCategories;
    @Bind(R.id.text_input_layout_cat_desc)
    TextInputLayout textInputLayoutDesc;

    @Bind(R.id.ok_button)
    Button okButton;

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    public static final String TAG = "MilhasApplication";
    public static final String ACTION = " ";
    private CategoriesDAO categoriesDAO;
    private Categories categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static CategoriesRegisterFragment newInstance(String action, Integer id){
        CategoriesRegisterFragment fragment = new CategoriesRegisterFragment();
        Bundle b = new Bundle();
        b.putString(ACTION, action);
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_categories_register, container, false);
        ButterKnife.bind(this, rootView);

        categoriesDAO = new CategoriesDAO(getActivity());

        if(getArguments().get(ACTION).equals("EDITION")){
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.categories_edit));
            int id = getArguments().getInt("ID");
            if(id > 0){
                categories = categoriesDAO.consultarPorId(id);
                if(categories != null){
                    descCategories.setText(categories.getDescription());
                }
            }
        }else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.categories_add));
        }

        Utils.cleanErrorMessage(descCategories, textInputLayoutDesc);
        textInputLayoutDesc.setErrorEnabled(true);

        return rootView;
    }

    @OnClick(R.id.ok_button) public void registerCategories() {
        String description = descCategories.getText().toString();

        if (description.isEmpty()) {
            Utils.setEmptyMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else if (!Utils.validateText(description)){
            Utils.setTextMessage(textInputLayoutDesc, getResources().getString(R.string.description));
        } else {
            boolean sucess = false;

            if (getArguments().get(ACTION).equals("EDITION")) {
                sucess = categoriesDAO.atualizar(categories.getId(), description);
                if (!sucess){
                    showSnackBar(getResources().getString(R.string.snack_no_edition));
                }
                initFragment(new CategoriesFragment());
            }else{
                if (!verifyRegistration(description)){
                    categories = new Categories(description);
                    sucess = categoriesDAO.inserir(categories);
                    Fragment f = (getArguments().get(ACTION).equals("GOALS")) ? GoalsRegisterFragment.newInstance("", 0, 0) : new CategoriesFragment();
                    if (sucess) {
                        initFragment(f);
                    }else{
                        showSnackBar(getResources().getString(R.string.snack_no_add));
                    }
                }else{
                    showSnackBar("Categoria " + getResources().getString(R.string.snack_same));
                }
            }

            descCategories.setText(" ");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void initFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
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
        List<Categories> c = categoriesDAO.consultarCategoriesList();

        if (c != null){
            for (int i = 0; i < c.size(); i++){
                Categories cat = c.get(i);
                if (cat.getDescription().equals(description)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTittle() {
        return "Categorias";
    }
}
