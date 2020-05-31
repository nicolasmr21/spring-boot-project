package com.computacion.taller.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.repository.GameDao;
import com.computacion.taller.repository.IGameDao;
import com.computacion.taller.repository.IStoryDao;
import com.computacion.taller.repository.ITimecontrolDao;
import com.computacion.taller.repository.ITopicDao;
import com.computacion.taller.repository.StoryDao;
import com.computacion.taller.repository.TimecontrolDao;
import com.computacion.taller.repository.TopicDao;

@Service
@Scope("singleton")
public class GameService {

	private IGameDao gameDao;
	private ITopicDao topicDao;
	private IStoryDao storyDao;
	private ITimecontrolDao timecontrolDao;

	@Autowired
	public GameService(GameDao gameDao, TopicDao topicDao, StoryDao storyDao, TimecontrolDao tlDao) {
		this.gameDao = gameDao;
		this.topicDao = topicDao;
		this.storyDao = storyDao;
		this.timecontrolDao = tlDao;
	}
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscGame findById(long id) {
		Optional<TsscGame> g = gameDao.findById(id);
		if(g.isPresent()) {
			return g.get();
		}
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscGame save(TsscGame game) {
		
		if(game==null)
			return null;
		if(game.getNGroups()>0 && game.getNSprints()>0 ) {			
			if(game.getTsscTopic()==null) {	
				gameDao.save(game);
				return game;
			}
			else{
				Optional<TsscTopic> x =  topicDao.findById(game.getTsscTopic().getId());
				if(x.isPresent()) {
						
						gameDao.save(game);
						ArrayList<TsscStory> stories = new ArrayList<TsscStory>();
						ArrayList<TsscTimecontrol> tcs = new ArrayList<TsscTimecontrol>();
						if(x.get().getTsscStories() != null) {
							
							for (TsscStory t : x.get().getTsscStories()) {
								TsscStory n= new TsscStory();
								n.setAltDescripton(t.getAltDescripton());
								n.setBusinessValue(t.getBusinessValue());
								n.setDescription(t.getDescription());
								n.setInitialSprint(t.getInitialSprint());
								n.setPriority(t.getPriority());
								n.setShortDescription(t.getShortDescription());
								n.setNumber(t.getNumber());
								n.setTsscTopic(t.getTsscTopic());
								n.setTsscGame(game);
								stories.add(n);
								storyDao.save(n);
							}
							
						}
						
						if(x.get().getTsscTimecontrols() != null) {

							for (TsscTimecontrol t : x.get().getTsscTimecontrols()) {
								TsscTimecontrol n = new TsscTimecontrol();
								n.setName(t.getName());
								n.setTimeInterval(t.getTimeInterval());
								n.setName(t.getName());
								n.setAutostart(t.getAutostart());
								n.setIntervalRunning(t.getIntervalRunning());
								n.setTsscGame(game);
								tcs.add(n);
								timecontrolDao.save(n);
							}
						}
						
						game.setTsscStories(stories);
						game.setTsscTimecontrols(tcs);
						return game;
				}
			}
		}
		return null;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TsscGame edit(TsscGame game) {
		if(game==null)
			return null;
		if(gameDao.findById(game.getId()).isPresent() 
				&& game.getNGroups()>0 && game.getNSprints()>0) {
			
			if(game.getTsscTopic()==null) {				
				gameDao.update(game);
				return game;
			}
			else if(topicDao.findById(game.getTsscTopic().getId()).isPresent()) {
				gameDao.update(game);
				return game;
			}
		}
		return null;
	}

	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Iterable<TsscGame> findAll() {
		return gameDao.findAll();
	}
	
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Iterable<TsscGame> findByDate(LocalDate date) {
		return gameDao.findGamesByDateWithMax9StoriesOr0Timecontrols(date);
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(TsscGame t) {
		gameDao.delete(t);
	}

}
