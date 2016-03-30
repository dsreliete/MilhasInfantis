package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 8/16/15.
 */
public class AwardsDAO {

    private Context context;

    public AwardsDAO(Context context){
        this.context = context;
    }

    public boolean insertAwards(Awards awards){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.AwardsTable.AWARD_DESC, awards.getDescription());
        values.put(DBContract.AwardsTable.AWARD_POINT, awards.getPoints());
        db.insert(DBContract.AwardsTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Awards> fetchAwardsList(){
        List<Awards> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.AwardsTable.NAME_TABLE, DBContract.AwardsTable.AWARD_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.AwardsTable.AWARD_ID));
            String desc = c.getString(1);
            String point = c.getString(2);
            Awards awds = new Awards(id, desc, Integer.parseInt(point));
            lista.add(awds);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean updateAward(int id, String desc, Integer points){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.AwardsTable.AWARD_ID, id);
        values.put(DBContract.AwardsTable.AWARD_DESC, desc);
        values.put(DBContract.AwardsTable.AWARD_POINT, points);
        return db.update(DBContract.AwardsTable.NAME_TABLE, values, DBContract.AwardsTable.AWARD_ID + "=" + id, null) > 0;

    }


    public Awards fetchAwardsPerId(Integer id) throws SQLException {

        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.AwardsTable.NAME_TABLE, DBContract.AwardsTable.AWARD_COLS, DBContract.AwardsTable.AWARD_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;

        Awards awds;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.AwardsTable.AWARD_ID));
            String desc = c.getString(1);
            int point = c.getInt(2);
            awds = new Awards(idd, desc, point);
        }while (c.moveToNext());
        c.close();
        db.close();
        return awds;
    }

    public boolean deleteAwardId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.AwardsTable.NAME_TABLE, DBContract.AwardsTable.AWARD_ID + "=" + rowId, null) > 0;
    }

    public boolean obtainAwards(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.AwardsTable.NAME_TABLE, DBContract.AwardsTable.AWARD_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
