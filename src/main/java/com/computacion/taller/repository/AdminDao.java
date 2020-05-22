package com.computacion.taller.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.computacion.taller.model.TsscAdmin;

@Repository
@Scope("singleton")
public class AdminDao implements IAdminDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(TsscAdmin entity) {
		entityManager.persist(entity);		
		
	}

	@Override
	public void update(TsscAdmin entity) {
		entityManager.merge(entity);	
		
	}

	@Override
	public void delete(TsscAdmin entity) {
		entityManager.remove(entity);		
		
	}

	@Override
	public Optional<TsscAdmin> findById(long codigo) {
		TsscAdmin  a = entityManager.find(TsscAdmin.class, codigo);
		if(a!=null) {
			return Optional.of(a);					
		}
		
		return Optional.ofNullable(null);
	}

	@Override
	public List<TsscAdmin> findAll() {
		String jpql = "SELECT a FROM TsscAdmin a";
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	public TsscAdmin findByUser(String username) {
		String jpql = "SELECT a FROM TsscAdmin a WHERE a.user = '" +username +"'";
		return (TsscAdmin) entityManager.createQuery(jpql).getSingleResult();
	}
	
}
