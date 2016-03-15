package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;

/**
 * Created by eliete on 8/16/15.
 */
public class ChildrenDAO {

    private Context context;

    public ChildrenDAO(Context context){
        this.context = context;
    }

    public boolean inserir(Children children){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaFilho.FILHO_NOME, children.getName());
        values.put(DBContract.TabelaFilho.FILHO_DT_NASC, children.getDataNasc());
        values.put(DBContract.TabelaFilho.FILHO_SEXO, children.getGender());
        db.insert(DBContract.TabelaFilho.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Children> consultarChildrenList(){
        List<Children> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaFilho.NOME_TABLE, DBContract.TabelaFilho.FILHO_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaFilho.FILHO_ID));
            String nome = c.getString(1);
            String d = c.getString(2);
            String dataNasc = Utils.formatDate(d);
            String gender = c.getString(3);
            Children child = new Children(id, nome, dataNasc, gender);
            lista.add(child);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean atualizar(int id, String nome, String dataNasc, String gender){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaFilho.FILHO_NOME, nome);
        values.put(DBContract.TabelaFilho.FILHO_DT_NASC, dataNasc);
        values.put(DBContract.TabelaFilho.FILHO_SEXO, gender);
        return db.update(DBContract.TabelaFilho.NOME_TABLE, values, DBContract.TabelaFilho.FILHO_ID + "=" + id, null) > 0;

    }


    public Children consultarPorId(int id) throws SQLException {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.TabelaFilho.NOME_TABLE, DBContract.TabelaFilho.FILHO_COLS, DBContract.TabelaFilho.FILHO_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;
        Children child;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaFilho.FILHO_ID));
            String nome = c.getString(1);
            String dataNasc = c.getString(2);
            String gender = c.getString(3);
            child = new Children(idd, nome, dataNasc, gender);
        }while (c.moveToNext());
        c.close();
        db.close();
        return child;
    }

    public boolean deletarId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.TabelaFilho.NOME_TABLE, DBContract.TabelaFilho.FILHO_ID + "=" + rowId, null) > 0;
    }

    public boolean obterCadastroFilho(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaFilho.NOME_TABLE, DBContract.TabelaFilho.FILHO_COLS, null, null, null, null, null);

        if (c != null)
            c.moveToFirst();

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
