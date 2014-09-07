package lambda.controller.impl;

import java.util.Map;

import javax.servlet.ServletException;

import lambda.controller.Controller;
import lambda.dao.MemberDao;

public class MemberDeleteCtrl implements Controller {

  @Override
  public String execute(Map<String, Object> model) throws Exception {
   
    try {
      MemberDao md = (MemberDao) model.get("memberDao");
      int id = (int) model.get("id");
      
      md.delete(id);
      
      return "redirect:list.do";
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

}
