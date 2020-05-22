package com.computacion.taller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.repository.ITimecontrolDao;
import com.computacion.taller.repository.TimecontrolDao;

@Service
@Scope("singleton")
public class TimecontrolService {

	ITimecontrolDao timecontrolDao;

	@Autowired
	public TimecontrolService(TimecontrolDao timecontrolDao) {
		super();
		this.timecontrolDao = timecontrolDao;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(TsscTimecontrol t) {
		timecontrolDao.save(t);
	}
	
	
	
}
