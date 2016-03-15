package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Parents;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 8/16/15.
 */
public class ParentsDAO {


    private Context context;

    public ParentsDAO(Context context){
        this.context = context;
    }

    public boolean inserir(Parents parents){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaPai.PAI_NOME, parents.getName());
        values.put(DBContract.TabelaPai.PAI_FAMILIA, parents.getNomeFamilia());
        db.insert(DBContract.TabelaPai.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public Parents consultarParents(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaPai.NOME_TABLE, DBContract.TabelaPai.PAI_COLS, null, null, null, null, null);
        Parents p;
        if(c != null)
            c.moveToFirst();
        if (c.getCount() == 0)
            return null;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaPai.PAI_ID));
            String nome = c.getString(1);
            String familia = c.getString(2);
            p = new Parents(id, nome, familia);
        }while (c.moveToNext());
        c.close();
        db.close();
        return p;
    }

    public boolean atualizar(int id, String nome, String familia){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaPai.PAI_NOME, nome);
        values.put(DBContract.TabelaPai.PAI_FAMILIA, familia);

        return db.update(DBContract.TabelaPai.NOME_TABLE, values, DBContract.TabelaPai.PAI_ID + "=" + id, null) > 0;

    }

    //verifica se ja tem um pai cadastrado
    public boolean obterCadastroPai(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaPai.NOME_TABLE, DBContract.TabelaPai.PAI_COLS, null, null, null, null, null);

        if (c != null)
            c.moveToFirst();

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }





}
