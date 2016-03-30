package br.com.rodrigues.eliete.milhasinfantis.Utils;

import android.provider.BaseColumns;

/**
 * Created by eliete on 8/16/15.
 */
public class DBContract {

    public static class ParentsTable implements BaseColumns{
        public static final String NAME_TABLE = "pai";
        public static final String PARENT_ID = "_id";
        public static final String PARENT_NAME = "nome";
        public static final String PARENT_FAMILY = "familia";
        public static final String PARENT_COLS[] = {PARENT_ID, PARENT_NAME, PARENT_FAMILY};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" + PARENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PARENT_NAME + " VARCHAR(30) NOT NULL, "+ PARENT_FAMILY +
                " VARCHAR(30) NOT NULL)";

    }

    public static class ChildTable implements BaseColumns{
        public static final String NAME_TABLE = "filho";
        public static final String CHILD_ID = "_id";
        public static final String CHILD_NAME = "nome";
        public static final String CHILD_BIRTH = "dtNasc";
        public static final String CHILD_GENDER = "sexo";
        public static final String CHILD_COLS[] = {CHILD_ID, CHILD_NAME, CHILD_BIRTH, CHILD_GENDER};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" +
                CHILD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHILD_NAME + " VARCHAR(30) NOT NULL, "+ CHILD_BIRTH +
                " DATE NOT NULL," + CHILD_GENDER + " CHARACTER(1))";
    }

    public static class AwardsTable implements BaseColumns{
        public static final String NAME_TABLE = "premio";
        public static final String AWARD_ID = "_id";
        public static final String AWARD_DESC = "desc";
        public static final String AWARD_POINT = "ponto";
        public static final String AWARD_COLS[] = {AWARD_ID, AWARD_DESC, AWARD_POINT};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" +
                AWARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AWARD_DESC + " VARCHAR(30) NOT NULL, "+ AWARD_POINT + " INTEGER NOT NULL)";
    }

    public static class CategoryTable implements BaseColumns{
        public static final String NAME_TABLE = "categoria";
        public static final String CATEGORY_ID = "_id";
        public static final String CATEGORY_DESC = "desc";
        public static final String CATEGORY_COLS[] = {CATEGORY_ID, CATEGORY_DESC};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_DESC + " VARCHAR(30) NOT NULL)";
    }

    public static class ActivityTable implements BaseColumns{
        public static final String NAME_TABLE = "atividade";
        public static final String ACTIVITY_ID = "_id";
        public static final String ACTIVITY_DESC = "desc";
        public static final String ACTIVITY_PVERM = "ponto_vermelho";
        public static final String ACTIVITY_PVERD = "ponto_verde";
        public static final String ACTIVITY_PAMA = "ponto_amarelo";
        public static final String ACTIVITY_CAT_ID = "atividade_categoria_id";
        public static final String ACTIVITY_COLS[] = {ACTIVITY_ID, ACTIVITY_DESC,
                ACTIVITY_PVERM, ACTIVITY_PAMA, ACTIVITY_PVERD, ACTIVITY_CAT_ID};
        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" +
                ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ACTIVITY_DESC + " VARCHAR(30) NOT NULL, " +
                ACTIVITY_PVERM + " INTEGER NOT NULL, " +
                ACTIVITY_PAMA + " INTEGER NOT NULL, " +
                ACTIVITY_PVERD + " INTEGER NOT NULL, " +
                ACTIVITY_CAT_ID + " INTEGER, FOREIGN KEY(" + ACTIVITY_CAT_ID + ") REFERENCES " +
                CategoryTable.NAME_TABLE + "(" + CategoryTable.CATEGORY_ID + "))";
    }

    public static class PenaltyTable implements BaseColumns{
        public static final String NOME_TABLE = "penalidade";
        public static final String PENALTY_ID = "_id";
        public static final String PENALTY_DESC = "desc";
        public static final String PENALTY_POINT = "ponto";
        public static final String PENALTY_COLS[] = {PENALTY_ID, PENALTY_DESC,
                PENALTY_POINT};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                PENALTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PENALTY_DESC + " VARCHAR(30) NOT NULL, " + PENALTY_POINT +
                " INTEGER NOT NULL)";

    }

    public static class AssociationTable implements BaseColumns{
        public static final String NAME_TABLE = "associacao";
        public static final String ASSOCIATION_CHILD_ID = "assoc_filho_id";
        public static final String ASSOCIATION_ACTIVITY_ID = "assoc_ativ_id";
        public static final String ASSOCIATION_ACTIVITY_STATUS = "status_associacao";
        public static final String ASSOCIATION_CAT_ID = "assoc_cat_id";
        public static final String ASSOCIATION_COLS[] = {ASSOCIATION_CHILD_ID,
                ASSOCIATION_ACTIVITY_ID, ASSOCIATION_ACTIVITY_STATUS, ASSOCIATION_CAT_ID};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" +
                ASSOCIATION_CHILD_ID + " INTEGER NOT NULL, " +
                ASSOCIATION_ACTIVITY_ID + " INTEGER NOT NULL, " +
                ASSOCIATION_ACTIVITY_STATUS + " INTEGER NOT NULL, " +
                ASSOCIATION_CAT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ASSOCIATION_CHILD_ID + ") REFERENCES " +
                ChildTable.NAME_TABLE + "(" + ChildTable.CHILD_ID + "), " +
                "FOREIGN KEY(" + ASSOCIATION_ACTIVITY_ID + ") REFERENCES " +
                ActivityTable.NAME_TABLE + "(" + ActivityTable.ACTIVITY_ID + "),"  +
                "FOREIGN KEY(" + ASSOCIATION_CAT_ID + ") REFERENCES " +
                CategoryTable.NAME_TABLE + "(" + CategoryTable.CATEGORY_ID + ")," +
                "PRIMARY KEY(" + ASSOCIATION_CHILD_ID + ", " + ASSOCIATION_ACTIVITY_ID + "))";


    }

    public static class RealizationTable implements BaseColumns{
        public static final String NAME_TABLE = "realizacao";
        public static final String REALIZATION_ID = "realizacao_id";
        public static final String REALIZATION_CHILD_ID = "realizacao_filho_id"; //id filho
        public static final String REALIZATION_ACTION_ID = "realizacao_acao_id"; //id de atividade, penalidade, premio, categoria, filho, bonificaça etc
        public static final String REALIZATION_ACTION_CAT_ID = "realizacao_acao_cat_id"; //atributo nulo, somente para atividade
        public static final String REALIZATION_ACTION_TYPE = "realizacao_acao_tipo"; // cadastrar. editar, excluir, penalizar, associar. Qq ação do app
        public static final String REALIZATION_POINT = "realizacao_ponto"; // valor do ponto
        public static final String REALIZATION_POINT_TYPE = "realizacao_ponto_tipo"; // atributo nulo. Só para bonificar, penalizar ou premiar. O Tipo do ponto pode ser: verde, verm, amarelo ou azul
        public static final String REALIZATION_DATE = "realizacao_data";
        public static final String REALIZATION_HOUR = "realizacao_hora";
        public static final String REALIZATION_COLS[] = {REALIZATION_ID, REALIZATION_CHILD_ID,
                REALIZATION_ACTION_ID, REALIZATION_ACTION_CAT_ID, REALIZATION_ACTION_TYPE, REALIZATION_POINT, REALIZATION_POINT_TYPE,
                REALIZATION_DATE, REALIZATION_HOUR};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" +
                REALIZATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REALIZATION_ACTION_ID +
                " INTEGER NOT NULL, " + REALIZATION_ACTION_CAT_ID + " INTEGER, " + REALIZATION_ACTION_TYPE + " VARCHAR(15) NOT NULL, " +
                REALIZATION_CHILD_ID + " INTEGER NOT NULL, " + REALIZATION_POINT + " REAL, "
                + REALIZATION_POINT_TYPE + " VARCHAR(15), " + REALIZATION_DATE + " TEXT NOT NULL, " +
                REALIZATION_HOUR + " VARCHAR(4))";

    }

}
