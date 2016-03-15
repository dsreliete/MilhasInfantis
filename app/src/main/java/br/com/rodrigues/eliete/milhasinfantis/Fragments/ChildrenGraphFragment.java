package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Widget.CircleImageView;

/**
 * Created by eliete on 10/31/15.
 */
public class ChildrenGraphFragment extends BaseFragment {

    public static final String TAG = "MilhasApplication";
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private int idChild;

    public static ChildrenGraphFragment newInstance(int id) {
        ChildrenGraphFragment fragment = new ChildrenGraphFragment();
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Gráficos");
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        setHasOptionsMenu(false);

        if (getArguments() != null) {
            idChild = getArguments().getInt("ID");
        }

        CircleImageView totalImage = (CircleImageView) rootView.findViewById(R.id.total_image);
        totalImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initFragment(ChildrenGraphFragmentTotal.newInstance(idChild));
                return true;
            }
        });

        CircleImageView bonPenImage = (CircleImageView) rootView.findViewById(R.id.bon_pen_image);
        bonPenImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initFragment(ChildrenGraphFragmentBonPen.newInstance(idChild));
                return true;
            }
        });

        CircleImageView goalImage = (CircleImageView) rootView.findViewById(R.id.goal_image);
        goalImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initFragment(ChildrenGraphFragmentGoal.newInstance(idChild));
                return true;
            }
        });

        return rootView;
    }

    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack("back")
                .commit();
    }


        @Override
    public String getTittle() {
        return "Gráficos";
    }
}
