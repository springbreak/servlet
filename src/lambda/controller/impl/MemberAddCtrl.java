package lambda.controller.impl;

import java.util.Map;

import lambda.controller.Controller;
import lambda.dao.MemberDao;
import lambda.vo.Member;

public class MemberAddCtrl implements Controller {

  @Override
  public String execute(Map<String, Object> model) throws Exception {
    
    if (model.get("member") == null) {
      // case: GET
      return "/member/MemberForm.jsp";
    } else {
      // case: POST
      MemberDao md = (MemberDao) model.get("memberDao");
      Member member = (Member) model.get("member");
      
      md.insert(member);
      
      return "redirect:list.do";
    }
    
  }
}
