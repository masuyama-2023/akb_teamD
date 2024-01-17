//確認済み(ok)
package com.example.akb_teamD.app.controller;
import com.example.akb_teamD.app.service.UserService;
import com.example.akb_teamD.app.controller.InputsController;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SessionController {

    private HttpSession session;

    private UserService userService;

    private InputsController inputController;

    @Autowired
    public SessionController(HttpSession session ,UserService userService, InputsController inputController) {
        this.session = session;
        this.userService = userService;
        this.inputController = inputController;
    }


    @PostMapping("login_check")
    public String loginCheck(@RequestParam("id") int id, @RequestParam("password")String pass, Model model){

        //データベースから情報取得完了済み
            session.setAttribute("id",id);

            if(userService.getName(id, pass).equals("No Name") || userService.getRole(id).equals("No Role")){
                model.addAttribute("errors","ログインIDまたはパスワードが誤っています");
                session.invalidate();
                return"global_login";
            }

            session.setAttribute("name",userService.getName(id,pass));
            session.setAttribute("role",userService.getRole(id));

            System.out.println(session.getAttribute("role"));

        String status = "状態：";
        if(userService.checkStatus((int)session.getAttribute("id"),inputController.getDate()).equals("No Record")){
            status = status + "未出勤";
        }
        else {
            status = status + userService.checkStatus((int)session.getAttribute("id"),inputController.getDate());
        }


            model.addAttribute("name","こんにちは "+session.getAttribute("name")+" さん");
            model.addAttribute("role",session.getAttribute("role"));

            model.addAttribute("status",status);
            return "user_diligence";
    }

    @GetMapping("logout_check")
    public String logoutCheck(){
        session.invalidate();
        return "global_login";
    }

    public UserService getUserService(){
        return userService;
    }


    public InputsController getInputs(){
        return inputController;
    }

}


