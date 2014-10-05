package lambda.dao.impl;

import java.util.HashMap;
import java.util.List;

import lambda.annotation.Bean;
import lambda.dao.MemberDao;
import lambda.vo.Member;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@Bean(value = "memberDao")
public class MemberDaoJdbc implements MemberDao {
	
	SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public List<Member> selectList(HashMap<String, Object> paramMap) throws Exception {
		SqlSession ss = sqlSessionFactory.openSession();
		
		try {
			return ss.selectList("lambda.dao.MemberDao.selectList", paramMap);
		} finally {
			ss.close();
		}
	}

	public Member selectOne(int id) throws Exception {
		
		SqlSession ss = sqlSessionFactory.openSession();
		
		try {
			return ss.selectOne("lambda.dao.MemberDao.selectOne", id);
		} finally {
			ss.close();
		}
	}

	public int insert(Member member) throws Exception {
		SqlSession ss = sqlSessionFactory.openSession();
				
		try	{
			int count = ss.insert("lambda.dao.MemberDao.insert", member);
			ss.commit();
			return count;
		} finally {
			ss.close();
		}
	}

	public int delete(int id) throws Exception {
		SqlSession ss = sqlSessionFactory.openSession();
		
		try {
			int count = ss.delete("lambda.dao.MemberDao.delete", id);
			ss.commit();
			return count;
		} finally {
			ss.close();
		}
	}

	public int update(Member member) throws Exception {
		SqlSession ss = sqlSessionFactory.openSession();
		
		try {
			int count = ss.update("lambda.dao.MemberDao.update", member);
			ss.commit();
			return count;
		} finally {
			ss.close();
		}
	}

	public Member exist(String email, String password) throws Exception {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);		
		
		SqlSession ss = sqlSessionFactory.openSession();
		
		try {
			return ss.selectOne("lambda.dao.MemberDao.exist", map);
		} finally {
			ss.close();
		}
	}
}