package com.example.akb_teamD.app.controller;
import com.example.akb_teamD.app.service.UserService;


import com.example.akb_teamD.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private UserService userService;
    @Autowired
    public GlobalMoveController(UserService userService){
        this.userService = userService;
    }
    private JdbcTemplate jdbcTemplate;
    private com.example.akb_teamD.app.repository.UserRepository UserRepository;




    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/user_attendanceList")
    public String view_attendance(Model model) {
        model.addAttribute("attendList",getUserService().readAttendance());
        return "user_attendanceList";
    }

    @GetMapping("/user_diligence")
    public String diligence(Model model){
        return "user_diligence";
    }

    //TODO ログインしているユーザーの情報から勤怠履歴を取得

    //表示する日付の関数
    private static String past_month;
    private static String next_month;

    @GetMapping("/user_disp_history")
    public String history(Model model) {
        String ID = UserRepository.getID();

        //今日の日付を取得
        LocalDate currentDate = LocalDate.now();
        //今日の日にちを取得
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        String day = currentDate.format(dayFormatter);


        //年月を格納
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy/MM");

        //日付から表示する日付を設定
        if (15 <= Integer.parseInt(day)) {
            //次の月を取得
            LocalDate changeMonth = currentDate.plusMonths(1);
            DateTimeFormatter nexMonthFormatter = DateTimeFormatter.ofPattern("yyyy/MM");

            next_month = changeMonth.format(nexMonthFormatter);
            past_month = currentDate.format(monthFormatter);

        } else {
            //前の月を取得
            LocalDate changeMonth = currentDate.minusMonths(1);
            DateTimeFormatter nexMonthFormatter = DateTimeFormatter.ofPattern("yyyy/MM");


            next_month = changeMonth.format(monthFormatter);
            past_month = currentDate.format(nexMonthFormatter);
        }

        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);


        //postgres
        String sql_sel = "SELECT *,to_char(break_end - break_begin, 'HH24:MI:SS') AS break_sum," +
                "to_char((end_time - begin_time) - (break_end - break_begin), 'HH24:MI:SS') AS working" +
                " FROM attendances_table WHERE '" + past_month + "/15' <= date AND date < '"+ next_month +
                "/15'AND id ="+ID ;

        System.out.println(jdbcTemplate.queryForList(sql_sel));

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);
        //

        return "user_disp_history";
    }

    // past_monthのgetter
    public static String getPastMonth() {
        return past_month;
    }

    // next_monthのgetter
    public static String getNextMonth() {
        return next_month;
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
