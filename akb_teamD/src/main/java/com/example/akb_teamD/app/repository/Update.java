package com.example.akb_teamD.app.repository;

import java.sql.SQLException;

public interface Update {

    void breakStart(); //休憩開始
    void breakEnd(); //休憩終了
    void WorkEnd(); //退勤
    void place(); //勤務地登録

<<<<<<< HEAD
    void userEdit(int no, String name,int id,String pass); //ユーザー編集
=======
    void userEdit() ; //ユーザー編集
>>>>>>> 8995833084cae87426a20fc67cf1fb3fea2a9cc4

}
