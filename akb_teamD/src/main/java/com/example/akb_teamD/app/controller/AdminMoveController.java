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




    @GetMapping("adm_user_add")
    public String moveAdd() {

        return "adm_user_add";
    }

    @PostMapping("adm_user_add")
    public String userAdd(@RequestParam("name") String name, @RequestParam("id") int id,@RequestParam("pass") String pass ,@RequestParam("authority") String role , Model model){


        List<Map<String,Object>> list = new ArrayList<>();
        list = userService.getUserById(id);
        if( list.size() != 0) {
            System.out.println("idの重複");
            model.addAttribute("name", name);
            model.addAttribute("id", id);
            model.addAttribute("pass", pass);

            model.addAttribute("errors", "IDが重複しています。");
            return "adm_user_add";
        }

        userService.insertAdmAdd(name,id,pass,role);

        model.addAttribute("users",userService.readUserList());
        return "adm_userList";
    }


    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public String userEdit(@RequestParam("userId") int id, Model model){

        List<Map<String, Object>> users = new ArrayList<>();
        users = userService.getUserById(id);

        model.addAttribute("name",users.get(0).get("name"));
        model.addAttribute("id",id);
        model.addAttribute("pass",users.get(0).get("password"));
        model.addAttribute("role",users.get(0).get("role"));

        return "adm_user_edit";
    }

    @PostMapping("user_edit_check")
    public String editCheck(@RequestParam("afterName") String name,
                            @RequestParam("afterId") int afterId,
                            @RequestParam("afterPass")String pass,
                            @RequestParam("beforeId")int beforeId,
                            @RequestParam("beforeName") String beforeName,
                            @RequestParam("afterPass")String beforePass,
                            @RequestParam("authority") String role,
                            Model model){
        //users_tableの主キーの取得
        int no = userService.getUserNo(beforeId);
        //編集後のidの重複確認
        List<Map<String,Object>> list = new ArrayList<>();
        list = userService.getUserById(afterId);
        if(beforeId != afterId && list.size() != 0) {
                System.out.println("idの重複");
            model.addAttribute("name",beforeName);
            model.addAttribute("id",beforeId);
            model.addAttribute("pass",beforePass);
            model.addAttribute("role",list.get(0).get("role"));

            model.addAttribute("errors","IDが重複しています。");
            return "adm_user_edit";
        }

        userService.updateUserEdit(no,name,beforeId,afterId,pass,role);
        model.addAttribute("users",userService.readUserList());
        model.addAttribute("exceed","編集が完了しました。");
        return "adm_userList";
    }

    @GetMapping("adm_user_list")
    public String userList(Model model) {

        System.out.println(userService.readUserList());
        model.addAttribute("users",userService.readUserList());
        return "adm_userList";
    }

    @PostMapping("delete_check")
    public String deleteUser(@RequestParam("beforeId") int id, Model model){
        userService.userDelete(id);
        model.addAttribute("users",userService.readUserList());
        return "adm_userList";
    }


    public UserService getUserService(){
        return userService;
    }

}



