package br.com.rodrigues.eliete.milhasinfantis.Utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by eliete on 9/19/15.
 */
public class Utils {

    public static void cleanErrorMessage(EditText editText, final TextInputLayout textInputLayout){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    textInputLayout.setError(null);
            }
        });

    }

    public static boolean validateText(String string){
        Pattern text = Pattern.compile("^[a-zA-Zà-üÀ-Ú 0123456789%$#*@#!?:]+$");
        return text.matcher(string).matches();
    }

    public static void setEmptyMessage(TextInputLayout textInputLayout, String errorMessage){
        textInputLayout.setError(errorMessage + " é obrigatório");
    }

    public static void setTextMessage(TextInputLayout textInputLayout, String errorMessage){
        textInputLayout.setError(errorMessage + " deve conter apenas letras");
    }

    public static String getTimeNow(){
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        return sdfTime.format(new Date(System.currentTimeMillis()));
    }

    public static String getTodayDate(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        return sdfDate.format(d);
    }

    public static String getFirstDayOfWeek(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        for (int i = 0; i < 7; i++) {
            if(i == 0) {
                return sdfDate.format(c.getTime());
            }
            c.add(Calendar.DAY_OF_WEEK, 1);
        }
        return "";
    }

    public static String getLastDayOfWeek(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        for (int i = 0; i < 7; i++) {
            if(i == 6) {
                return sdfDate.format(c.getTime());
            }
            c.add(Calendar.DAY_OF_WEEK, 1);
        }
        return "";
    }

    public static String getSevenDaysBefore(String dateIni){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dIni = null;
        Date dFim;
        try{
            dIni = sdf.parse(dateIni);
        }catch (Exception e){

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dIni);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        dFim = calendar.getTime();

        return sdf.format(dFim);

    }

    public static String concatDate(int year, String month, String day){
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append("-");
        sb.append(month);
        sb.append("-");
        sb.append(day);
        return sb.toString();
    }

    public static String formatDate(String d){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try{
            date = formatter.parse(d);
        }catch (Exception e){

        }
        return sdf.format(date);
    }
}
