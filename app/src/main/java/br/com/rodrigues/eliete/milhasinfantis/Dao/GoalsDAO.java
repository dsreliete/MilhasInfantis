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

    public boolean inserir(Goals goals){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaAtividade.ATIVIDADE_DESC, goals.getDescription());
        values.put(DBContract.TabelaAtividade.ATIVIDADE_PVERM, goals.getRedPoint());
        values.put(DBContract.TabelaAtividade.ATIVIDADE_PAMA, goals.getYellowPoint());
        values.put(DBContract.TabelaAtividade.ATIVIDADE_PVERD, goals.getGreenPoint());
        values.put(DBContract.TabelaAtividade.ATIVIDADE_CAT_ID, goals.getCatId());
        db.insert(DBContract.TabelaAtividade.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Goals> consultarGoalsList(){
        List<Goals> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaAtividade.NOME_TABLE, DBContract.TabelaAtividade.ATIVIDADE_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Goals goals;
        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERD));
            int catId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_CAT_ID));
            goals = new Goals(id, desc, red, yellow, green, catId);
            lista.add(goals);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public List<Goals> consultarGoalsListPerCategory(int id){
        List<Goals> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaAtividade.NOME_TABLE, DBContract.TabelaAtividade.ATIVIDADE_COLS, DBContract.TabelaAtividade.ATIVIDADE_CAT_ID + "=" + id, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Goals goals;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERD));
            goals = new Goals(idd, desc, red, yellow, green, id);
            lista.add(goals);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean atualizar(int id, String description, int red, int yellow, int green, int catId){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaAtividade.ATIVIDADE_DESC, description);
        values.put(DBContract.TabelaAtividade.ATIVIDADE_PVERM, red);
        values.put(DBContract.TabelaAtividade.ATIVIDADE_PAMA, yellow);
        values.put(DBContract.TabelaAtividade.ATIVIDADE_PVERD, green);
        values.put(DBContract.TabelaAtividade.ATIVIDADE_CAT_ID, catId);
        return db.update(DBContract.TabelaAtividade.NOME_TABLE, values, DBContract.TabelaAtividade.ATIVIDADE_ID + "=" + id, null) > 0;

    }


    public Goals consultarGoalPorId(int idActivity) throws SQLException {

        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.TabelaAtividade.NOME_TABLE, DBContract.TabelaAtividade.ATIVIDADE_COLS, DBContract.TabelaAtividade.ATIVIDADE_ID + "=" + idActivity, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;

        Goals goals;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERD));
            int catId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_CAT_ID));
            goals = new Goals(idd, desc, red, yellow, green, catId);
        }while (c.moveToNext());
        c.close();
        db.close();
        return goals;
    }

    public boolean deletarId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.TabelaAtividade.NOME_TABLE, DBContract.TabelaAtividade.ATIVIDADE_ID + "=" + rowId, null) > 0;
    }

    public boolean obterCadastroAtividade(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaAtividade.NOME_TABLE, DBContract.TabelaAtividade.ATIVIDADE_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean getBooleanValue(int value){
        boolean result = value == 1 ? true : false;
        return result;
    }

}
