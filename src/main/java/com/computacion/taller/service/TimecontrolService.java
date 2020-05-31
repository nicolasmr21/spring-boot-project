package com.computacion.taller.service;

import java.util.Optional;

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
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscTimecontrol findById(long id) {
		Optional<TsscTimecontrol> t = timecontrolDao.findById(id);
		if(t.isPresent()) {
			return t.get();
		}
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscTimecontrol save(TsscTimecontrol t) {
		timecontrolDao.save(t);
		return t;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscTimecontrol edit(TsscTimecontrol t) {
		
		if(t==null)
			return null;
		timecontrolDao.update(t);
			
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(TsscTimecontrol t) {
		System.out.println(t.getTsscGame().getTsscTimecontrols().size());
		timecontrolDao.delete(t);
		System.out.println(t.getTsscGame().getTsscTimecontrols().size());
	}
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Iterable<TsscTimecontrol> findAll() {
		return timecontrolDao.findAll();
	}
	
	
	
}
