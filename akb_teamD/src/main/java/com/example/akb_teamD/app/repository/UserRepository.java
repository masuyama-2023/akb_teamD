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

import static java.util.Objects.isNull;

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
    private List<Map<String, Object>> list = new ArrayList<>();
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
        sql = "INSERT INTO attendances_table(id,name,date,begin_time,status,place,flag_break) VALUES(?,?,?,?,'勤務中','登録予定',0)";
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
    public void userDelete(int id) {
        sql = "DELETE FROM users_table WHERE id = ?";
        jdbcTemplate.update(sql,id);

    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   UPDATE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void breakStart(int id, LocalDate date, LocalTime time) {

        time = LocalTime.parse(time.format(timeFormatter));
        sql = "UPDATE attendances_table SET break_begin = ?,status = '休憩中',flag_break = 1 WHERE id = ? AND date = ?";
        jdbcTemplate.update(sql,time,id,date);

    }

    @Override
    public void breakEnd(int id, LocalDate date, LocalTime time) {

        time = LocalTime.parse(time.format(timeFormatter));
        sql = "UPDATE attendances_table SET break_end = ?,status = '勤務中' WHERE id = ? AND date  = ?";
        jdbcTemplate.update(sql,time,id,date);

    }

    @Override
    public void WorkEnd(int id, LocalDate date, LocalTime time) {

        time = LocalTime.parse(time.format(timeFormatter));
        sql = "UPDATE attendances_table SET end_time = ?,status = '退勤済' WHERE id = ? AND date = ?";
        jdbcTemplate.update(sql,time,id,date);

    }

    @Override
    public void place(int id, String place, LocalDate date) {
        sql = "UPDATE attendances_table SET place = ? WHERE id = ? AND date = ?";
        jdbcTemplate.update(sql,place,id,date);
    }

    @Override
    public void userEdit(int no, String name,int beforeId ,int afterId,String pass) {
        //users_table編集
        sql = "UPDATE users_table SET id = ?, name = ? ,password = ?  WHERE no = ?";
        jdbcTemplate.update(sql,afterId,name,pass,no);
        //address_table編集
        sql = "UPDATE address_table SET id = ?, name = ?WHERE id = ?";
        jdbcTemplate.update(sql,afterId,name,beforeId);
        //attendances_table編集
        sql = "UPDATE attendances_table SET id = ?, name = ? WHERE id = ?";
        jdbcTemplate.update(sql,afterId,name,beforeId);


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
    public List<Map<String, Object>> findAttendance(LocalDate date) {
        sql = "SELECT attendances_table.id AS id,attendances_table.name AS name,date,begin_time,break_begin,break_end,end_time,status,place,mail,phone\n" +
                "FROM attendances_table\n" +
                "INNER JOIN address_table\n" +
                "ON attendances_table.id = address_table.id\n" +
                "WHERE date = ?\n" +
                "ORDER BY id ASC";
        return jdbcTemplate.queryForList(sql, date);
    }

    @Override
    public List<Map<String, Object>> findDisplayTimes() {
        sql = "SELECT * FROM attendances_table";
        return jdbcTemplate.queryForList(sql);

    }


    /*直打ちを確認したため、後日編集*/

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

    public String checkAttendRecord(int id, LocalDate date) {
        sql = "SELECT * FROM attendances_table WHERE id = ? AND date = ?";
        System.out.println(jdbcTemplate.queryForList(sql,id,date));
        list = jdbcTemplate.queryForList(sql,id,date);
        if (list == null || list.size() == 0) {
            return "No Record";
        }
        return "exceed";
    }

    public String checkStatus(int id, LocalDate date){
        sql = "SELECT * FROM attendances_table WHERE id = ? AND date = ?";
        list = jdbcTemplate.queryForList(sql,id,date);
        if (list == null || list.size() == 0) {
            return "No Record";
        }
        return (String)list.get(0).get("status");

    }

    public String checkBreak(int id, LocalDate date){
        sql = "SELECT flag_break FROM attendances_table WHERE id = ? AND date = ?";
        list = jdbcTemplate.queryForList(sql,id,date);

        int check = (int) list.get(0).get("flag_break");
        System.out.println(check);
        if (check == 0) {
            return "First Break";
        }
        else{
            return "Second";
        }


    }
    public int getUserNo(int id) {
        sql = "SELECT no FROM users_table WHERE id = "+id;

        List<Map<String, Object>> list = new ArrayList<>();
        list = jdbcTemplate.queryForList(sql);
        if(list == null || list.size() == 0){
            return 0;
        }
        System.out.println("レコードが存在します。");
        return (int) jdbcTemplate.queryForList(sql).get(0).get("no");
    }

    @Override
    public List<Map<String, Object>> findUserList() {
        sql = "SELECT * FROM users_table ORDER BY no ASC";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> getUserById(int id){
        sql="SELECT * FROM users_table WHERE id = "+ id;
        return jdbcTemplate.queryForList(sql);
    }

    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }



}
