package lambda.controller.impl;

import java.util.Map;

import javax.servlet.ServletException;

import lambda.bind.DataBinding;
import lambda.controller.Controller;
import lambda.dao.MemberDao;
import lambda.vo.Member;

public class MemberUpdateCtrl implements Controller, DataBinding{

  private MemberDao memberDao;
  
  public MemberUpdateCtrl setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
    return this;
  }

  @Override
  public String execute(Map<String, Object> model) throws Exception {

    Member member = (Member) model.get("member");
    try {
      if (member.getEmail() == null &&
          member.getName() == null) {
        // case: GET
        int id = (int) model.get("id");
        model.put("member", memberDao.selectOne(id));
        return "/member/MemberUpdateForm.jsp";
      } else {
        // case: POST
        memberDao.update(member);
        return "redirect:list.do";
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
    
  }

  @Override
  public Object[] getDataPairs() {
    // TODO Auto-
    return new Object[] {
       "member", lambda.vo.Member.class,
       "id", Integer.class
    };
  }
}
