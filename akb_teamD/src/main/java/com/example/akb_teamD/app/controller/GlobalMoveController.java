package com.example.akb_teamD.app.controller;
import com.example.akb_teamD.app.repository.UserRepository;
import com.example.akb_teamD.app.service.UserService;
import com.example.akb_teamD.app.controller.InputsController;
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

    private InputsController inputController;
    @Autowired
    public GlobalMoveController(HttpSession session, UserService userService, InputsController inputController){
        this.session = session;
        this.userService = userService;
        this.inputController = inputController;
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //private com.example.akb_teamD.app.repository.UserRepository UserRepository;


    @GetMapping("/")
    public String login() {
        return "global_login";
    }

@GetMapping("/user_diligence")
public String diligence(Model model){
        model.addAttribute("name","こんにちは "+session.getAttribute("name")+" さん");
        model.addAttribute("role",session.getAttribute("role"));

        return "user_diligence";
}
    @GetMapping("/user_attendanceList")
    public String view_attendance(Model model) {
        model.addAttribute("attendList",getUserService().readAttendance(getInputs().getDate()));
        model.addAttribute("role",session.getAttribute("role"));
        return "user_attendanceList";
    }

    @GetMapping("/user_place")
    public String viewPlace(Model model){
        model.addAttribute("role",session.getAttribute("role"));
        return "user_place";
    }

    //TODO ログインしているユーザーの情報から勤怠履歴を取得
    private static String past_month = null;
    private static String next_month = null;

    //月の変動処理
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
    ///////////////
    //今日の日付を取得
    public static void DetermineTheMonth(){
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
    }
    ///////////////////
    /////勤怠履歴///////
    @GetMapping("/user_disp_history")
    public String history(Model model) {
        //年月の初期値
        DetermineTheMonth();
        //formに月を送る
        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);
        model.addAttribute("role",session.getAttribute("role"));

        List<Map<String, Object>> attendences = this.getWorkAndRestTimeFrom(past_month, next_month);
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
        model.addAttribute("role",session.getAttribute("role"));

        List<Map<String, Object>> attendences = this.getWorkAndRestTimeFrom(past_month, next_month);
        model.addAttribute("fromJV_sel", attendences);

        return "user_disp_history";
    }
    //勤怠履歴,30日分取得
    private List<Map<String, Object>> getWorkAndRestTimeFrom(String past_month, String next_month) {
        String sql_sel = UserRepository.getUserDisplayHistorySql()+
                " WHERE '" + past_month + "/15' <= date AND date < '"+ next_month +
                "/15'AND id ="+ session.getAttribute("id");
        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        return attendences;
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
        model.addAttribute("role",session.getAttribute("role"));
        //postgres　月の総合労働時間の表示
        String sql_sel = UserRepository.getAdmDisplayTimesSql()
                + " WHERE '" + past_month + "/01' <= date AND date < '" + next_month +"/01'"
                + " GROUP BY id,name";
        List<Map<String, Object>> attendances = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendances);
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

        if ("検索".equals(action)&& !"'ShoWallUsers'".equals(search)) {
            search = "id";
        }
        //postgres 月の総合労働時間の表示
        String sql_sel = UserRepository.getAdmDisplayTimesSql()
                + " WHERE '" + past_month + "/01' <= date AND date < '" + next_month +"/01'"
                + "AND "+selectedPlace+"="+search
                + " GROUP BY id,name";

        List<Map<String, Object>> attendances = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendances);
        model.addAttribute("role",session.getAttribute("role"));
        return "adm_display_times";
    }
    ////////////////////////////
    ///運営用、選択した人の勤務時間集計一覧////
    @GetMapping("/adm_select_display_times")
    public String adm_select(Model model) {
        //今日の日付を取得
        DetermineTheMonth();
        //formに月を送る
        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);

        //postgres(user)
        String sql_user = "SELECT *FROM users_table";
        List<Map<String, Object>> usersList = this.jdbcTemplate.queryForList(sql_user);
        model.addAttribute("fromJV_user", usersList);

        return "adm_select_display_times";
    }
    @PostMapping("/adm_select_display_times")
    public String adm_slect_click(HttpServletRequest request, Model model,
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
        //postgres 選択したユーザーの日ごとの労働時間の詳細を1月分獲得する
        String sql_sel = UserRepository.getAdmSelectSql()
                + " FROM attendances_table WHERE '" + past_month + "/15' <= date AND date < '"+ next_month
                + "/15' AND "+selectedPlace+"="+search;;

        List<Map<String, Object>> attendances = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendances);
        return "adm_select_display_times";
    }
    ////////////////////////////



    /*-------多分ここからが自分（益山）の担当だと思う-------------*/


    @GetMapping ("/user_contact_address")
    public String address_get(Model model){
        model.addAttribute("role",session.getAttribute("role"));return "user_contact_address";
    }
    @PostMapping("/user_contact_address")
    public String address(HttpServletRequest request,
                          @RequestParam(name = "phone", required = false) String phone,
                          @RequestParam(name = "mail", required = false) String mail,
                          @RequestParam(name = "remark", required = false) String remark,
                          Model model) throws SQLException {

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
        model.addAttribute("role",session.getAttribute("role"));
        return "user_contact_address";

    }


    public UserService getUserService(){
        return userService;
    }

    public InputsController getInputs(){
        return inputController;
    }
}


