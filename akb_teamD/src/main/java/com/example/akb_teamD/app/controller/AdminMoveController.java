package com.example.akb_teamD.app.controller;


import com.example.akb_teamD.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:管理者用フォームの遷移を管理
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/
@Controller
public class AdminMoveController {

    private HttpSession session;
    private UserService userService;
    @Autowired
    public AdminMoveController(HttpSession session, UserService userService){
        this.session = session;
        this.userService = userService;
    }



    @GetMapping("times")
    public String displayTimes(Model model) {

        return "adm_display_time";
    }

    @PostMapping("sel_time")
    public String selectDisplayTimes(Model model) {
        return "adm_select_disp_times";
    }

    @GetMapping("user_add")
    public String userAdd(Model model) {
        return "adm_user_add";
    }

    @PostMapping("user_edit")
    public String userEdit(Model model){
        return "adm_user_edit";
    }

    @GetMapping("user_list")
    public String userList(Model model) {
        return "adm_userList";
    }

    public UserService getUserService(){
        return userService;
    }

}



