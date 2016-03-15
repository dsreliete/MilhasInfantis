package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.AssociationPagerAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.IncludeExclude;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/2/15.
 */
public class GoalsAssociationFragment extends BaseFragment {

    @Bind(R.id.tabs) TabLayout tabs;
    @Bind(R.id.viewpager) ViewPager viewPager;

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private int idChild;
    private ChildrenDAO childrenDAO;
    private Children children;

    public static GoalsAssociationFragment newInstance(int id){
        GoalsAssociationFragment fragment = new GoalsAssociationFragment();
        Bundle b = new Bundle();
        b.putInt("ID", id);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_association, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);

        if(getArguments() != null){
            idChild = getArguments().getInt("ID");
        }

        childrenDAO = new ChildrenDAO(getActivity());
        if(idChild > 0) {
            children = childrenDAO.consultarPorId(idChild);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(children.getName());
        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        final AssociationPagerAdapter adapter = new AssociationPagerAdapter(getChildFragmentManager(), idChild);
        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                IncludeExclude fragment = (IncludeExclude) adapter.instantiateItem(viewPager, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }



    @Override
    public String getTittle() {
        return getResources().getString(R.string.association);
    }

}
