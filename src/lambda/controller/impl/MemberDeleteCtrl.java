package lambda.controller.impl;

import java.util.Map;

import javax.servlet.ServletException;

import lambda.bind.DataBinding;
import lambda.controller.Controller;
import lambda.dao.MemberDao;
public class MemberDeleteCtrl implements Controller, DataBinding{
  
  private MemberDao memberDao;
  
  public MemberDeleteCtrl setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
    return this;
  }

  @Override
  public String execute(Map<String, Object> model) throws Exception {
   
    try {
      int id = (int) model.get("id");
      
      memberDao.delete(id);
      
      return "redirect:list.do";
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  public Object[] getDataPairs() {
    
    
    return new Object[] {
        "id", Integer.class
    };
  }

}
