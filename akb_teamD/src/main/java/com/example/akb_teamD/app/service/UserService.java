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

    public void updateBreakEnd(int id, LocalDate date, LocalTime time){
        getUserRepository().breakEnd(id, date, time);
    }

    public void updateWorkEnd(int id, LocalDate date, LocalTime time) {
        getUserRepository().WorkEnd(id, date, time);
    }

    public void place(int id,String place,LocalDate date) {
        getUserRepository().place(id, place, date);
    }

    public void updateAddress(int id,String name,String phone,String mail,String remark){
        getUserRepository().updateAddress(id, name,phone,mail ,remark);

    }
    public void updateUserEdit( int no,String name,int id,String pass){
        getUserRepository().userEdit(no, name,id,pass);
    }
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   DELETE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void userDelete(int id){
        getUserRepository().userDelete(id);
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
    *                                   SELECT文                              *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    //当日の勤退者の一覧表示
    public List<Map<String, Object>> readAttendance(LocalDate date){
        return getUserRepository().findAttendance(date);
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

    public String checkAttend(int id, LocalDate date){
        return getUserRepository().checkAttendRecord(id,date);
    }

    public String checkStatus(int id, LocalDate date){
        return getUserRepository().checkStatus(id,date);
    }
    public String checkBreak(int id, LocalDate date){
        return getUserRepository().checkBreak(id,date);
    }

    public UserRepository getUserRepository(){
        return userRepository;
    }
    public int getUserNo(int id){
        return getUserRepository().getUserNo(id);
    }

    public List<Map<String,Object>> getUserById(int id){
        return getUserRepository().getUserById(id);
    }

}
