package com.cai.easyuse.database;

import java.util.HashSet;
import java.util.Set;

import com.cai.easyuse.database.core.IDaoConfig;
import com.cai.easyuse.database.core.IEntityDao;
import com.cai.easyuse.database.dao.DaoMaster;
import com.cai.easyuse.database.dao.DaoSession;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.greenrobot.dao.AbstractDao;

/**
 * 数据库api注册类
 * <p>
 * - 首先使用DBGenerator生成entityclass和daoclass等
 * - 让entityClass继承IDaoConfig，用一个普通类实现entityDao，管理所有的表创建和删除
 * - register所有daoclass
 * - 调用开始start
 * </p>
 * Created by cailingxiao on 2017/1/26.
 */
public final class DbApi implements IEntityDao {

    private static final String DEFAULT_DB_NAME = "mydb-tables";

    private static volatile DbApi sInstance = null;

    private Set<IDaoConfig> mDaoConf = new HashSet<IDaoConfig>();
    private Set<IEntityDao> mEntityDao = new HashSet<IEntityDao>();

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DbApi() {

    }

    public static DbApi getInstance() {
        if (null == sInstance) {
            synchronized (DbApi.class) {
                if (null == sInstance) {
                    sInstance = new DbApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 数据库初始化
     *
     * @param context
     */
    public void start(Context context) {
        start(context, DEFAULT_DB_NAME);
    }

    /**
     * 数据库初始化
     *
     * @param context
     */
    public void start(Context context, String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        for (IDaoConfig config : mDaoConf) {
            mDaoMaster.registerDaoClass(config.getClazz());
        }


        mDaoSession = mDaoMaster.newSession();

        for (IDaoConfig config : mDaoConf) {
            mDaoSession.registerDao(config);
        }
    }

    /**
     * 注册dao，需要在start前调用。新的table需要主动在这里注册
     *
     * @param daoConfig
     */
    public void register(IDaoConfig daoConfig, IEntityDao entityDao) {
        mDaoConf.add(daoConfig);
        if (null != entityDao && !mEntityDao.contains(entityDao)) {
            mEntityDao.add(entityDao);
        }
    }

    /**
     * 注册dao，需要在start前调用。新的table需要在这里主动的注册。表的创建动作如果在前一个里创建了，
     * 就可以直接调用这个函数
     *
     * @param daoConfig
     */
    public void register(IDaoConfig daoConfig) {
        register(daoConfig, null);
    }


    /**
     * 获取对应的dao
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T extends AbstractDao> T getDao(Class<? extends Object> entityClass) {
        return (T) mDaoSession.getTableEntityDao(entityClass);
    }

    /**
     * 创建所有table
     *
     * @param db
     * @param ifNotExists
     */
    @Override
    public void createTable(SQLiteDatabase db, boolean ifNotExists) {
        for (IEntityDao entityDao : mEntityDao) {
            entityDao.createTable(db, ifNotExists);
        }
    }

    /**
     * 删除所有的table
     *
     * @param db
     * @param ifExists
     */
    @Override
    public void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        for (IEntityDao entityDao : mEntityDao) {
            entityDao.dropAllTables(db, ifExists);
        }
    }
}
