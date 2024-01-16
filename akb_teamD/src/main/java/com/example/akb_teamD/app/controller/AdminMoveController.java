package com.example.akb_teamD.app.controller;


import com.example.akb_teamD.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public String userEdit(@RequestParam("userId") int id, Model model){

        List<Map<String, Object>> users = new ArrayList<>();
        users = userService.getUserById(id);

        model.addAttribute("name",users.get(0).get("name"));
        model.addAttribute("beforeId",id);
        model.addAttribute("afterId",id);
        model.addAttribute("pass",users.get(0).get("password"));


        return "adm_user_edit";
    }

    @PostMapping("user_edit_check")
    public String editCheck(@RequestParam("name") String name,@RequestParam("afterId") int afterId, @RequestParam("password")String pass,@RequestParam
            ("beforeId")int beforeId,Model model){
        int no = userService.getUserNo(beforeId);
        System.out.println(no);
        userService.updateUserEdit(no,name,afterId,pass);
        model.addAttribute("users",userService.readUserList());
        return "adm_userList";
    }

    @GetMapping("adm_user_list")
    public String userList(Model model) {

        System.out.println(userService.readUserList());
        model.addAttribute("users",userService.readUserList());
        return "adm_userList";
    }


    public UserService getUserService(){
        return userService;
    }

}



