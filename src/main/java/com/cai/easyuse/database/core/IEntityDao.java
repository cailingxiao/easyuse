package com.cai.easyuse.database.core;

import android.database.sqlite.SQLiteDatabase;

/**
 * 管理表的创建和删除的接口,一般使用普通的类继承继承，
 * 可直接使用一个类管理所有的数据库表的创建、删除语句
 * <p/>
 * Created by cailingxiao on 2017/1/26.
 */
public interface IEntityDao {

    void createTable(SQLiteDatabase db, boolean ifNotExists);

    void dropAllTables(SQLiteDatabase db, boolean ifExists);
}
