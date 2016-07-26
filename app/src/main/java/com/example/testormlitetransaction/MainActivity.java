package com.example.testormlitetransaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.testormlitetransaction.Bean.Book;
import com.example.testormlitetransaction.Bean.Student;
import com.example.testormlitetransaction.dao.BookDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public  void testSave(View v){
        Student stu = new Student();
        stu.setName("张三");
        stu.setAge(33);

        Book book = new Book();
        book.setBookName("android");
        book.setPrice(80);
        book.setStudent(stu);

        BookDao dao = new BookDao(getApplicationContext());
        boolean  result = dao.SaveBookInTransaction(book);

        Toast.makeText(this,"数据保存的结果:"+ result,Toast.LENGTH_SHORT).show();
        Log.i("YKF","第一个保存结果"+result);
        /**
         * Student 和Book的表是相关联的
         * 这里想达到如果一个保存失败另一个也不进行保存
         * 所以开启事务保存数据到数据库
         */
    }


}
