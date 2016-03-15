package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.rodrigues.eliete.milhasinfantis.Fragments.GoalsExcludeAssociationFragment;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.GoalsIncludeAssociationFragment;

/**
 * Created by eliete on 10/9/15.
 */
public class AssociationPagerAdapter extends FragmentPagerAdapter {

    private int idChild;

    public AssociationPagerAdapter(FragmentManager fm, int idChild) {
        super(fm);
        this.idChild = idChild;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return GoalsIncludeAssociationFragment.newInstance(idChild);//Atribuir Assciação
        }else{
            return GoalsExcludeAssociationFragment.newInstance(idChild) ; //Retirar Associação
        }

    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Atribuir Atividade";
        return "Retirar Atribuição";
    }

    @Override
    public int getCount() {
        return 2;
    }

}
