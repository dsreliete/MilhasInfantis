package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Adapters.ChildrenListAdapter;
import br.com.rodrigues.eliete.milhasinfantis.Dao.ChildrenDAO;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Widget.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eliete on 22/07/15.
 */
public class ChildrenFragment extends BaseFragment {

    @Bind(R.id.child_recyclerView)
    RecyclerView childRecyclerView;
    @Bind(R.id.fab_add_children)
    FloatingActionButton fabAddChild;

    private ChildrenListAdapter childrenListAdapter;
    private ChildrenDAO childrenDAO;
    private List<Children> childrenList;
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_children, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        ButterKnife.bind(this, rootView);

        childrenDAO = new ChildrenDAO(getActivity());
        childrenList = childrenDAO.consultarChildrenList();
        if (childrenList != null && childrenList.size() > 0) {
            childrenListAdapter = new ChildrenListAdapter(childrenList);
            childRecyclerView.setAdapter(childrenListAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            childRecyclerView.setLayoutManager(layoutManager);
            childRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            childRecyclerView.setHasFixedSize(true);
            childRecyclerView.addOnItemTouchListener(RecycleTouchListener);
        }

        return rootView;
    }

    @OnClick (R.id.fab_add_children) public void addChild(View v){
        initFragment(ChildrenRegisterFragment.newInstance("ADDITION", 0));
    }

    final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

        @Override public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

    });

    private RecyclerView.OnItemTouchListener RecycleTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = childRecyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && mGestureDetector.onTouchEvent(e)){
                int position = childRecyclerView.getChildAdapterPosition(child);

            final Children c = childrenList.get(position);
                initFragment(ChildrenDetailsFragment.newInstance(c.getId()));
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
    public String getTittle() {
        return getResources().getString(R.string.app_name);
    }


    public void initFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG)
                .addToBackStack("back")
                .commit();
    }
}
