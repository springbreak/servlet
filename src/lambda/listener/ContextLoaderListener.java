package lambda.listener;

// 서버에서 제공하는 DataSource 사용하기
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import lambda.context.ApplicationContext;
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
	static ApplicationContext ac;
	
	public static ApplicationContext getApplicationContext() {
		return ac;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();

			InitialContext initialContext = new InitialContext();
			// get the path of 'application.properties' in web.xml
			String propsPath = sc.getRealPath(sc.getInitParameter("ApplicationPropertyPath"));
			ac = new ApplicationContext(propsPath);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
