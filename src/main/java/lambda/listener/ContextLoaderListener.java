package lambda.listener;

// 서버에서 제공하는 DataSource 사용하기
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lambda.context.ApplicationContext;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
	static ApplicationContext ac;

	public static ApplicationContext getApplicationContext() {
		return ac;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ac = new ApplicationContext();

		try {
			ServletContext sc = event.getServletContext();
			
			String mybatisConfigPath = "mybatis/mybatis-config.xml";
			InputStream is = Resources.getResourceAsStream(mybatisConfigPath);
			SqlSessionFactory sqlSessionFactory = 
					new SqlSessionFactoryBuilder().build(is);
			
			ac.addBean("sqlSessionFactory", sqlSessionFactory);

			// get the path of 'application.properties' in web.xml
			String propsPath = sc.getRealPath(sc
					.getInitParameter("ApplicationPropertyPath"));
			
			ac.createPropsInst(propsPath);
			ac.createAnnotatedInst("");
			ac.injectDependency();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
