package lambda.controller.impl;

import java.util.HashMap;
import java.util.Map;

import lambda.bind.DataBinding;
import lambda.controller.Controller;
import lambda.dao.MemberDao;

public class MemberListCtrl implements Controller, DataBinding {

	private MemberDao memberDao;

	public MemberListCtrl setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {

		HashMap<String, Object> paramMap = 
				new HashMap<String, Object>();
		
		paramMap.put("orderCond", model.get("orderCond"));
		model.put("members", memberDao.selectList(paramMap));
		return "/member/MemberList.jsp";
	}

	@Override
	public Object[] getDataPairs() {
		// TODO Auto-generated method stub
		return new Object[] { "orderCond", String.class };
	}
}
