package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.computacion.taller.model.TsscStory;

@Repository
@Scope("singleton")
public class StoryDao implements IStoryDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public TsscStory save(TsscStory entity) {
		entityManager.persist(entity);	
		return entity;
		
	}

	@Override
	public TsscStory update(TsscStory entity) {
		entityManager.merge(entity);	
		return entity;
		
	}

	@Override
	public void delete(TsscStory entity) {
		System.out.println(entity.getShortDescription());
		entityManager.remove(entity);		
		
	}

	@Override
	public Optional<TsscStory> findById(long codigo) {
		TsscStory  a = entityManager.find(TsscStory.class, codigo);
		if(a!=null) {
			return Optional.of(a);					
		}
		
		return Optional.ofNullable(null);		
	}

	@Override
	public List<TsscStory> findAll() {
		String jpql = "SELECT a"
				+ " FROM TsscStory a";
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	public TsscStory findByDescription(String desc) {
		String jpql = "SELECT a "
				+ "FROM TsscStory a "
				+ "WHERE a.description = '" +desc +"'";
		return (TsscStory) entityManager.createQuery(jpql).getSingleResult();
	}
	
}
