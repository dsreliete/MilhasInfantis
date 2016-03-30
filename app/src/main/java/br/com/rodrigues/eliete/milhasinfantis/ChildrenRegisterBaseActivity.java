package br.com.rodrigues.eliete.milhasinfantis;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by eliete on 3/16/16.
 */
public abstract class ChildrenRegisterBaseActivity extends AppCompatActivity {


    public abstract boolean verifyRegistration(String name);
    public abstract void showSnackBar(String message);
    public abstract void registerChild();


}
