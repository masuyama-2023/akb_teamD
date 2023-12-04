package com.example.akb_teamD.app.repository;

import java.sql.SQLException;

public interface Update {

    void breakStart(); //休憩開始
    void breakEnd(); //休憩終了
    void WorkEnd(); //退勤
    void place(); //勤務地登録

    void userEdit(int id,String name,String phone,String mail,String remark) throws SQLException; //ユーザー編集

}
