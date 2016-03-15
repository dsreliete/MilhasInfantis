package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 8/16/15.
 */
public class CategoriesDAO {

    private Context context;

    public CategoriesDAO(Context context){
        this.context = context;
    }

    public boolean inserir(Categories categories){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaCategoria.CATEGORIA_DESC, categories.getDescription());
        db.insert(DBContract.TabelaCategoria.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Categories> consultarCategoriesList(){
        List<Categories> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaCategoria.NOME_TABLE, DBContract.TabelaCategoria.CATEGORIA_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaCategoria.CATEGORIA_ID));
            String desc = c.getString(1);
            Categories cat = new Categories(id, desc);
            lista.add(cat);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean atualizar(int id, String desc){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaCategoria.CATEGORIA_DESC, desc);
        return db.update(DBContract.TabelaCategoria.NOME_TABLE, values, DBContract.TabelaCategoria.CATEGORIA_ID + "=" + id, null) > 0;

    }


    public Categories consultarPorId(int id) throws SQLException {

        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.TabelaCategoria.NOME_TABLE, DBContract.TabelaCategoria.CATEGORIA_COLS, DBContract.TabelaCategoria.CATEGORIA_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;
        Categories cat;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaCategoria.CATEGORIA_ID));
            String desc = c.getString(1);
            cat = new Categories(idd, desc);
        }while (c.moveToNext());
        c.close();
        db.close();
        return cat;
    }

    public boolean deletarId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.TabelaCategoria.NOME_TABLE, DBContract.TabelaCategoria.CATEGORIA_ID + "=" + rowId, null) > 0;
    }

    public boolean obterCadastroCategoria(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaCategoria.NOME_TABLE, DBContract.TabelaCategoria.CATEGORIA_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
