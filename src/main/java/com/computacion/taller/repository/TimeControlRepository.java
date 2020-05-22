package com.computacion.taller.repository;

import org.springframework.data.repository.CrudRepository;
import com.computacion.taller.model.TsscTimecontrol;

public interface TimeControlRepository extends CrudRepository<TsscTimecontrol, Long> {

}
