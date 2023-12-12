package com.example.akb_teamD.app.service;

import com.example.akb_teamD.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void insertWorkStart(int id,String name,String time){
        getUserRepository().workStart(id,name,time);
    }

    public void insertAddress(){
        getUserRepository().address();
    }

    public void insertAdmAdd(){
        getUserRepository().adm_add();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
     *                                   UPDATE文                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void updateBreakStart(int id,String date,String time){
        getUserRepository().breakStart(id,date,time);
    }

    public void updateBreakEnd(){
        getUserRepository().breakEnd();
    }

    public void updateWorkEnd() {
        getUserRepository().WorkEnd();
    }

    public void updatePlace() {
        getUserRepository().place();
    }

    public void updateUserEdit(){
        getUserRepository().userEdit();
    }

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

    public UserRepository getUserRepository(){
        return userRepository;
    }

}
