package com.example.akb_teamD.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.SQLException;



import java.util.List;
import java.util.Map;

/* = = = = = = = = = = = = = = = = = = = = = =
    TODO メモ(見やすくするためにTODO機能を利用)
        ファイル概要:ユーザー用フォームの遷移を管理 & 全体で使うクラスの定義を行う
        Post    :フォームで入力された情報を基に操作
        Get     :URLに直接記載または未入力の場合動作
  = = = = = = = = = = = = = = = = = = = = = =*/


@RequestMapping("")
@Controller
public class GlobalMoveController
{
    // データベースのURL
    private static final String URL = "jdbc:postgresql://localhost:5432/springboot";

    // データベースにアクセスするユーザー
    private static final String USER = "postgres";

    // データベースにアクセスするユーザーのパスワード
    private static final String PASSWORD = "postgres";

    /**
     * データベースの接続を取得する
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;




    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/user_attendanceList")
    public String view_attendance() {
        return "user_attendanceList";
    }

    @GetMapping("/user_diligence")
    public String diligence(){
        return "user_diligence";
    }

    //TODO ログインしているユーザーの情報から勤怠履歴を取得
    @GetMapping("/user_disp_history")
    public String history() {
        return "user_disp_history";
    }

    @GetMapping("/user_place")
    public String place() {
        return "user_place";
    }


    @GetMapping("/user_contact_address")
    public String address() throws ClassNotFoundException, SQLException{
        //ここの　String sql = 以下の文をちゃんと書ければ動くはず？現状は仮置き。
        //idとnameは現在ログインしてるユーザー情報を取得。残りの３つは入力欄に入力した内容を入れる。
        // ？？？は全部仮置き。当然だが仮置きなので動かない。
        //String sql = "INSERT INTO address_table (id,name,phone,mail,other) VALUES (？？？,'？？？','？？？','？？？','？？？')";
        //jdbcTemplate.update(sql);

        int result = 0;


        String sql = "INSERT INTO address_table (id,name,phone,mail,other) VALUES(?, ?, ?, ?, ?);";
        //String sql = "INSERT INTO address_table (id,name,phone,mail,other) VALUES(2,'test1','a','b','c');";

/*

        String phone = request.getParameter("phone");
        String mail = Integer.parseInt(request.getParameter("mail"));
        String other = Integer.parseInt(request.getParameter("remark"));
*/

        try (Connection con = getConnection();PreparedStatement pstmt = con.prepareStatement(sql);) {
            // パラメータの設定
/*
            pstmt.setInt(1, 1);
            pstmt.setString(2,"test1");
            pstmt.setString(3, phone);
            pstmt.setString(4, mail);
            pstmt.setString(5, other);
*/


            //全然うまくいかないのでベタ打ちテスト
            //データベースにデータが入るのにページが開けない謎。
            pstmt.setInt(1, 3);
            pstmt.setString(2,"test3");
            pstmt.setString(3, "000-0000-0001");
            pstmt.setString(4, "aiueo@gmail.com");
            pstmt.setString(5, "あいうえお");

            // SQLの実行
            result = pstmt.executeUpdate();

        }


        jdbcTemplate.update(sql);
        return "user_contact_address";
    }

}
