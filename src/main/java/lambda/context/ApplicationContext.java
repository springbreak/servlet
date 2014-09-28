package lambda.context;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import lambda.annotation.Bean;

import org.reflections.Reflections;

// the purpose of this class is not to modify ContextLoaderListener 
// when DAOs or controllers added
public class ApplicationContext {

	// Hashtable is synchronized while Hashmap isn't
	Hashtable<String, Object> objTable;
	
	public ApplicationContext() {
		objTable = new Hashtable<String, Object>();
	}
	
	public Object getBean(String beanName) {
		return objTable.get(beanName);
	}
	
	public void addBean(String beanName, Object bean) {
		objTable.put(beanName, bean);
	}
	
	public void createAnnotatedInst(String beanPackages) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Reflections reflector = new Reflections(beanPackages);
		// get all annotated classes while searching src including sub-dir
		Set<Class<?>> annotatedClasses = reflector.getTypesAnnotatedWith(Bean.class);
		String key = null;
		
		for(Class<?> cls : annotatedClasses) {
			// get the value of @Bean annotation 
			key = cls.getAnnotation(Bean.class).value();
			objTable.put(key, cls.newInstance());
		}
	}

	public void createPropsInst(String propsPath) throws Exception {
		// We can't create resources provided by Tomcat 
		// e.g jndi.dataSource = java:comp/env/jdbc/studydb
		// The acronym JNDI stands for Java Naming and Directory Interface
		Properties props = new Properties();
		props.load(new FileReader(propsPath));
		
		// To get the resources, we will use jaxax.naming.Context
		Context context = new InitialContext();
		String key = null, value = null;
		
		for(Object propsKey : props.keySet()) {
			key = (String) propsKey;
			value = props.getProperty(key);
			
			if (key.startsWith("jndi.")) {
				// get a tomcat provided resource and put it to the object table
				objTable.put(key, context.lookup(value));
			} else {
				// create a resource and put it to the object table
				objTable.put(key, Class.forName(value).newInstance());
			}
		}
	}

	public void injectDependency() throws Exception {
		String key = null;
		Object value = null;
		
		for(Object tableKey : objTable.keySet()) {
			key = (String) tableKey;
			
			if(!key.startsWith("jndi.")) {
				callSetter(objTable.get(key));
			}
		}
	}

	private void callSetter(Object inst) throws Exception {
		Object setterArg = null;
		for(Method m : inst.getClass().getMethods()) {
			if (m.getName().startsWith("set")) {
				// setters have only one parameter 
				setterArg = findInstByType(m.getParameterTypes()[0]);
				
				if(setterArg != null) {
					m.invoke(inst, setterArg);
				}
			}
		}
	}
	
	private Object findInstByType(Class<?> setterArgType) {
		for(Object obj : objTable.values()) {
			if (setterArgType.isInstance(obj)) {
				return obj;
			}
		}
		
		return null;
	}
}
