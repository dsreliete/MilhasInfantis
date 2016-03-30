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

    public boolean insertCategory(Categories categories){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.CategoryTable.CATEGORY_DESC, categories.getDescription());
        db.insert(DBContract.CategoryTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Categories> fetchCategoriesList(){
        List<Categories> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.CategoryTable.NAME_TABLE, DBContract.CategoryTable.CATEGORY_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.CategoryTable.CATEGORY_ID));
            String desc = c.getString(1);
            Categories cat = new Categories(id, desc);
            lista.add(cat);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean updateCategory(int id, String desc){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.CategoryTable.CATEGORY_DESC, desc);
        return db.update(DBContract.CategoryTable.NAME_TABLE, values, DBContract.CategoryTable.CATEGORY_ID + "=" + id, null) > 0;

    }


    public Categories fetchCategoryPerId(int id) throws SQLException {

        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.CategoryTable.NAME_TABLE, DBContract.CategoryTable.CATEGORY_COLS, DBContract.CategoryTable.CATEGORY_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;
        Categories cat;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.CategoryTable.CATEGORY_ID));
            String desc = c.getString(1);
            cat = new Categories(idd, desc);
        }while (c.moveToNext());
        c.close();
        db.close();
        return cat;
    }

    public boolean deleteCategoryId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.CategoryTable.NAME_TABLE, DBContract.CategoryTable.CATEGORY_ID + "=" + rowId, null) > 0;
    }

    public boolean obtainCategory(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.CategoryTable.NAME_TABLE, DBContract.CategoryTable.CATEGORY_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
