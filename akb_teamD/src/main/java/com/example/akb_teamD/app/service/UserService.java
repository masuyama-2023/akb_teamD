package com.example.akb_teamD.app.service;

import com.example.akb_teamD.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   INSERT文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    public void insertWorkStart(int id, String name, LocalTime time , LocalDate date){
        getUserRepository().workStart(id,name,time,date);
    }

    public void insertAddress(int id, String name,String phone,String mail,String remark){
        getUserRepository().address(id,name,phone,mail,remark);
    }

    public void insertAdmAdd(){
        getUserRepository().adm_add();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   UPDATE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void updateBreakStart(int id, LocalDate date, LocalTime time){
        getUserRepository().breakStart(id, date, time);
    }

    public void updateBreakEnd(){
        getUserRepository().breakEnd();
    }

    public void updateWorkEnd() {
        getUserRepository().WorkEnd();
    }

    public void place(int id,String place,LocalDate date) {
        getUserRepository().place(id, place, date);
    }

    public void updateAddress(int id,String name,String phone,String mail,String remark){
        getUserRepository().updateAddress(id, name,phone,mail ,remark);

    }
    public void updateUserEdit()  {getUserRepository().userEdit();}

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   DELETE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void userDelete(){
        getUserRepository().userDelete();
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
    *                                   SELECT文                              *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    //当日の勤退者の一覧表示
    public List<Map<String, Object>> readAttendance(){
        return getUserRepository().findAttendance();
    }

    public List<Map<String,Object>> readDisplayTimes(){
        return getUserRepository().findDisplayTimes();
    }

    public List<Map<String,Object>> readUserList(){
        return getUserRepository().findUserList();
    }
    public List<Map<String,Object>> readSelectTimes(){
        return getUserRepository().findSelectTimes();
    }
    public List<Map<String,Object>> readHistory(){
        return getUserRepository().findHistory();
    }

    public String getName(int id, String pass){
        return getUserRepository().loginCheck(id,pass);
    }

    public String getRole(int id){
        return getUserRepository().getRole(id);
    }

    public List<Map<String, Object>> findRecord(int id){
        return getUserRepository().findRecord(id);
    }





    public UserRepository getUserRepository(){
        return userRepository;
    }



}
