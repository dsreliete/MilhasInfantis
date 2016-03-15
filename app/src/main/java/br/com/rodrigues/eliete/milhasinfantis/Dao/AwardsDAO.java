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

    public boolean inserir(Awards awards){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaPremio.PREMIO_DESC, awards.getDescription());
        values.put(DBContract.TabelaPremio.PREMIO_PONTO, awards.getPoints());
        db.insert(DBContract.TabelaPremio.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Awards> consultarAwardsList(){
        List<Awards> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaPremio.NOME_TABLE, DBContract.TabelaPremio.PREMIO_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaPremio.PREMIO_ID));
            String desc = c.getString(1);
            String ponto = c.getString(2);
            Awards awds = new Awards(id, desc, Integer.parseInt(ponto));
            lista.add(awds);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean atualizar(int id, String desc, Integer points){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaPremio.PREMIO_ID, id);
        values.put(DBContract.TabelaPremio.PREMIO_DESC, desc);
        values.put(DBContract.TabelaPremio.PREMIO_PONTO, points);
        return db.update(DBContract.TabelaPremio.NOME_TABLE, values, DBContract.TabelaPremio.PREMIO_ID + "=" + id, null) > 0;

    }


    public Awards consultarPorId(Integer id) throws SQLException {

        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.TabelaPremio.NOME_TABLE, DBContract.TabelaPremio.PREMIO_COLS, DBContract.TabelaPremio.PREMIO_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;

        Awards awds;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaPremio.PREMIO_ID));
            String desc = c.getString(1);
            int ponto = c.getInt(2);
            awds = new Awards(idd, desc, ponto);
        }while (c.moveToNext());
        c.close();
        db.close();
        return awds;
    }

    public boolean deletarId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.TabelaPremio.NOME_TABLE, DBContract.TabelaPremio.PREMIO_ID + "=" + rowId, null) > 0;
    }

    public boolean obterCadastroPremio(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaPremio.NOME_TABLE, DBContract.TabelaPremio.PREMIO_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
