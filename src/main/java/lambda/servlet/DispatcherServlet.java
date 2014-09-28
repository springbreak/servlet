package lambda.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lambda.bind.DataBinding;
import lambda.context.ApplicationContext;
import lambda.controller.Controller;
import lambda.listener.ContextLoaderListener;

@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public DispatcherServlet() {
    super();
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    res.setContentType("text/html; charset=UTF-8");
    String servicePath = req.getServletPath();
    Controller ctrl = null;

    try {
      // remove .do from request URL
      String ctrlPath = (servicePath.split("\\."))[0];

      // set session 
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("session", req.getSession());
      
      // get beans from ApplicationContext
      ApplicationContext ac = ContextLoaderListener.getApplicationContext();
      ctrl = (Controller) ac.getBean(ctrlPath);
      
      if (ctrl == null) {
    	  throw new Exception("Can't find bean :" + ctrlPath);
      }
      
      if (ctrl instanceof DataBinding) {
        bindRequiredDataPairs((DataBinding)ctrl, model, req);
      }
      
      String view = ctrl.execute(model);
      
      // iterate model and setAttribute to render
      
      for(String key: model.keySet()) {
        req.setAttribute(key, model.get(key));
      }
      
      if(view.startsWith("redirect:")) {
        // handle redirect
        res.sendRedirect(view.substring(9));
        return;
      } else {
        RequestDispatcher rd = req.getRequestDispatcher(view);
        rd.include(req, res);
      }

    } catch (Exception e) {
      e.printStackTrace();
      req.setAttribute("error", e);
      RequestDispatcher rd = req.getRequestDispatcher("/Error.jsp");
      rd.forward(req, res);
    }
  }
  
  /*
   * Make requestParams dynamically 
   */
  private void bindRequiredDataPairs(
      DataBinding db, 
      HashMap<String, Object> model,
      HttpServletRequest req) throws Exception {
    
    Object[] requiredObjPairs = db.getDataPairs(); 
    
    if ( requiredObjPairs.length % 2 != 0) {
      throw new RuntimeException("getDataPairs should return object paris");
    }
    
    for (int i = 0; i < requiredObjPairs.length; i += 2) {
      String requiredObjName = (String) requiredObjPairs[i];
      Class<?> requiredObjType = (Class<?>) requiredObjPairs[i+1];
      Object obj = createRequiredObj(req, requiredObjName, requiredObjType);
      
      if (obj != null) model.put(requiredObjName, obj);
    }
  }
  
  private Object createRequiredObj(HttpServletRequest req, String objName,
      Class<?> objType) throws Exception {
    // case: Primitive
    
    if (isPrimitive(objType)) {
      return createObject(objType, req.getParameter(objName));
    }
    
    // case: Non-primitive
    Set<String> reqParamKeys = req.getParameterMap().keySet();
    Object requiredObj = objType.newInstance();
    Method setter = null;
    
    // case: No request parameters 
    if (reqParamKeys.size() == 0) return null; 
    
    for(String paramKey : reqParamKeys) {
      
      // get setter method
      setter = findSetter(objType, paramKey);
      
      if (setter != null) {
        // create a setter argument dynamically 
        Class<?> setterArgType = setter.getParameterTypes()[0];
        String setterArgValue = req.getParameter(paramKey);
        Object setterArg = createObject(setterArgType, setterArgValue); 

        // invoke setter
        setter.invoke(requiredObj, setterArg);
      }
    }
    
    return requiredObj;
  }
  
  private Method findSetter(Class<?> objType, String paramKey) {
    
    Method[] methods = objType.getMethods();
    
    paramKey = paramKey.toLowerCase();
    
    for(Method method : methods) {
      if (method.getName().startsWith("set") && 
          method.getName().substring(3).toLowerCase().equals(paramKey)) {
        return method;
      }
    }
    
    return null;
  }

  private Object createObject(Class<?> objType, String value) {
   
    if (objType == Integer.class || objType.getName().equals("int")) 
      return new Integer(value);
    else if (objType == Long.class || objType.getName().equals("long")) 
      return new Long(value);
    else if (objType == Float.class || objType.getName().equals("float")) 
      return new Float(value);
    else if (objType == Double.class || objType.getName().equals("double")) 
      return new Double(value);
    else if (objType == Boolean.class || objType.getName().equals("boolean"))
      return new Boolean(value);
    else if (objType == Date.class) return java.sql.Date.valueOf(value);
    else return value; // case: String
  }

  private boolean isPrimitive(Class<?> type) {
    
    if (type == Integer.class || type.getName().equals("int") ||
        type == Long.class || type.getName().equals("long") ||
        type == Float.class || type.getName().equals("float") ||
        type == Double.class || type.getName().equals("double") ||
        type == Boolean.class || type.getName().equals("boolean") ||
        type == Date.class || 
        type == String.class) { 
      
      return true;
    }
    
    return false;
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

}
