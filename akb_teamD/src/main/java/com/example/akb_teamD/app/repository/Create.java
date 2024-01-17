package com.example.akb_teamD.app.repository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Create {

    void workStart(int id, String name, LocalTime time, LocalDate date); //出勤登録
    void address(int id,String name, String phone,String mail,String remark); //連絡先登録です


    void adm_add(String name, int id,String pass ,String role); //ユーザー登録


}
