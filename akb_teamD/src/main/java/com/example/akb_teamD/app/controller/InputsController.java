package com.example.akb_teamD.app.controller;

import com.example.akb_teamD.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:情報入力系のフォーム遷移を管理
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/



@Controller
public class InputsController {
    private HttpSession session;
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public InputsController(HttpSession session, UserService userService){
        this.session = session;
        this.userService = userService;
    }



    public String getTime() {
        Date nowtime = new Date();
        //System.out.println(new SimpleDateFormat("hh:mm:ss").format(today));
        return new SimpleDateFormat("HH:mm:ss").format(nowtime);
    }


    //出勤退勤ボタン
    @PostMapping("/user_diligence")
    public String diligence(Model model) {
        return "user_diligence";
    }



    @RequestMapping("user_place")
    public String Placepage(){
        return "user_place";
    }

    @PostMapping("/user_attendanceList")
    public String PlaceInput(@RequestParam("place") String place, Model model, HttpSession session){

//        Date daydate = new Date();
//        SimpleDateFormat data = new SimpleDateFormat("YYYY-MM-DD");
//        System.out.println("aaa" + data.format(daydate));

        LocalDate days = LocalDate.now();


        String sql = "UPDATE attendances_table SET place = ? WHERE id = ? and date = ?";
        jdbcTemplate.update(sql, place, (int)session.getAttribute("id"), days);

        return "user_attendanceList";

    }



    //TODO 勤怠管理ボタンの中身実装(return値は仮入力されたもの)
    @GetMapping("/workIn")
    public String workIn(Model model) {

        getUserService().insertWorkStart((int)session.getAttribute("id"),(String)session.getAttribute("name"),getTime());
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

    public UserService getUserService(){
        return userService;
    }

}


