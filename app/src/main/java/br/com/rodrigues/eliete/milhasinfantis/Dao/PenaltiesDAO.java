package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 8/16/15.
 */
public class PenaltiesDAO {

    private Context context;

    public PenaltiesDAO(Context context){
        this.context = context;
    }

    public boolean insertPenalty(Penalties penalties){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.PenaltyTable.PENALTY_DESC, penalties.getDescription());
        values.put(DBContract.PenaltyTable.PENALTY_POINT, penalties.getPoint());
        db.insert(DBContract.PenaltyTable.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Penalties> fetchPenaltiesList(){
        List<Penalties> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.PenaltyTable.NOME_TABLE, DBContract.PenaltyTable.PENALTY_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.PenaltyTable.PENALTY_ID));
            String desc = c.getString(1);
            int point = c.getInt(2);
            Penalties pen = new Penalties(id, desc, point);
            lista.add(pen);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean updatePenalty(int id, String desc, int point){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.PenaltyTable.PENALTY_DESC, desc);
        values.put(DBContract.PenaltyTable.PENALTY_POINT, point);

        return db.update(DBContract.PenaltyTable.NOME_TABLE, values, DBContract.PenaltyTable.PENALTY_ID + "=" + id, null) > 0;

    }


    public Penalties fetchPenaltyPerId(int id) throws SQLException {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.PenaltyTable.NOME_TABLE, DBContract.PenaltyTable.PENALTY_COLS, DBContract.PenaltyTable.PENALTY_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;
        Penalties pen;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.PenaltyTable.PENALTY_ID));
            String desc = c.getString(1);
            int point = c.getInt(2);
            pen = new Penalties(idd, desc, point);
        }while (c.moveToNext());
        c.close();
        db.close();
        return pen;
    }

    public boolean deletePenaltyId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.PenaltyTable.NOME_TABLE, DBContract.PenaltyTable.PENALTY_ID + "=" + rowId, null) > 0;
    }

    public boolean obtainPenalty(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.PenaltyTable.NOME_TABLE, DBContract.PenaltyTable.PENALTY_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
