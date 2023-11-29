package com.example.akb_teamD.app.repository;



/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:SQL全般を司る
  = = = = = = = = = = = = = = = = = = = = = =*/


import com.example.akb_teamD.app.controller.GlobalMoveController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Controller
public class UserRepository extends HttpServlet {

    private com.example.akb_teamD.app.controller.GlobalMoveController GlobalMoveController;
    private static boolean hasProcessed = false;
    private static String past_month = null;
    private static String next_month = null;
    private static String ID;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/user_disp_history")
    public String history_click(HttpServletRequest request, HttpServletResponse response, Model model )
            throws ServletException, IOException {



        if (!hasProcessed) {
            ID = getID();
            past_month = GlobalMoveController.getPastMonth();
            next_month = GlobalMoveController.getNextMonth();
            hasProcessed = true;
        }

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
                "/15' AND id ="+ID ;

        System.out.println(jdbcTemplate.queryForList(sql_sel));

        List<Map<String, Object>> attendences = this.jdbcTemplate.queryForList(sql_sel);
        model.addAttribute("fromJV_sel", attendences);

        return "user_disp_history";
    }

    public static String getID() {
        return ID;
    }
}
