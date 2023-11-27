package com.example.akb_teamD.app.controller;
import com.example.akb_teamD.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:情報入力系のフォーム遷移を管理
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/

@Controller
public class InputsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public String getTime() {
        Date today = new Date();
        //System.out.println(new SimpleDateFormat("hh:mm:ss").format(today));
        return new SimpleDateFormat("hh:mm:ss").format(today);

    }

    //出勤退勤ボタン
    @PostMapping("/user_diligence")
    public String diligence(Model model) {
        return "diligence";
    }


    //データベースに情報の挿入&表示
    @PostMapping("/user_attendanceList")
    public String attendanceList(Model model) {
        return "user_attendanceList";
    }



    //TODO 勤怠管理ボタンの中身実装(return値は仮入力されたもの)
    @GetMapping("/workIn")
    public String workIn(Model model) {
        return "user_place";
    }
    @GetMapping("/workOut")
    public String workOut(Model model) {
        return "user_attendanceList";
    }
    @GetMapping("/breakIn")
    public String breakIn(Model model) {
        return "user_attendanceList";
    }
    @GetMapping("/breakOut")
    public String breakOut(Model model) {
        return "user_attendanceList";
    }


}

