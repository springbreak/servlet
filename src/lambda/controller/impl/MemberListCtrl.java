package lambda.controller.impl;

import java.util.Map;

import lambda.controller.Controller;
import lambda.dao.MemberDao;

public class MemberListCtrl implements Controller {
  
  private MemberDao memberDao;
  
  public MemberListCtrl setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
    return this;
  }
  
  @Override
  public String execute(Map<String, Object> model) throws Exception {
    
    model.put("members", memberDao.selectList());
    return "/member/MemberList.jsp";
  }
}
