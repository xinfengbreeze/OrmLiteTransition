package com.example.testormlitetransaction.dao;

import android.content.Context;
import android.util.Log;

import com.example.testormlitetransaction.Bean.Book;
import com.example.testormlitetransaction.Bean.Student;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.concurrent.Callable;
public class BookDao {
    DbHelper helper  = null;
    Dao<Book,Integer> bookDao = null;
    Dao<Student,Integer> studentDao = null;
    public BookDao(Context context){
      helper  = DbHelper.getDbHelperInstance(context);
      bookDao  =  helper.getBeanDao(Book.class);
        studentDao  = helper.getBeanDao(Student.class);
    }


    public void saveBoookAndStudent(Book book) throws Exception {
        /**这里面千万不要捕获掉异常 必须上抛 这样TransactionManager才知道有没有产生异常**/
        Student student  = book.getStudent();
//        int i = 1/0;
//        Log.w("YKF","i"+i);   //模拟一个异常
        studentDao.createIfNotExists(student);
//        int i = 1/0;
//        Log.w("YKF","i"+i);   //模拟一个异常
        bookDao.createIfNotExists(book);
//        int i = 1/0;
//        Log.w("YKF","i"+i);   //模拟一个异常
    }
    public boolean SaveBookInTransaction(final Book book){
        boolean  result = false ;
        //创建事务管理器
        TransactionManager transactionManager  = new TransactionManager(helper.getConnectionSource());
        //一个调用的事件
        Callable<Boolean>  callable  = new Callable<Boolean>() {  //java.util.concurrent.Callable;
            @Override
            public Boolean call() throws Exception {//如果异常被抛出 事件管理 就知道保存数据失败要回滚
                saveBoookAndStudent(book);
                return true;
            }
        };

        try {
          result =  transactionManager.callInTransaction(callable);//执行事件
        } catch (SQLException e) {
            result = false;
            Log.w("YKF","事务保存异常");
            e.printStackTrace();
        }
        return result;
    }

}
