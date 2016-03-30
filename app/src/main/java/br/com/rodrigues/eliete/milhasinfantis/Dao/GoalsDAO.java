package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 8/16/15.
 */
public class GoalsDAO {

    private Context context;

    public GoalsDAO(Context context){
        this.context = context;
    }

    public boolean insertGoals(Goals goals){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.ActivityTable.ACTIVITY_DESC, goals.getDescription());
        values.put(DBContract.ActivityTable.ACTIVITY_PVERM, goals.getRedPoint());
        values.put(DBContract.ActivityTable.ACTIVITY_PAMA, goals.getYellowPoint());
        values.put(DBContract.ActivityTable.ACTIVITY_PVERD, goals.getGreenPoint());
        values.put(DBContract.ActivityTable.ACTIVITY_CAT_ID, goals.getCatId());
        db.insert(DBContract.ActivityTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }


    public List<Goals> fetchGoalsListPerCategory(int id){
        List<Goals> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.ActivityTable.NAME_TABLE, DBContract.ActivityTable.ACTIVITY_COLS, DBContract.ActivityTable.ACTIVITY_CAT_ID + "=" + id, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Goals goals;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PVERD));
            goals = new Goals(idd, desc, red, yellow, green, id);
            lista.add(goals);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean updateGoal(int id, String description, int red, int yellow, int green, int catId){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.ActivityTable.ACTIVITY_DESC, description);
        values.put(DBContract.ActivityTable.ACTIVITY_PVERM, red);
        values.put(DBContract.ActivityTable.ACTIVITY_PAMA, yellow);
        values.put(DBContract.ActivityTable.ACTIVITY_PVERD, green);
        values.put(DBContract.ActivityTable.ACTIVITY_CAT_ID, catId);
        return db.update(DBContract.ActivityTable.NAME_TABLE, values, DBContract.ActivityTable.ACTIVITY_ID + "=" + id, null) > 0;

    }


    public Goals fetchGoalPerId(int idActivity) throws SQLException {

        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.ActivityTable.NAME_TABLE, DBContract.ActivityTable.ACTIVITY_COLS, DBContract.ActivityTable.ACTIVITY_ID + "=" + idActivity, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }

        Goals goals = null;
        if (c.getCount() == 0)
            return goals;

        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PVERD));
            int catId = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_CAT_ID));
            goals = new Goals(idd, desc, red, yellow, green, catId);
        }while (c.moveToNext());
        c.close();
        db.close();
        return goals;
    }

    public boolean deleteGoalId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.ActivityTable.NAME_TABLE, DBContract.ActivityTable.ACTIVITY_ID + "=" + rowId, null) > 0;
    }

    public boolean obtainGoal(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.ActivityTable.NAME_TABLE, DBContract.ActivityTable.ACTIVITY_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
