package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;

import com.computacion.taller.model.TsscGame;


public interface IGameDao {

	public TsscGame save(TsscGame entity);
	public TsscGame update(TsscGame entity);
	public void delete(TsscGame entity);
	public Optional<TsscGame> findById(long codigo);
	public List<TsscGame> findAll();
	
}
