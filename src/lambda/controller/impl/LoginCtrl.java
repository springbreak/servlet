package lambda.controller.impl;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import lambda.controller.Controller;
import lambda.dao.MemberDao;
import lambda.vo.Member;

public class LoginCtrl implements Controller {

  @Override
  public String execute(Map<String, Object> model) throws Exception {
    // TODO Auto-generated method stub
    
    try {
      
      HttpSession session = (HttpSession) model.get("session");
      if (session.getAttribute("member") != null) {
        // case: already logged in
        return "redirect:../member/list.do";
      }
      
      if (model.get("email") == null || model.get("password") == null) {
        // case: GET
        return "/auth/LoginForm.jsp";
      } 

      // case: POST
      
      
      MemberDao md = (MemberDao) model.get("memberDao");
      String email = (String) model.get("email");
      String password = (String) model.get("password");
      
      Member mb = md.exist(email, password);
      
      if (mb == null) {
        return "auth/LoginFail.jsp";
      }
     
      session.setAttribute("member", mb);
      return "redirect:../member/list.do";
      
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
