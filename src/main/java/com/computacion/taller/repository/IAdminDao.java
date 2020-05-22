package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;

import com.computacion.taller.model.TsscAdmin;


public interface IAdminDao {

	public void save(TsscAdmin entity);
	public void update(TsscAdmin entity);
	public void delete(TsscAdmin entity);
	public Optional<TsscAdmin> findById(long codigo);
	public List<TsscAdmin> findAll();
	
}
