package com.example.akb_teamD.app.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:SQL全般を司る
        JdbcTemplateの最適化
        SQL文の定数化

  = = = = = = = = = = = = = = = = = = = = = =*/


@Repository
public class UserRepository  implements Create, Delete, View, Update{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sql = null;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    //TODO 各メソッドにSQL処理の記述

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   INSERT文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void workStart(int id, String name, LocalTime time, LocalDate date) {
        sql = "INSERT INTO attendances_table(id,name,date,begin_time,status,place) VALUES(?,?,?,?,'勤務中','登録予定')";
        time = LocalTime.parse(time.format(timeFormatter));
        jdbcTemplate.update(sql,id,name,date,time);
    }

    @Override
    public void address(int id, String name, String phone, String mail, String remark) {
        sql = "INSERT INTO address_table (id,name,phone,mail,other) VALUES(?,?,?,?,?)";
        getJdbcTemplate().update(sql,id,name,phone,mail,remark);

    }


    @Override
    public void adm_add() {

    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   DELETE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void userDelete() {

    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   UPDATE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void breakStart() {

    }

    @Override
    public void breakEnd() {

    }

    @Override
    public void WorkEnd() {

    }

    @Override
    public void place(int id, String place, LocalDate date) {
        sql = "UPDATE attendances_table SET place = ? WHERE id = ? AND date = ?";
        jdbcTemplate.update(sql,place,id,date);
    }

    @Override
    public void userEdit()  {

    }

    @Override
    public void updateAddress(int id,String name,String phone,String mail, String remark){
        sql = "UPDATE address_table SET id = ?,name = ?,phone = ?,mail = ?,other = ?  WHERE id = ?";
        jdbcTemplate.update(sql,id,name,phone,mail,remark,id);
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   SELECT文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public List<Map<String, Object>> findAttendance() {
        sql = "SELECT * FROM attendances_table";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findDisplayTimes() {
        sql = "SELECT * FROM attendances_table";
        return jdbcTemplate.queryForList(sql);

    }

    @Override
    public List<Map<String, Object>> findUserList() {
        sql = "SELECT * FROM attendances_table";
        return jdbcTemplate.queryForList(sql);

    }

    @Override
    public List<Map<String, Object>> findSelectTimes() {
        sql = "SELECT * FROM attendances_table";
        return jdbcTemplate.queryForList(sql);

    }

    @Override
    public List<Map<String, Object>> findHistory() {
        sql = "SELECT * FROM attendances_table";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public String loginCheck(int id, String pass) {
        List<Map<String, Object>> list = new ArrayList<>();
        sql = "SELECT name FROM users_table WHERE id = ?AND password = ?";
        list = jdbcTemplate.queryForList(sql, id,pass);
        if(list == null || list.size() == 0){
            return "No Name";
        }

        System.out.println(list);


        return (String) list.get(0).get("name");
    }

    @Override
    public String getRole(int id) {
        sql = "SELECT role FROM users_table WHERE id = "+ id;

        List<Map<String, Object>> list = new ArrayList<>();
        list = jdbcTemplate.queryForList(sql);
        if(list == null || list.size() == 0){
            return "No Role";
        }

        return (String) jdbcTemplate.queryForList(sql).get(0).get("role");
    }

    public List<Map<String, Object>> findRecord(int id){
        sql = "SELECT name FROM address_table WHERE id = ?";

        return jdbcTemplate.queryForList(sql,id);
    }

    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }



}
