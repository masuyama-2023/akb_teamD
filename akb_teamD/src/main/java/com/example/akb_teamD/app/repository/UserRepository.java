package com.example.akb_teamD.app.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void userEdit() {

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

    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }
}