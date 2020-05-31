package com.computacion.taller.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.repository.ITopicDao;
import com.computacion.taller.repository.TopicDao;
import antlr.collections.List;

@Service
@Scope("singleton")
public class TopicService {
	
	private ITopicDao topicDao;
	
	
	@Autowired
	public TopicService(TopicDao t) {
		topicDao = t;
	}
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Iterable<TsscTopic> findAll(){
		return topicDao.findAll();
	}
	
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscTopic findById(long id) {
		Optional<TsscTopic> t = topicDao.findById(id);
		if(t.isPresent()) {
			return t.get();
		}
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscTopic save(TsscTopic topic) {
		
		if(topic==null)
			return null;
		
		if(topic.getDefaultGroups()>0 && topic.getDefaultSprints()>0 ) {
			topicDao.save(topic);
			return topic;
			
		}
	
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscTopic edit(TsscTopic topic) {
		
		if(topic==null)
			return null;
		
		if(topicDao.findById(topic.getId()).isPresent() 
				&& topic.getDefaultGroups()>0 && topic.getDefaultSprints()>0) {
			topicDao.update(topic);
			return topic;
		}
		return null;
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(TsscTopic t) {
		topicDao.delete(t);
	}
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Iterable<TsscTopic> findByDate(LocalDate date){
		return topicDao.findTopicWithGameAmountByDate(date);
	}
	
}
