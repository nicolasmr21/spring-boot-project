package com.computacion.taller.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscTopic;

@Repository
@Scope("singleton")
public class GameDao implements IGameDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public TsscGame save(TsscGame entity) {
		entityManager.persist(entity);	
		return entity;	
	}

	@Override
	public TsscGame update(TsscGame entity) {
		entityManager.merge(entity);	
		return entity;
		
	}

	@Override
	public void delete(TsscGame entity) {
		entityManager.remove(entity);		
		
	}

	@Override
	public Optional<TsscGame> findById(long codigo) {
		TsscGame a = entityManager.find(TsscGame.class, codigo);
		if(a!=null) {
			return Optional.of(a);					
		}
		return Optional.ofNullable(null);	
	}

	@Override
	public List<TsscGame> findAll() {
		String jpql = "SELECT a "
				+ "FROM TsscGame a";
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	public TsscGame findByName(String name) {
		String jpql = "SELECT a "
				+ "FROM TsscGame a "
				+ "WHERE a.name = '" +name +"'";
		return (TsscGame) entityManager.createQuery(jpql).getSingleResult();
	}
		

	public List<TsscGame> findByLinkedTopic(long id) {
		String jpql = "SELECT a "
				+ "FROM TsscGame a "
				+ "WHERE a.tsscTopic.id = '"+id +"'";
		return entityManager.createQuery(jpql).getResultList();
	}
	
	public List<TsscGame> findByDateRange(LocalDate datemin, LocalDate datemax) {
		String jpql = "SELECT a "
				+ "FROM TsscGame a "
				+ "WHERE a.scheduledDate >= '" +datemin +"' "
				+ "AND a.scheduledDate <= '" +datemax +"' ";
		return entityManager.createQuery(jpql).getResultList();
	}
	
	public List<TsscGame> findByDateAndTimeRange(LocalDate date, LocalTime min, LocalTime max) {
		String jpql = "SELECT a "
				+ "FROM TsscGame a "
				+ "WHERE a.scheduledDate = '" +date +"'"
				+" AND a.scheduledTime <= '" +max +"' " +" "
				+ "AND a.scheduledTime >= '" +min +"'";
		return entityManager.createQuery(jpql).getResultList();
	}
	
	public List<TsscGame> findGameByDateWithMaxTenStoriesOrZeroTimecontrols(LocalDate date) {
		String jpql = "SELECT a "
				+ "FROM TsscGame a "
				+ "WHERE a.scheduledDate = '" +date +"'";
		return entityManager.createQuery(jpql).getResultList();
	}
	
	public Iterable<TsscGame> findGamesByDateWithMax9StoriesOr0Timecontrols(LocalDate date) {
		String jpql = " SELECT g"
				+ " FROM TsscGame g "
				+ " WHERE ( g.scheduledDate = '" +date +"' AND size(g.tsscStories) < 10 ) " 
				+ " OR size(g.tsscTimecontrols) = 0 ";	
		return 	entityManager.createQuery(jpql).getResultList();	
	}
	
	
}
