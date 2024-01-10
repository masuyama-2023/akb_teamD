package com.example.akb_teamD.app.controller;
import com.example.akb_teamD.app.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import com.example.akb_teamD.app.service.UserService;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:ユーザー用フォームの遷移を管理 & 全体で使うクラスの定義を行う
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/

@ConfigurationProperties(prefix = "spring.datasource")
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
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //private com.example.akb_teamD.app.repository.UserRepository UserRepository;


    @GetMapping("/")
    public String login() {
        return "global_login";
    }


    @GetMapping("/user_attendanceList")
    public String view_attendance(Model model) {
        model.addAttribute("attendList",getUserService().readAttendance());
        return "user_attendanceList";
    }

    @GetMapping("/user_place")
    public String viewPlace(Model model){
        return "user_place";
    }

      /////勤怠履歴///////
    //TODO ログインしているユーザーの情報から勤怠履歴を取得
    private static String past_month = null;
    private static String next_month = null;

    public static void Click_MonthMutation(String action) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/[]M");
        //計算をするために型を変える
        YearMonth pastMonth_date = YearMonth.parse(past_month, formatter);
        YearMonth nextMonth_date = YearMonth.parse(next_month, formatter);

        if ("前月へ".equals(action)) {
            YearMonth past_resultDate = pastMonth_date.minusMonths(1);
            YearMonth next_resultDate = nextMonth_date.minusMonths(1);

            past_month = past_resultDate.format(formatter);
            next_month = next_resultDate.format(formatter);
        } else if ("次月へ".equals(action)) {
            YearMonth past_resultDate = pastMonth_date.plusMonths(1);
            YearMonth next_resultDate = nextMonth_date.plusMonths(1);

            past_month = past_resultDate.format(formatter);
            next_month = next_resultDate.format(formatter);
        } else {
            System.out.println("Click_MonthMutation無効");
        }
    }

    @GetMapping("/user_disp_history")
    public String history(Model model) {
        //今日の日付を取得
        LocalDate currentDate = LocalDate.now();
        //今日の日にちを取得
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("[]d");
        String day = currentDate.format(dayFormatter);
        //年月を格納
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy/[]M");

        //日付から表示する日付を設定
        if (15 <= Integer.parseInt(day)) {
            //次の月を取得
            LocalDate changeMonth = currentDate.plusMonths(1);

            next_month = changeMonth.format(Formatter);
            past_month = currentDate.format(Formatter);
        } else {
            //前の月を取得
            LocalDate changeMonth = currentDate.minusMonths(1);

            past_month = changeMonth.format(Formatter);
            next_month = currentDate.format(Formatter);
        }
        //formに月を送る
        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);

        //postgres
        String sql_sel = "SELECT *,to_char(break_end - break_begin, 'HH24:MI:SS') AS break_sum," +
                "to_char((end_time - begin_time) - (break_end - break_begin), 'HH24:MI:SS') AS working" +
                " FROM attendances_table WHERE '" + past_month + "/15' <= date AND date < '"+ next_month +
                "/15'AND id ="+ session.getAttribute("id");

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);

        return "user_disp_history";
    }
    @PostMapping("/user_disp_history")
    public String history_click(HttpServletRequest request,Model model ) {
        // ボタンがクリックされたかどうかを判定
        String action = request.getParameter("action");
        // 月の変化処理
        Click_MonthMutation(action);

        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);
        //postgres
        String sql_sel = "SELECT *,to_char(break_end - break_begin, 'HH24:MI:SS') AS break_sum," +
                "to_char((end_time - begin_time) - (break_end - break_begin), 'HH24:MI:SS') AS working" +
                " FROM attendances_table WHERE '" + past_month + "/15' <= date AND date < '"+ next_month +
                "/15' AND id ="+session.getAttribute("id");

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);

        return "user_disp_history";
    }
    //////////////////
    ///運営用、勤怠時間集計一覧////
    @GetMapping("/adm_display_times")
    public String adm_history(Model model) {
        //今日の日付を取得
        LocalDate currentDate = LocalDate.now();
        //年と月を獲得する為のフォーマットを作成
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy/[]M");
        //次の月を取得
        LocalDate changeMonth = currentDate.plusMonths(1);

        next_month = changeMonth.format(Formatter);
        past_month = currentDate.format(Formatter);
        //formに月を送る
        model.addAttribute("fromJV_month", past_month);

        //postgres(user)
        String sql_user = "SELECT *FROM users_table";
        List<Map<String, Object>> usersList = this.jdbcTemplate.queryForList(sql_user);
        model.addAttribute("fromJV_user", usersList);

        //postgres
        String sql_sel = "SELECT id,name,TO_CHAR(SUM((end_time - begin_time) - (break_end - break_begin))," +
                " 'HH24:MI:SS')" + " AS working_sum"
                + " FROM attendances_table"
                + " WHERE '" + past_month + "/01' <= date AND date < '" + next_month +"/01'"
                + " GROUP BY id,name";
        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);

        return "adm_display_times";
    }
    @PostMapping("/adm_display_times")
    public String adm_history_click(HttpServletRequest request, Model model,
                                    @RequestParam("place") String selectedPlace) {
        String search = selectedPlace;
        // ボタンがクリックされたかどうかを判定
        String action = request.getParameter("action");
        // 月の変化処理
        Click_MonthMutation(action);
        //formに月を送る
        model.addAttribute("fromJV_month", past_month);

        //postgres(user)
        String sql_user = "SELECT *FROM users_table;";
        List<Map<String, Object>> usersList = this.jdbcTemplate.queryForList(sql_user);
        model.addAttribute("fromJV_user", usersList);

        if ("検索".equals(action)&& !"'f'".equals(search)) {
            search = "id";
        }

        //postgres
        String sql_sel = "SELECT id,name,TO_CHAR(SUM((end_time - begin_time) - (break_end - break_begin))," +
                " 'HH24:MI:SS')" + " AS working_sum"
                + " FROM attendances_table"
                + " WHERE '" + past_month + "/01' <= date AND date < '" + next_month +"/01'"
                + "AND "+selectedPlace+"="+search
                + " GROUP BY id,name";

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);

        return "adm_display_times";
    }
    ////////////////////////////
    ///運営用、選択した人の勤務時間集計一覧////
    @GetMapping("/adm_select_disp_times")
    public String adm_select(Model model) {
        //今日の日付を取得
        LocalDate currentDate = LocalDate.now();
        //今日の日にちを取得
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("[]d");
        String day = currentDate.format(dayFormatter);
        //年月を格納
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy/[]M");

        //日付から表示する日付を設定
        if (15 <= Integer.parseInt(day)) {
            //次の月を取得
            LocalDate changeMonth = currentDate.plusMonths(1);

            next_month = changeMonth.format(Formatter);
            past_month = currentDate.format(Formatter);
        } else {
            //前の月を取得
            LocalDate changeMonth = currentDate.minusMonths(1);

            past_month = changeMonth.format(Formatter);
            next_month = currentDate.format(Formatter);
        }
        //formに月を送る
        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);

        //postgres(user)
        String sql_user = "SELECT *FROM users_table";
        List<Map<String, Object>> usersList = this.jdbcTemplate.queryForList(sql_user);
        model.addAttribute("fromJV_user", usersList);

        return "adm_select_disp_times";
    }
    @PostMapping("/adm_select_disp_times")
    public String adm_select_click(HttpServletRequest request, Model model,
                                    @RequestParam("place") String selectedPlace) {
        String search = selectedPlace;
        // ボタンがクリックされたかどうかを判定
        String action = request.getParameter("action");
        // 月の変化処理
        Click_MonthMutation(action);
        //formに月を送る
        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);

        //postgres(user)
        String sql_user = "SELECT *FROM users_table;";
        List<Map<String, Object>> usersList = this.jdbcTemplate.queryForList(sql_user);
        model.addAttribute("fromJV_user", usersList);

        if ("検索".equals(action)&& !"null".equals(search)) {
            search = "id";
        }

        //postgres
        String sql_sel = "SELECT *,to_char(break_end - break_begin, 'HH24:MI:SS') AS break_sum,"
               + "to_char((end_time - begin_time) - (break_end - break_begin), 'HH24:MI:SS') AS working"
               + " FROM attendances_table WHERE '" + past_month + "/15' <= date AND date < '"+ next_month
               + "/15' AND "+selectedPlace+"="+search;

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);
        return "adm_select_disp_times";
    }
    ////////////////////////////



    /*-------多分ここからが自分（益山）の担当だと思う-------------*/


    @GetMapping ("/user_contact_address")
    public String address_get(){return "user_contact_address";}
    @PostMapping("/user_contact_address")
    public String address(HttpServletRequest request,
                          @RequestParam(name = "phone", required = false) String phone,
                          @RequestParam(name = "mail", required = false) String mail,
                          @RequestParam(name = "remark", required = false) String remark) throws SQLException {

        // 取得した内容をコンソールに表示(改変前（益山）)
        //String DatabaseName = DatabaseProperties.getDatabase();
        //String url = DatabaseProperties.getUrl();
        //String username = DatabaseProperties.getUsername();
       // String password = DatabaseProperties.getPassword();


        if (session.getAttribute("id") != null) {

                List<Map<String, Object>> list = new ArrayList<>();
                list = userService.findRecord((int)session.getAttribute("id"));

                int id = (int)session.getAttribute("id");
                String name = (String)session.getAttribute("name");

                //レコードがなければ新規追加、そうでなければ更新
                if (list == null || list.size() == 0) {
                    getUserService().insertAddress(id,name,phone,mail,remark);
                } else {
                    System.out.println("update");
                    getUserService().updateAddress(id,name,phone,mail,remark);
            }
        }
        return "user_contact_address";

    }


    public UserService getUserService(){
        return userService;
    }

}
