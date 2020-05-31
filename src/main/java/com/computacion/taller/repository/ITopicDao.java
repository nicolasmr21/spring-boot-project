package com.computacion.taller.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.computacion.taller.model.TsscTopic;


public interface ITopicDao {

	public TsscTopic save(TsscTopic entity);
	public TsscTopic update(TsscTopic entity);
	public void delete(TsscTopic entity);
	public Optional<TsscTopic> findById(long codigo);
	public List<TsscTopic> findAll();
	public List<TsscTopic> findTopicWithGameAmountByDate(LocalDate date);
	
}
