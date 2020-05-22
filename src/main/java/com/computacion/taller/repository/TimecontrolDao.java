package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.computacion.taller.model.TsscTimecontrol;

@Repository
@Scope("singleton")
public class TimecontrolDao implements ITimecontrolDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(TsscTimecontrol entity) {
		entityManager.persist(entity);		
		
	}

	@Override
	public void update(TsscTimecontrol entity) {
		entityManager.merge(entity);		
		
	}

	@Override
	public void delete(TsscTimecontrol entity) {
		entityManager.remove(entity);		
		
	}

	@Override
	public Optional<TsscTimecontrol> findById(long codigo) {
		TsscTimecontrol a = entityManager.find(TsscTimecontrol.class, codigo);
		if(a!=null) {
			return Optional.of(a);					
		}
		
		return Optional.ofNullable(null);		
	}

	@Override
	public List<TsscTimecontrol> findAll() {
		String jpql = "SELECT a "
				+ "FROM TsscTimecontrol a";
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	
}
