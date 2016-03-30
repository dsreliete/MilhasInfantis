package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 10/3/15.
 */
public class GoalsAssociationDAO {

    private Context context;

    public GoalsAssociationDAO(Context context){
        this.context = context;
    }

    public boolean insertAssociation(Associates a){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.AssociationTable.ASSOCIATION_CHILD_ID, a.getIdChildren());
        values.put(DBContract.AssociationTable.ASSOCIATION_ACTIVITY_ID, a.getIdActivity());
        values.put(DBContract.AssociationTable.ASSOCIATION_CAT_ID, a.getCatId());
        values.put(DBContract.AssociationTable.ASSOCIATION_ACTIVITY_STATUS, a.getStatus());
        db.insert(DBContract.AssociationTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }


    public List<Goals> fetchAssociatedGoalsListPerCategoryPerChild(int idCategory, int idChild){
        List<Goals> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBContract.ActivityTable.NAME_TABLE + ", " +
                DBContract.AssociationTable.NAME_TABLE + " WHERE " + DBContract.ActivityTable.ACTIVITY_ID +
                " = " + DBContract.AssociationTable.ASSOCIATION_ACTIVITY_ID + " AND " +
                DBContract.AssociationTable.ASSOCIATION_CAT_ID + " = " + idCategory + " AND " +
                DBContract.AssociationTable.ASSOCIATION_CHILD_ID + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Goals goals;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_PVERD));
            int catId = c.getInt(c.getColumnIndexOrThrow(DBContract.ActivityTable.ACTIVITY_CAT_ID));
            goals = new Goals(idd, desc, red, yellow, green, catId);
            lista.add(goals);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public List<Associates> fetchAssociatesListPerCategoryPerChild(int idCategory, int idChildren){
        List<Associates> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.AssociationTable.NAME_TABLE, DBContract.AssociationTable.ASSOCIATION_COLS,
                DBContract.AssociationTable.ASSOCIATION_CAT_ID + "=" + idCategory + " AND " +
                        DBContract.AssociationTable.ASSOCIATION_CHILD_ID + " = " + idChildren, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Associates a;
        do{
            int idChild = c.getInt(c.getColumnIndexOrThrow(DBContract.AssociationTable.ASSOCIATION_CHILD_ID));
            int idActivity = c.getInt(c.getColumnIndexOrThrow(DBContract.AssociationTable.ASSOCIATION_ACTIVITY_ID));
            int status = c.getInt(c.getColumnIndexOrThrow(DBContract.AssociationTable.ASSOCIATION_ACTIVITY_STATUS));
            a = new Associates(idChild, idActivity, idCategory, status);
            lista.add(a);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public List<Associates> fetchAssociatesListPerCategory(int idCategory){
        List<Associates> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.AssociationTable.NAME_TABLE, DBContract.AssociationTable.ASSOCIATION_COLS,
                DBContract.AssociationTable.ASSOCIATION_CAT_ID + "=" + idCategory, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Associates a;
        do{
            int idChild = c.getInt(c.getColumnIndexOrThrow(DBContract.AssociationTable.ASSOCIATION_CHILD_ID));
            int idActivity = c.getInt(c.getColumnIndexOrThrow(DBContract.AssociationTable.ASSOCIATION_ACTIVITY_ID));
            int status = c.getInt(c.getColumnIndexOrThrow(DBContract.AssociationTable.ASSOCIATION_ACTIVITY_STATUS));
            a = new Associates(idChild, idActivity, idCategory, status);
            lista.add(a);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean obtainAssociation(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.AssociationTable.NAME_TABLE, DBContract.AssociationTable.ASSOCIATION_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

     public boolean deleteAssociationId(Associates a) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.AssociationTable.NAME_TABLE,
                DBContract.AssociationTable.ASSOCIATION_CHILD_ID + "=" + a.getIdChildren() + " AND " +
                        DBContract.AssociationTable.ASSOCIATION_ACTIVITY_ID + "=" + a.getIdActivity(), null) > 0;
    }
}
