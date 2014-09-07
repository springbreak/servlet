package lambda.controller.impl;

import java.util.Map;
import javax.servlet.ServletException;

import lambda.controller.Controller;
import lambda.dao.MemberDao;
import lambda.vo.Member;

public class MemberUpdateCtrl implements Controller {

  @Override
  public String execute(Map<String, Object> model) throws Exception {

    try {
      MemberDao md = (MemberDao) model.get("memberDao");

      if (model.get("member") == null) {
        // case: GET
        int id = (int) model.get("id");
        model.put("member", md.selectOne(id));
        return "/member/MemberUpdateForm.jsp";
      } else {
        // case: POST
        Member mb = (Member) model.get("member");
        md.update(mb);
        return "redirect:list.do";
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
    
  }
}
