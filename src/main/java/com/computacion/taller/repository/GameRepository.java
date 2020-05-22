package com.computacion.taller.repository;

import org.springframework.data.repository.CrudRepository;

import com.computacion.taller.model.TsscGame;


public interface GameRepository extends CrudRepository<TsscGame, Long>{

}
