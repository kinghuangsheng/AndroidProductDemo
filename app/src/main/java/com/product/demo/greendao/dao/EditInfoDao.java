package com.product.demo.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.product.demo.greendao.bean.EditInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EDIT_INFO".
*/
public class EditInfoDao extends AbstractDao<EditInfo, Long> {

    public static final String TABLENAME = "EDIT_INFO";

    /**
     * Properties of entity EditInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property AlarmSituationId = new Property(2, String.class, "alarmSituationId", false, "ALARM_SITUATION_ID");
        public final static Property HandleInfo = new Property(3, String.class, "handleInfo", false, "HANDLE_INFO");
        public final static Property SurveyInfo = new Property(4, String.class, "surveyInfo", false, "SURVEY_INFO");
    };


    public EditInfoDao(DaoConfig config) {
        super(config);
    }
    
    public EditInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EDIT_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"ALARM_SITUATION_ID\" TEXT," + // 2: alarmSituationId
                "\"HANDLE_INFO\" TEXT," + // 3: handleInfo
                "\"SURVEY_INFO\" TEXT);"); // 4: surveyInfo
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EDIT_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EditInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String alarmSituationId = entity.getAlarmSituationId();
        if (alarmSituationId != null) {
            stmt.bindString(3, alarmSituationId);
        }
 
        String handleInfo = entity.getHandleInfo();
        if (handleInfo != null) {
            stmt.bindString(4, handleInfo);
        }
 
        String surveyInfo = entity.getSurveyInfo();
        if (surveyInfo != null) {
            stmt.bindString(5, surveyInfo);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public EditInfo readEntity(Cursor cursor, int offset) {
        EditInfo entity = new EditInfo( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // alarmSituationId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // handleInfo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // surveyInfo
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EditInfo entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAlarmSituationId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHandleInfo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSurveyInfo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(EditInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(EditInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
