package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Realizates;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;

/**
 * Created by eliete on 10/26/15.
 */
public class RealizationDAO {

    private Context context;

    public RealizationDAO(Context context){
        this.context = context;
    }

    public boolean insertRealization(int childId, int actionId, int actionCatId, String actionType, int point, String pointType, String date, String hour){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.RealizationTable.REALIZATION_CHILD_ID, childId); // id do filho
        values.put(DBContract.RealizationTable.REALIZATION_ACTION_ID, actionId); // id de: atividade, categoria, penalizacao, associacao
        values.put(DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID, actionCatId);//atributo nulo, somente para atividade
        values.put(DBContract.RealizationTable.REALIZATION_ACTION_TYPE, actionType); // cadastrar. editar, excluir, penalizar, associar. Qq ação do app
        values.put(DBContract.RealizationTable.REALIZATION_POINT, point);// atributo nulo. Só para bonificar, penalizar ou premiar, correponde ao vcalor do ponto
        values.put(DBContract.RealizationTable.REALIZATION_POINT_TYPE, pointType); // atributo nulo. Só para bonificar, penalizar ou premiar. O Tipo do ponto pode ser: verde, verm, amarelo ou azul
        values.put(DBContract.RealizationTable.REALIZATION_DATE, date);
        values.put(DBContract.RealizationTable.REALIZATION_HOUR, hour);
        db.insert(DBContract.RealizationTable.NAME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Realizates> fetchRealizationPerChild(int idChild){
        List<Realizates> list = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBContract.RealizationTable.NAME_TABLE + ", " +
                DBContract.ChildTable.NAME_TABLE + " WHERE " + DBContract.ChildTable.CHILD_ID +
                " = " + DBContract.RealizationTable.REALIZATION_CHILD_ID + " AND " +
                DBContract.ChildTable.CHILD_ID + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return list;

        Realizates realizates;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ID));
            String childName = c.getString(c.getColumnIndexOrThrow(DBContract.ChildTable.CHILD_NAME));
            int point = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_POINT));
            String pointType = c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_POINT_TYPE));
            int actionId = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ACTION_ID));
            int catActionId = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID));
            String actionType = c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ACTION_TYPE));
            String date = c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_DATE));
            String birthDate = Utils.formatDate(date);
            String hour =  c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_HOUR));
            realizates = new Realizates(idd, childName, point, pointType, actionId, catActionId, actionType, birthDate, hour);
            list.add(realizates);
        }while (c.moveToNext());
        c.close();
        db.close();
        return list;
    }

    public List<Realizates> fetchRealization(){
        List<Realizates> list = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBContract.RealizationTable.NAME_TABLE + ", " +
                DBContract.ChildTable.NAME_TABLE + " WHERE " + DBContract.ChildTable.CHILD_ID +
                " = " + DBContract.RealizationTable.REALIZATION_CHILD_ID;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return list;

        Realizates realizates;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ID));
            int idFilho = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_CHILD_ID));
            String childName = c.getString(c.getColumnIndexOrThrow(DBContract.ChildTable.CHILD_NAME));
            int point = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_POINT));
            String pointType = c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_POINT_TYPE));
            int actionId = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ACTION_ID));
            int catActionId = c.getInt(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID));
            String actionType = c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_ACTION_TYPE));
            String date = c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_DATE));
            String birthDate = Utils.formatDate(date);
            String hour =  c.getString(c.getColumnIndexOrThrow(DBContract.RealizationTable.REALIZATION_HOUR));
            realizates = new Realizates(idd, idFilho, childName, point, pointType, actionId, catActionId, actionType, birthDate, hour);
            list.add(realizates);
        }while (c.moveToNext());
        c.close();
        db.close();
        return list;
    }

    public int fetchTotalPointsPerChild(int idChild){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT SUM (" + DBContract.RealizationTable.REALIZATION_POINT + ") FROM " + DBContract.RealizationTable.NAME_TABLE
                + " WHERE " + DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchTotalPointsPerChildToday(int idChild, String date){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT SUM (" + DBContract.RealizationTable.REALIZATION_POINT + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " + DBContract.RealizationTable.REALIZATION_CHILD_ID
                + " = " + idChild + " AND strftime('%Y-%m-%d', " + DBContract.RealizationTable.REALIZATION_DATE +
                ")=strftime('%Y-%m-%d','" + date + "')" ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchTotalPointsPerChildWeek(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT SUM (" + DBContract.RealizationTable.REALIZATION_POINT + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE strftime('%Y-%m-%d', "
                + DBContract.RealizationTable.REALIZATION_DATE + ")>=strftime('%Y-%m-%d','" +
                iniDate + "') AND strftime('%Y-%m-%d', " + DBContract.RealizationTable.REALIZATION_DATE +
                ")<=strftime('%Y-%m-%d','" + endDate + "') AND " + DBContract.RealizationTable.REALIZATION_CHILD_ID
                + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchTotalRedActions(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'vermelho' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild
                  + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'"
                ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchTotalYellowActions(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'amarelo' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'"
                ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchTotalGreenActions(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'verde' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }


    public int fetchBonificationRedActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'vermelho' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);

    }

    public int fetchBonificationYellowActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'amarelo' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar'"
                + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'"
                ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchBonificationGreenActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'verde' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar'"
                 + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchExtraPointsRedActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'vermelho' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'ponto_extra'"
                + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarExtraPointsGreenActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_POINT_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE "
                + DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'verde' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'ponto_extra'" +
                " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchPenActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'penalizar' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild +
                " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int fetchBonActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild +
                " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int CatRedActions(int idChild, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'vermelho' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID + " = " + catId +
                " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int CatYellowActions(int idChild, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'amarelo' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID + " = " + catId + " AND "
                + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int CatGreenActions(int idChild, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'verde' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID + " = " + catId
                + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int GoalRedActions(int idChild, int idGoal, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'vermelho' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID + " = " + catId + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_ID + " = " + idGoal
                + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int GoalYellowActions(int idChild, int idGoal, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'amarelo' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID + " = " + catId + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_ID + " = " + idGoal +
                " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";
        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int GoalGreenActions(int idChild, int idGoal, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.RealizationTable.REALIZATION_ACTION_TYPE + ") FROM " +
                DBContract.RealizationTable.NAME_TABLE + " WHERE " +
                DBContract.RealizationTable.REALIZATION_ACTION_TYPE + " = 'bonificar' AND " +
                DBContract.RealizationTable.REALIZATION_POINT_TYPE + " = 'verde' AND " +
                DBContract.RealizationTable.REALIZATION_CHILD_ID + " = " + idChild + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_CAT_ID + " = " + catId + " AND " +
                DBContract.RealizationTable.REALIZATION_ACTION_ID + " = " + idGoal
                + " AND " + DBContract.RealizationTable.REALIZATION_DATE +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }
}
