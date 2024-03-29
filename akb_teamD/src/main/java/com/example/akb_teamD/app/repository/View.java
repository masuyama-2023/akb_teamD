package com.example.akb_teamD.app.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface View {
    List<Map<String, Object>> findAttendance(LocalDate date); //本日の出退勤者一覧

    /*直打ちを確認したため、後日編集*/
    List<Map<String, Object>> findDisplayTimes(); //(管理者)ユーザーの合計出勤時刻の表示
    List<Map<String, Object>> findUserList(); //(管理者)ユーザー一覧表示

    /*直打ちを確認したため、後日編集*/
    List<Map<String, Object>> findSelectTimes(); //(管理者)選択した人物の勤怠履歴を表示
    /*直打ちを確認したため、後日編集*/
    List<Map<String, Object>> findHistory(); //自分の1ヶ月単位の出退勤を確認

    String loginCheck(int id, String pass);
    String getRole(int id);
    List<Map<String, Object>> findRecord(int id);

    String checkAttendRecord(int id, LocalDate date);
    String checkStatus(int id, LocalDate date);
    String checkBreak(int id, LocalDate date);
    List<Map<String,Object>> getUserById(int id);

}


