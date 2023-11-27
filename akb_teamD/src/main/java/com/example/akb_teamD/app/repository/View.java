package com.example.akb_teamD.app.repository;

public interface View {
    void findAttendance(); //本日の出退勤者一覧
    void findDisplayTimes(); //(管理者)ユーザーの合計出勤時刻の表示
    void findUserList(); //(管理者)ユーザー一覧表示
    void findSelectTimes(); //(管理者)選択した人物の勤怠履歴を表示
    void findHistory(); //自分の1ヶ月単位の出退勤を確認
}
