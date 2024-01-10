package com.example.akb_teamD.app.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface View {
    List<Map<String, Object>> findAttendance(); //本日の出退勤者一覧


    List<Map<String, Object>> findDisplayTimes(); //(管理者)ユーザーの合計出勤時刻の表示
    List<Map<String, Object>> findUserList(); //(管理者)ユーザー一覧表示
    List<Map<String, Object>> findSelectTimes(); //(管理者)選択した人物の勤怠履歴を表示
    List<Map<String, Object>> findHistory(); //自分の1ヶ月単位の出退勤を確認

    String loginCheck(int id, String pass);
    String getRole(int id);
    List<Map<String, Object>> findRecord(int id);

}


