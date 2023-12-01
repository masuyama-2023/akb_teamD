package com.example.akb_teamD.app.controller;
import com.example.akb_teamD.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import com.example.akb_teamD.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:ユーザー用フォームの遷移を管理 & 全体で使うクラスの定義を行う
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/


@Controller
public class GlobalMoveController
{
    private HttpSession session;
    private UserService userService;
    @Autowired
    public GlobalMoveController(HttpSession session, UserService userService){
        this.session = session;
        this.userService = userService;
    }

    @GetMapping("/")
    public String login() {
        return "global_login";
    }


    @GetMapping("/user_attendanceList")
    public String view_attendance(Model model) {
        model.addAttribute("attendList",getUserService().readAttendance());
        return "user_attendanceList";
    }

    @GetMapping("/user_diligence")
    public String diligence(Model model){
        model.addAttribute("name","こんにちは "+session.getAttribute("name")+" さん");
        return "user_diligence";
    }

    //TODO ログインしているユーザーの情報から勤怠履歴を取得
    @GetMapping("/user_disp_history")
    public String history(Model model) {
        return "user_disp_history";
    }

    @GetMapping("/user_place")
    public String place(Model model) {
        return "user_place";
    }


    @GetMapping("/user_contact_address")
    public String address(Model model) {
        return "user_contact_address";
    }


    public UserService getUserService(){
        return userService;
    }

}
