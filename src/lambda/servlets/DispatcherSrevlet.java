package lambda.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lambda.controller.Controller;
import lambda.controller.impl.LoginCtrl;
import lambda.controller.impl.LogoutCtrl;
import lambda.controller.impl.MemberAddCtrl;
import lambda.controller.impl.MemberDeleteCtrl;
import lambda.controller.impl.MemberListCtrl;
import lambda.controller.impl.MemberUpdateCtrl;
import lambda.vo.Member;

@WebServlet("*.do")
public class DispatcherSrevlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public DispatcherSrevlet() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    // TODO Auto-generated method stub

    res.setContentType("text/html; charset=UTF-8");
    String servicePath = req.getServletPath();
    Controller ctrl = null;
    HashMap<String, Object> model = new HashMap<String, Object>();

    try {
      // remove .do from request URL
      String ctrlPath = (servicePath.split("\\."))[0];
      
      // get memberDao from servlet context
      ServletContext sc = this.getServletContext();
      model.put("memberDao", sc.getAttribute("memberDao"));

      if ("/member/add".equals(ctrlPath)) {
        
        ctrl = new MemberAddCtrl();
        
        String passwd = req.getParameter("password");
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        
        if ( email != null && passwd != null && name != null) {
          Member mb = new Member();
          mb.setName(name);
          mb.setPassword(passwd);
          mb.setEmail(email);
          
          model.put("member", mb);
        }
        
      } else if ("/member/list".equals(ctrlPath)){
        ctrl = new MemberListCtrl();
      } else if ("/member/update".equals(ctrlPath)) {
        
        ctrl = new MemberUpdateCtrl();
        
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        int id = Integer.parseInt(req.getParameter("no")); 
        
        if (email != null && name != null) {
          // case: POST
          Member mb = new Member();
          mb.setNo(id);
          mb.setName(name);
          mb.setEmail(email);
          
          model.put("member", mb);
        } else {
          model.put("id", id);
        }
        
        
      } else if ("/member/delete".equals(ctrlPath)){
        ctrl = new MemberDeleteCtrl();
        
        // get id to be removed
        int id = Integer.parseInt(req.getParameter("no")); 
        model.put("id", id);
      } else if ("/auth/login".equals(ctrlPath)) {
        ctrl = new LoginCtrl();
        
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        model.put("session", req.getSession());
          
        if (email != null && password != null) {
          model.put("email", email);
          model.put("password", password);
        }
      } else if ("/auth/logout".equals(ctrlPath)) {
        ctrl = new LogoutCtrl();
        model.put("session", req.getSession());
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
      // TODO: handle exception
      e.printStackTrace();
      req.setAttribute("error", e);
      RequestDispatcher rd = req.getRequestDispatcher("/Error.jsp");
      rd.forward(req, res);
    }
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
