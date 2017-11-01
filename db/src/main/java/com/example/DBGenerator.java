package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DBGenerator {
    // new db version
    public static final int DB_VERSION = 1;

    public static final void main(String[] args) {
        new DBGenerator().launch();
    }

    private void launch() {
        // entity package
        Schema schema = new Schema(DB_VERSION, "cn.zaixiandeng.niu.kxvideo.modules.list.model.entity");

        // path about(DaoMaster、DaoSession、Dao)
        schema.setDefaultJavaPackageDao("com.cai.easyuse.database.dao");

        // keep
        schema.enableActiveEntitiesByDefault();
        schema.enableKeepSectionsByDefault();

//        addDownloadEntity(schema);
        addDirItemEntity(schema);

        // code path
        try {
            new DaoGenerator().generateAll(schema, "d://src");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDirItemEntity(Schema schema) {
        Entity news = schema.addEntity("DirItemEntity");
        news.implementsInterface("com.cai.easyuse.base.mark.UnConfusion");
        news.setTableName("DirItemTable");
        news.addStringProperty("name").columnName("name");
        news.addIntProperty("count").columnName("count");
        news.addLongProperty("lastModTime").columnName("lastModTime");
        news.addStringProperty("path").columnName("path").primaryKey();
    }

    private void addDownloadEntity(Schema schema) {
        Entity news = schema.addEntity("ExampleTableEntity");
        news.implementsInterface("com.cai.easyuse.base.mark.UnConfusion");
        news.setTableName("ExampleTable");
        //        news.addLongProperty("id").columnName("_id").primaryKey().autoincrement().notNull();
        news.addStringProperty("eid").columnName("eid").primaryKey();
    }
}
