package com.computacion.taller.repository;

import org.springframework.data.repository.CrudRepository;

import com.computacion.taller.model.TsscAdmin;

public interface AdminsRepository extends CrudRepository<TsscAdmin, Long> {
	
	public TsscAdmin findByUser(String user);

}
