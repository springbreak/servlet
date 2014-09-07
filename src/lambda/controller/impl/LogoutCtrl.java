package lambda.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import lambda.controller.Controller;

public class LogoutCtrl implements Controller {

  @Override
  public String execute(Map<String, Object> model) throws Exception {
    // TODO Auto-generated method stub
    
    HttpSession session = (HttpSession) model.get("session");
    session.invalidate();
    
    return "redirect:login.do";
  }

}
