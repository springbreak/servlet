package lambda.listener;

// 서버에서 제공하는 DataSource 사용하기
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lambda.context.ApplicationContext;

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
