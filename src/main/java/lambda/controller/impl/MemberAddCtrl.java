package lambda.controller.impl;

import java.util.Map;

import lambda.bind.DataBinding;
import lambda.controller.Controller;
import lambda.dao.MemberDao;
import lambda.vo.Member;

public class MemberAddCtrl implements Controller, DataBinding{

  private MemberDao memberDao;
  
  public MemberAddCtrl setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
    return this;
  }

  @Override
  public String execute(Map<String, Object> model) throws Exception {
    
    Member member = (Member) model.get("member");
    
    if (member == null) {
      // case: GET
      return "/member/MemberForm.jsp";
    } else {
      // case: POST
      memberDao.insert(member);
      return "redirect:list.do";
    }
  }

  @Override
  public Object[] getDataPairs() {
    // TODO Auto-generated method stub
    return new Object[] { 
        "member", lambda.vo.Member.class
    };
  }
}
