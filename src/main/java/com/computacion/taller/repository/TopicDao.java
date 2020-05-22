package com.computacion.taller.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.computacion.taller.model.TsscTopic;

@Repository
@Scope("singleton")
public class TopicDao implements ITopicDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public TsscTopic save(TsscTopic entity) {
		entityManager.persist(entity);	
		return entity;
		
	}

	@Override
	public TsscTopic update(TsscTopic entity) {
		entityManager.merge(entity);		
		return entity;
	}

	@Override
	public void delete(TsscTopic entity) {
		entityManager.remove(entity);		
		
	}

	@Override
	public Optional<TsscTopic> findById(long codigo) {
		TsscTopic  a = entityManager.find(TsscTopic.class, codigo);
		if(a!=null) {
			return Optional.of(a);					
		}
		
		return Optional.ofNullable(null);		
	}

	@Override
	public List<TsscTopic> findAll() {
		String jpql = "SELECT a FROM TsscTopic a";
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	public TsscTopic findByName(String name) {
		String jpql = "SELECT a FROM "
				+ "TsscTopic a "
				+ "WHERE a.name = '" +name +"'";
		return (TsscTopic) entityManager.createQuery(jpql).getSingleResult();
	}
	
	public TsscTopic findByDescription(String desc) {
		String jpql = "SELECT a "
				+ "FROM TsscTopic a "
				+ "WHERE a.description = '" +desc +"'";
		return (TsscTopic) entityManager.createQuery(jpql).getSingleResult();
	}
	
	public List<TsscTopic> findTopicWithGameAmountByDate(LocalDate date) {
		String jpql = "SELECT x from TsscTopic x WHERE x.id IN " 
				+ "(SELECT t.id"
				+ " FROM TsscTopic t, TsscGame g  "
				+ " WHERE t.id = g.tsscTopic.id "
				+ " AND g.scheduledDate = '" +date +"' "
				+ " ORDER BY g.scheduledTime ASC)";	
		return 	entityManager.createQuery(jpql).getResultList();	
	}
	
	
	
	
}
