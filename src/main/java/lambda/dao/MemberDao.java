package lambda.dao;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import lambda.vo.Member;


public interface MemberDao {
  
  public List<Member> selectList(HashMap<String, Object> paramMap) throws Exception;
  public int insert(Member member) throws Exception;
  public int delete(int id) throws Exception;
  public Member selectOne(int id) throws Exception;
  public int update(Member member) throws Exception; 
  public Member exist(String email, String password) throws Exception;
}
