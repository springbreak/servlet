package lambda.controller;

import java.util.Map;

import lambda.dao.impl.MemberDaoJdbc;

public interface Controller {
  
  String execute(Map<String, Object> model) throws Exception;
}
