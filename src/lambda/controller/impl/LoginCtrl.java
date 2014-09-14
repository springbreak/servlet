package lambda.controller.impl;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import lambda.bind.DataBinding;
import lambda.controller.Controller;
import lambda.dao.MemberDao;
import lambda.vo.Member;


public class LoginCtrl implements Controller, DataBinding{

  private MemberDao memberDao;
  
  public LoginCtrl setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
    return this;
  }
  
  @Override
  public String execute(Map<String, Object> model) throws Exception {
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
      String email = (String) model.get("email");
      String password = (String) model.get("password");
      
      Member mb = memberDao.exist(email, password);
      
      if (mb == null) {
        return "auth/LoginFail.jsp";
      }
     
      session.setAttribute("member", mb);
      return "redirect:../member/list.do";
      
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  public Object[] getDataPairs() {
    
    return new Object[] {
        "email", String.class,
        "password", String.class
    };
  }
}
