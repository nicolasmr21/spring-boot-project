package com.computacion.taller.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.computacion.taller.model.TsscTopic;


public interface TopicRepository extends CrudRepository<TsscTopic, Long> {

}
