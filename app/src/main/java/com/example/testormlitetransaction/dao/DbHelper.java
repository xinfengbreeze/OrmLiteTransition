package com.example.testormlitetransaction.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.testormlitetransaction.Bean.Book;
import com.example.testormlitetransaction.Bean.Student;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbHelper extends OrmLiteSqliteOpenHelper {
    private static final int DBVersion = 1;
    private static final String DB_NAME = "test.db";
    private Map<String, Dao> daoMap  = new HashMap<>();
    private static DbHelper helper;


    private DbHelper(Context context) {
        super(context, getDbFilePath(context), null, DBVersion);
    }

    /**
     * 数据库helper 单例
     * @param context
     * @return
     */
    public static synchronized DbHelper getDbHelperInstance(Context context){
        if(helper == null){
            synchronized (DbHelper.class){
                if(helper ==null){
                    helper  = new DbHelper(context);
                }
            }
        }
        return  helper;
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTableIfNotExists(connectionSource,Student.class);
            TableUtils.createTableIfNotExists(connectionSource, Book.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
            for(int i = oldVersion  ;i< newVersion; i++){
                /**
                * 这里是为了依次升级防止跳版本
                */
                if(oldVersion ==1){

                }else if(oldVersion ==2){

                }else if(oldVersion ==3){

                }else if(oldVersion ==4){

                }

            }
    }

    /**
     * 在SDcard中创建数据库 或者
     * @return
     */
    private  static String getDbFilePath(Context context){
        boolean isSdcardEnable = false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){//SDCard是否插入
            isSdcardEnable = true;
        }
        String dbPath = null;
        if(isSdcardEnable){
            dbPath = Environment.getExternalStorageDirectory().getPath() + "/yuan/database/";
            File dbp = new File(dbPath);
            if(!dbp.exists()){
                dbp.mkdirs(); //创建文件夹
            }
            return  dbPath + DB_NAME;
        }else{//未插入SDCard，创建在应用下的database/中
            return DB_NAME;
        }
    }


    /**
     * 各个数据的dao维护在 hashmap 集合中
     * @param clazz
     * @param <T>
     * @param <ID>
     * @return
     */
    public  synchronized  <T,ID> Dao<T,ID>  getBeanDao(Class<T> clazz){
        String key  = clazz.getSimpleName();
        Dao<T,ID> dao  = null;
        if(daoMap.containsKey(key)){
            return daoMap.get(key);
        }else{
            try {
                dao = getDao(clazz);
                daoMap.put(key,dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    /**
     * 释放资源   一般在 application类中释放
     */
    @Override
    public void close() {
        daoMap.clear();
        super.close();
    }
}
/**
 *事件回滚大大概原理
 * 在开始操作数据库的开始前 首先保存一个SavePoint万一需要回滚就回到这个点
 * DatabaseConnection connection = connectionSource.getReadWriteConnection()；//数据库连接
 * SavePoint  savePoint = connection.setSavePoint("ORMLITE" + savePointCounter.incrementAndGet());//保存一个savePoint
 *
 *1、出现异常就进行回滚，回滚到执行事务前的状态savePoint   rollBack(connection, savePoint);
 *2、未出现异常提交事务，只有这句执行完了之后，你所做的操作才会在数据库中生效 commit(connection, savePoint);
 *
 * 关键点 transactionManager.callInTransaction(callable);中callable的call方法如果向上抛出异常  就会被认为事件失败
 * 会触发回滚，如果里面的方法try catch 掉了异常那么 事件管理就认为没有失败，所以不要在call里面将异常给处理掉
 * 要抛上去
 *
 * 当然也可以这样写
 //事务操作
 TransactionManager.callInTransaction(helper.getConnectionSource(),
 new Callable<Boolean>()
 {
 @Override
 public Boolean call() throws Exception
 {
 ******写数据库操作方法****
 return false;
 }
 });
 *这里的泛型可以是void  但还是为了获得是否保存成功的结果就使用了 Boolean
 */