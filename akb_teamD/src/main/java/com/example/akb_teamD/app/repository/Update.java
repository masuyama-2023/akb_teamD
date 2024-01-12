package com.example.akb_teamD.app.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public interface Update {

    void breakStart(int id, LocalDate date, LocalTime time); //休憩開始
    void breakEnd(); //休憩終了
    void WorkEnd(); //退勤
    void place(int id, String place, LocalDate date); //勤務地登録

    void userEdit() ; //ユーザー編集

    void updateAddress(int id, String name,String phone, String mail,String remark);
}
