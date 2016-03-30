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

    public boolean insertChild(Children children){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.ChildTable.CHILD_NAME, children.getName());
        values.put(DBContract.ChildTable.CHILD_BIRTH, children.getBirthDate());
        values.put(DBContract.ChildTable.CHILD_GENDER, children.getGender());
        db.insert(DBContract.ChildTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Children> fetchChildrenList(){
        List<Children> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.ChildTable.NAME_TABLE, DBContract.ChildTable.CHILD_COLS, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        do{
            int id = c.getInt(c.getColumnIndexOrThrow(DBContract.ChildTable.CHILD_ID));
            String name = c.getString(1);
            String date = c.getString(2);
            String birthDate = Utils.formatDate(date);
            String gender = c.getString(3);
            Children child = new Children(id, name, birthDate, gender);
            lista.add(child);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean updateChildren(int id, String name, String birthDate, String gender){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.ChildTable.CHILD_NAME, name);
        values.put(DBContract.ChildTable.CHILD_BIRTH, birthDate);
        values.put(DBContract.ChildTable.CHILD_GENDER, gender);
        return db.update(DBContract.ChildTable.NAME_TABLE, values, DBContract.ChildTable.CHILD_ID + "=" + id, null) > 0;

    }


    public Children fetchChildrenPerId(int id) throws SQLException {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DBContract.ChildTable.NAME_TABLE, DBContract.ChildTable.CHILD_COLS, DBContract.ChildTable.CHILD_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() == 0)
            return null;
        Children child;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.ChildTable.CHILD_ID));
            String name = c.getString(1);
            String date = c.getString(2);
            String gender = c.getString(3);
            child = new Children(idd, name, date, gender);
        }while (c.moveToNext());
        c.close();
        db.close();
        return child;
    }

    public boolean deleteChildrenId(long rowId) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.ChildTable.NAME_TABLE, DBContract.ChildTable.CHILD_ID + "=" + rowId, null) > 0;
    }

    public boolean obtainChildren(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.ChildTable.NAME_TABLE, DBContract.ChildTable.CHILD_COLS, null, null, null, null, null);

        if (c != null)
            c.moveToFirst();

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

}
