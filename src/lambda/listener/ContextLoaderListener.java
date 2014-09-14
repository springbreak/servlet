package lambda.listener;

// 서버에서 제공하는 DataSource 사용하기
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import lambda.controller.impl.LoginCtrl;
import lambda.controller.impl.LogoutCtrl;
import lambda.controller.impl.MemberAddCtrl;
import lambda.controller.impl.MemberDeleteCtrl;
import lambda.controller.impl.MemberListCtrl;
import lambda.controller.impl.MemberUpdateCtrl;
import lambda.dao.MemberDao;
import lambda.dao.impl.MemberDaoJdbc;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      ServletContext sc = event.getServletContext();
      
      InitialContext initialContext = new InitialContext();
      DataSource ds = (DataSource)initialContext.lookup(
          "java:comp/env/jdbc/studydb");
      
      MemberDao memberDao = new MemberDaoJdbc();
      memberDao.setDataSource(ds);
    
      sc.setAttribute("/auth/login", new LoginCtrl().setMemberDao(memberDao));
      sc.setAttribute("/auth/logout", new LogoutCtrl());
      sc.setAttribute("/member/add", new MemberAddCtrl().setMemberDao(memberDao));
      sc.setAttribute("/member/delete", new MemberDeleteCtrl().setMemberDao(memberDao));
      sc.setAttribute("/member/list", new MemberListCtrl().setMemberDao(memberDao));
      sc.setAttribute("/member/update", new MemberUpdateCtrl().setMemberDao(memberDao));
      
    } catch(Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {}
}
