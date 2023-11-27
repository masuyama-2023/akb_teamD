package com.example.akb_teamD.app.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

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

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

/*
    @Override
    public void attendance_view(Model model) {



        String sql = "SELECT * FROM attendances_table";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        model.addAttribute("attendList",list);
        System.out.println(list);

    }
*/

    //TODO 各メソッドにSQL処理の記述
    @Override
    public void workStart() {

    }

    @Override
    public void address() {

    }

    @Override
    public void adm_add() {

    }

    @Override
    public void userDelete() {

    }

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

    @Override
    public void findAttendance() {

    }

    @Override
    public void findDisplayTimes() {

    }

    @Override
    public void findUserList() {

    }

    @Override
    public void findSelectTimes() {

    }

    @Override
    public void findHistory() {

    }
}