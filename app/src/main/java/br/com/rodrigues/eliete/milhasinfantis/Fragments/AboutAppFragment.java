package br.com.rodrigues.eliete.milhasinfantis.Fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.MainActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 22/07/15.
 */
public class AboutAppFragment extends BaseFragment {

    @Bind(R.id.about) TextView about;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aboutapp, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        ButterKnife.bind(this, rootView);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());

        // Version
        PackageInfo pInfo;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            StringBuilder sb = new StringBuilder(getResources().getString(R.string.simbol_rights));
            sb.append(" ");
            sb.append(year);
            sb.append(" ");
            sb.append(getResources().getString(R.string.app_name));
            sb.append(" ");
            sb.append(getResources().getString(R.string.name_rights));
            sb.append(" ");
            sb.append(version);
            about.setText(sb.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    @Override
    public String getTittle() {
        return getResources().getString(R.string.app_name);
    }
}
