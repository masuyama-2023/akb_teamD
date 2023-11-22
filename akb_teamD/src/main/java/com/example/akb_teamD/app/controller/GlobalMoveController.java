package com.example.akb_teamD.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:ユーザー用フォームの遷移を管理 & 全体で使うクラスの定義を行う
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/


@RequestMapping("")
@Controller
public class GlobalMoveController
{


    @Autowired
    private JdbcTemplate jdbcTemplate;




    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/user_attendanceList")
    public String view_attendance() {
        return "user_attendanceList";
    }

    @GetMapping("/user_diligence")
    public String diligence(){
        return "user_diligence";
    }

    //TODO ログインしているユーザーの情報から勤怠履歴を取得
    @GetMapping("/user_disp_history")
    public String history() {
        return "user_disp_history";
    }

    @GetMapping("/user_place")
    public String place() {
        return "user_place";
    }


    @GetMapping("/user_contact_address")
    public String address() {
        //ここの　String sql = 以下の文をちゃんと書ければ動くはず？現状は仮置き。
        //idとnameは現在ログインしてるユーザー情報を取得。残りの３つは入力欄に入力した内容を入れる。
        //String sql = "INSERT INTO address_table (id,name,phone,mail,other) VALUES (？？？,'？？？','？？？','？？？','？？？')";
        //jdbcTemplate.update(sql);
        return "user_contact_address";
    }

}
