package lambda.controller.impl;

import java.util.Map;

import lambda.controller.Controller;
import lambda.dao.MemberDao;

public class MemberListCtrl implements Controller {

  @Override
  public String execute(Map<String, Object> model) throws Exception {
    
    // TODO Auto-generated method stub
    MemberDao md = (MemberDao)model.get("memberDao");
    model.put("members", md.selectList());
    
    return "/member/MemberList.jsp";
  }
}
