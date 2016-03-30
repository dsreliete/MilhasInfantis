package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public boolean insertParents(Parents parents){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.ParentsTable.PARENT_NAME, parents.getName());
        values.put(DBContract.ParentsTable.PARENT_FAMILY, parents.getNameFamily());
        db.insert(DBContract.ParentsTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }

    public Parents fetchParents(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.ParentsTable.NAME_TABLE, DBContract.ParentsTable.PARENT_COLS, null, null, null, null, null);
        Parents p;
        if(c != null)
            c.moveToFirst();
        if (c.getCount() == 0)
            return null;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.ParentsTable.PARENT_ID));
            String name = c.getString(1);
            String family = c.getString(2);
            p = new Parents(id, name, family);
        }while (c.moveToNext());
        c.close();
        db.close();
        return p;
    }

    public boolean updateParent(int id, String name, String family){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.ParentsTable.PARENT_NAME, name);
        values.put(DBContract.ParentsTable.PARENT_FAMILY, family);

        return db.update(DBContract.ParentsTable.NAME_TABLE, values, DBContract.ParentsTable.PARENT_ID + "=" + id, null) > 0;

    }

    public boolean obtainParent(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.ParentsTable.NAME_TABLE, DBContract.ParentsTable.PARENT_COLS, null, null, null, null, null);

        if (c != null)
            c.moveToFirst();

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }





}
