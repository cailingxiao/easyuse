package com.cai.easyuse.database.core;

import com.cai.easyuse.database.dao.DaoSession;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * 数据库dao的配置daoconfig，一般使用Entity继承
 * <p/>
 * Created by cailingxiao on 2017/1/26.
 */
public interface IDaoConfig {
    //    IdentityScopeType type, Class<T> entityClass, AbstractDao<T, ?> dao

    /**
     * 返回config的scope
     *
     * @return
     */
    IdentityScopeType getType();

    /**
     * 返回entity.class
     *
     * @return
     */
    Class<? extends AbstractDao<?, ?>> getClazz();

    /**
     * 返回entityDao
     *
     * @return
     */
    AbstractDao getDao(DaoConfig config, DaoSession daoSession);

}
