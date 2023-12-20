package com.example.akb_teamD.app.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
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
public class UserRepository  implements Insert, Delete, View, Update{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sql = null;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO 各メソッドにSQL処理の記述

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   INSERT文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void workStart(int id, String name, String time) {
        getJdbcTemplate().update(
         "INSERT INTO attendances_table (id,name,date,begin_time,status,place) VALUES(" + id + ",'"+ name +"','2023-11-27','" + time + "','出勤中','登録予定') ");
    }

    @Override
    public void address() {

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
    public void place() {

    }

    @Override
    public void userEdit(int no, String name, int id,String pass) {
        sql = "UPDATE users_table SET id = "+ id +", name = '"+ name +"',password = '"+ pass +"' WHERE no = " + no;
        jdbcTemplate.update(sql);
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
        sql = "SELECT * FROM users_table ORDER BY no ASC";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String,Object>> getUserById(int id){
        sql="SELECT * FROM users_table WHERE id = "+ id;
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
        sql = "SELECT name FROM users_table WHERE id = " + id + "AND password = '" + pass +"'";
        list = jdbcTemplate.queryForList(sql);
        if(list == null || list.size() == 0){
            return "No Name";
        }

        System.out.println(list);


        return (String) list.get(0).get("name");
    }

    @Override
    public String getRole(int id) {
        sql = "SELECT role FROM users_table WHERE id = " + id;

        List<Map<String, Object>> list = new ArrayList<>();
        list = jdbcTemplate.queryForList(sql);
        if(list == null || list.size() == 0){
            return "No Role";
        }

        return (String) jdbcTemplate.queryForList(sql).get(0).get("role");
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

    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }



}
