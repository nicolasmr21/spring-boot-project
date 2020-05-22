package com.computacion.taller.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.computacion.taller.model.TsscStory;
import com.computacion.taller.repository.GameDao;
import com.computacion.taller.repository.IGameDao;
import com.computacion.taller.repository.IStoryDao;
import com.computacion.taller.repository.StoryDao;

@Service
@Scope("singleton")
public class StoryService {

	private IStoryDao storyDao;
	private IGameDao gameDao;
	
	@Autowired
	public StoryService(StoryDao storyDao, GameDao gameDao) {
		this.storyDao = storyDao;
		this.gameDao = gameDao;
	}

	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscStory findById(long id) {
		Optional<TsscStory> t = storyDao.findById(id);
		if(t.isPresent()) {
			return t.get();
		}
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscStory save(TsscStory story) {
		
		if(story==null)
			return null;
		
		if(story.getBusinessValue().intValue() >0 && story.getInitialSprint().intValue()>0 
				&& story.getPriority().intValue()>0) {			
			if(story.getTsscGame()!=null && gameDao.findById(story.getTsscGame().getId()).isPresent()) {				
				story.setTsscTopic(gameDao.findById(story.getTsscGame().getId()).get().getTsscTopic());
				storyDao.save(story);
				return story;
			}
		}
		return null;
	}
	
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscStory edit(TsscStory story) {
		
		if(story==null)
			return null;
		
		if(storyDao.findById(story.getId()).isPresent() 
				&& story.getBusinessValue().intValue() >0 && story.getInitialSprint().intValue()>0 
				&& story.getPriority().intValue()>0) {
			
			if(story.getTsscGame()!=null && gameDao.findById(story.getTsscGame().getId()).isPresent()) {				
				storyDao.update(story);
				return story;
			}
			
		}
		return null;
	}

	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Iterable<TsscStory> findAll() {
		return storyDao.findAll();
	}
	
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(TsscStory t) {
		storyDao.delete(t);
	}
	
}
