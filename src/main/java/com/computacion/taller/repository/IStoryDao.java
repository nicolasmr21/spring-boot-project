package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;

import com.computacion.taller.model.TsscStory;


public interface IStoryDao {

	public TsscStory save(TsscStory entity);
	public TsscStory update(TsscStory entity);
	public void delete(TsscStory entity);
	public Optional<TsscStory> findById(long codigo);
	public List<TsscStory> findAll();
	
}
