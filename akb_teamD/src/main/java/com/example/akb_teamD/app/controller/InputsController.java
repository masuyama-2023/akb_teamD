package com.example.akb_teamD.app.controller;

import com.example.akb_teamD.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

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
    public InputsController(HttpSession session, UserService userService) {
        this.session = session;
        this.userService = userService;
    }


    public LocalTime getTime() {
        return LocalTime.now();
    }

    public LocalDate getDate() {
        return LocalDate.now();
    }

    //出勤退勤ボタン
    @PostMapping("/user_diligence")
    public String diligence(Model model) {
        model.addAttribute("role",session.getAttribute("role"));
        return "user_diligence";
    }


    //データベースに情報の挿入&表示
    @PostMapping("/setPlace")
    public String attendanceList(@RequestParam("place") String place, Model model) {

        int id = (int) session.getAttribute("id");
        getUserService().place(id, place, getDate());

        model.addAttribute("attendList", getUserService().readAttendance(getDate()));
        model.addAttribute("role",session.getAttribute("role"));
        System.out.println(getUserService().readAttendance(getDate()));
        return "user_attendanceList";
    }


    //TODO 勤怠管理ボタンの中身実装(return値は仮入力されたもの)
    @GetMapping("/workIn")
    public String workIn(Model model) {
        model.addAttribute("role",session.getAttribute("role"));
        if(branchDiligence("workIn",model).equals("exceed")) {
            return "user_place";
        }
        else{
            return "user_diligence";
        }
    }

    @GetMapping("/workOut")
    public String workOut(Model model) {
        model.addAttribute("role",session.getAttribute("role"));
        if(branchDiligence("workOut",model).equals("exceed")) {
            return "user_attendanceList";
        }
        else{
            return "user_diligence";
        }
    }
    @GetMapping("/breakIn")
    public String breakIn(Model model) {
        model.addAttribute("role",session.getAttribute("role"));
        if (branchDiligence("breakIn", model).equals("exceed")) {
            return "user_attendanceList";
        } else {
            return "user_diligence";
        }
    }

    @GetMapping("/breakOut")
    public String breakOut(Model model) {
        model.addAttribute("role",session.getAttribute("role"));
        if (branchDiligence("breakOut", model).equals("exceed")) {
            return "user_attendanceList";
        } else {
            return "user_diligence";
        }
    }

    public UserService getUserService() {
        return userService;
    }

    String branchDiligence(String option, Model model){
        int id = (int) session.getAttribute("id");
        String name = (String) session.getAttribute("name");
        String checker = null;
        String result = null;
        if(option.equals("workIn")){
            checker = getUserService().checkAttend(id, getDate());

            if (checker.equals("No Record")) {
                getUserService().insertWorkStart(id, name, getTime(), getDate());
                result = "exceed";
            } else {
                model.addAttribute("name", "こんにちは " + name + " さん");
                model.addAttribute("Error", "ERROR : 出勤ボタンを押せません(2回目ではないか、確認してください。)");
                result ="error";
            }

        }
        else if(option.equals("breakIn")){
            checker = getUserService().checkStatus(id, getDate());
            if (checker.equals("勤務中")) {
                if(getUserService().checkBreak(id, getDate()).equals("First Break")){

                    /* 休憩開始時刻の打刻処理*/

                    getUserService().updateBreakStart(id, getDate(), getTime());
                    model.addAttribute("attendList", getUserService().readAttendance(getDate()));
                    result = "exceed";
                }
                else{
                    /* 休憩開始時刻のエラー処理*/
                    model.addAttribute("name", "こんにちは " + name + " さん");
                    model.addAttribute("Error", "ERROR : 休憩開始ボタンを押せません(2度目の休憩は考慮されていません。)");
                    result = "error";
                }

            } else {
                /* 休憩開始時刻のエラー処理*/
                model.addAttribute("name", "こんにちは " + name + " さん");
                model.addAttribute("Error", "ERROR : 休憩開始ボタンを押せません(勤務状況を確認して正しいボタンを押してください。)");
                result = "error";
            }

        }
        else if(option.equals("breakOut")){
            checker = getUserService().checkStatus(id, getDate());
            if (checker.equals("休憩中")) {
                getUserService().updateBreakEnd(id, getDate(), getTime());
                model.addAttribute("attendList", getUserService().readAttendance(getDate()));
                result = "exceed";
            } else {
                model.addAttribute("name", "こんにちは " + name + " さん");
                model.addAttribute("Error", "ERROR : 休憩終了ボタンを押せません(勤務状況を確認して正しいボタンを押してください。)");
                result = "error";
            }
        }

        else{
            checker = getUserService().checkStatus(id, getDate());
            if (checker.equals("勤務中")) {
                getUserService().updateWorkEnd(id, getDate(), getTime());
                model.addAttribute("attendList", getUserService().readAttendance(getDate()));
                result = "exceed";
            } else {
                model.addAttribute("name", "こんにちは " + name + " さん");
                model.addAttribute("Error", "ERROR : 退勤ボタンを押せません(勤務状況を確認して正しいボタンを押してください。)");
                result = "error";
            }
        }
        return result;
    }

}



