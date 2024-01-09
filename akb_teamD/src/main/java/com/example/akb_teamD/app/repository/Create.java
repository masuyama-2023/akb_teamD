package com.example.akb_teamD.app.repository;

public interface Create {

    void workStart(int id, String name, String time); //出勤登録
    void address(String phone,String mail,String remark); //連絡先登録です

    void adm_add(); //ユーザー登録


}
