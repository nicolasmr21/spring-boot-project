package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;

import com.computacion.taller.model.TsscTimecontrol;


public interface ITimecontrolDao {

	public void save(TsscTimecontrol entity);
	public void update(TsscTimecontrol entity);
	public void delete(TsscTimecontrol entity);
	public Optional<TsscTimecontrol> findById(long codigo);
	public List<TsscTimecontrol> findAll();
	
}
