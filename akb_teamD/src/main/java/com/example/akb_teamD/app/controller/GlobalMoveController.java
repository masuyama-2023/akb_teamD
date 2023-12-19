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
@Component
@Data
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
    private com.example.akb_teamD.app.repository.UserRepository UserRepository;


    @GetMapping("/")
    public String login() {
        return "global_login";
    }


    @GetMapping("/user_attendanceList")
    public String view_attendance(Model model) {
        model.addAttribute("attendList",getUserService().readAttendance());
        return "user_attendanceList";
    }

    /////勤怠履歴///////
    private static String past_month = null;
    private static String next_month = null;

    @GetMapping("/user_diligence")
    public String diligence(Model model){
        model.addAttribute("name","こんにちは "+session.getAttribute("name")+" さん");
        return "user_diligence";
    }

    //TODO ログインしているユーザーの情報から勤怠履歴を取得

    //表示する日付の関数

    @GetMapping("/user_disp_history")
    public String history(Model model) {


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
                "/15'"/*AND id ="+ID */;

        System.out.println(jdbcTemplate.queryForList(sql_sel));

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);
        //

        return "user_disp_history";
    }

    @PostMapping("/user_disp_history")
    public String history_click(HttpServletRequest request, HttpServletResponse response, Model model )
            throws ServletException, IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/[]M");

        YearMonth pastMonth_date = YearMonth.parse(past_month, formatter);
        YearMonth nextMonth_date = YearMonth.parse(next_month, formatter);

        // ボタンがクリックされたかどうかを判定
        String action = request.getParameter("action");

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
            System.out.println("エラー");
        }

        model.addAttribute("fromJV_left_month", past_month);
        model.addAttribute("fromJV_right_month", next_month);


        //postgres
        String sql_sel = "SELECT *,to_char(break_end - break_begin, 'HH24:MI:SS') AS break_sum," +
                "to_char((end_time - begin_time) - (break_end - break_begin), 'HH24:MI:SS') AS working" +
                " FROM attendances_table WHERE '" + past_month + "/15' <= date AND date < '"+ next_month +
                "/15' "/*AND id ="ID;*/ ;

        System.out.println(jdbcTemplate.queryForList(sql_sel));

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);

        return "user_disp_history";
    }
    //////////////////

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



    /*-------多分ここからが自分（益山）の担当だと思う-------------*/

/*
    @Value("${spring.datasource.url}")
    private static String database_test;
    @Component
    public static class DatabaseProperties {


        public static String getDatabase() {
            System.out.println(database_test);
            return database_test;
        }
    }
    */
@Component
public class DatabaseProperties {
    private static String database;  // 静的なフィールド
    public static String url;
    public static String username;
    public static String password;
    @Value("${spring.jpa.database}")
    public void setDatabase(String database) {
        DatabaseProperties.database = database;
    }
    @Value("${spring.datasource.url}")
    public void setDatabaseUrl(String url) {
        DatabaseProperties.url = url;
    }
    @Value("${spring.datasource.username}")
    public void setDatabaseUsername(String username) {
        DatabaseProperties.username = username;
    }
    @Value("${spring.datasource.password}")
    public void setDatabasePassword(String password) {
        DatabaseProperties.password = password;
    }

    public static String getDatabase() {
        return database;
    }
    public static String getUrl() {
        return url;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }
}

    @Service
    public static class DemoGetDataSource {

        private final DatabaseProperties databaseProperties;

        @Autowired
        public DemoGetDataSource(DatabaseProperties databaseProperties) {
            this.databaseProperties = databaseProperties;
        }
    }

    @GetMapping ("/user_contact_address")
    public String address_get(){
        return "user_contact_address";
    }
    @PostMapping("/user_contact_address")
    public String address(HttpServletRequest request,
                          @RequestParam(name = "phone", required = false) String phone,
                          @RequestParam(name = "mail", required = false) String mail,
                          @RequestParam(name = "remark", required = false) String remark,
                          Model model) throws SQLException {

        // 取得した内容をコンソールに表示
        String DatabaseName = DatabaseProperties.getDatabase();
        String url = DatabaseProperties.getUrl();
        String username = DatabaseProperties.getUsername();
        String password = DatabaseProperties.getPassword();

        String sql = "INSERT INTO address_table (id,name,phone,mail,other) VALUES(?,?,?,?,?)";
        String sql2 = "UPDATE address_table SET id = ?,name = ?,phone = ?,mail = ?,other = ?  WHERE id = ?";
        String sql3 = "SELECT name FROM address_table WHERE id = " +(int) session.getAttribute("id") ;
        try (Connection conn = DriverManager.getConnection(url, username, password)){


            List<Map<String, Object>> list = new ArrayList<>();
            list = jdbcTemplate.queryForList(sql3);
            if(list == null || list.size() == 0) {

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, (int) session.getAttribute("id"));
                    pstmt.setString(2, (String) session.getAttribute("name"));
                    pstmt.setString(3, phone);
                    pstmt.setString(4, mail);
                    pstmt.setString(5, remark);
                    pstmt.executeUpdate();

                }
            }else{
                try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setInt(1, (int) session.getAttribute("id"));
                    pstmt.setString(2, (String) session.getAttribute("name"));
                    pstmt.setString(3, phone);
                    pstmt.setString(4, mail);
                    pstmt.setString(5, remark);
                    pstmt.setInt(6, (int) session.getAttribute("id"));
                    pstmt.executeUpdate();
                }
            }
        }
        System.out.println("return直前");
        return "user_contact_address";

    }


    public UserService getUserService(){
        return userService;
    }

}
