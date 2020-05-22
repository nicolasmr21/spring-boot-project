package com.computacion.taller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.computacion.taller.model.TsscAdmin;
import com.computacion.taller.repository.IAdminDao;

@Service
@Scope("singleton")
public class AdminService {

	@Autowired
	IAdminDao adminDao;
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(TsscAdmin admin) {
		adminDao.save(admin);
	}
	
}
