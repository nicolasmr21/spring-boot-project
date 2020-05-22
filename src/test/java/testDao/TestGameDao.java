package testDao;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.computacion.taller.TallerAPP;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.repository.GameDao;
import com.computacion.taller.repository.StoryDao;
import com.computacion.taller.repository.TimecontrolDao;
import com.computacion.taller.repository.TopicDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TallerAPP.class})
@Rollback(false)
@TestInstance(Lifecycle.PER_METHOD)
public class TestGameDao {
	
	@Autowired
	private GameDao gameDao;
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private TimecontrolDao timecontrolDao;
	@Autowired
	private StoryDao storyDao;
	
	private TsscGame game;
	private TsscTopic topic;

	@BeforeEach
	public void setUp() {
		
		for (TsscGame t : gameDao.findAll()) {
			gameDao.delete(t);
		}	
		
		for(TsscTopic t: topicDao.findAll()) {
			topicDao.delete(t);;
		}
		
		topic= new TsscTopic();
		topic.setName("100-MGP");
		topic.setDescription("Scrum 100-MGP");
		topic.setDefaultGroups(5);
		topic.setDefaultSprints(5);
		topic.setGroupPrefix("100-Groups");
		topicDao.save(topic);
		game = new TsscGame();
		game.setName("GameXYZ");
		game.setNGroups(5);
		game.setNSprints(5);
		game.setAdminPassword("123");
		game.setGuestPassword("123");
		game.setScheduledTime(LocalTime.MIDNIGHT);
		game.setScheduledDate(LocalDate.of(2020, 7, 25));
		game.setStartTime(LocalTime.NOON);
		game.setTsscTopic(topic);
		gameDao.save(game);
		
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTest() {
		assertNotNull(gameDao);
		TsscGame gamex = new TsscGame();
		gamex.setName("Nuevo GameXYZ");
		gamex.setNGroups(5);
		gamex.setNSprints(5);
		gamex.setAdminPassword("12345");
		gamex.setGuestPassword("12345");
		gamex.setScheduledTime(LocalTime.MIDNIGHT);
		gamex.setScheduledDate(LocalDate.of(2020, 7, 25));
		gamex.setStartTime(LocalTime.NOON);
		gameDao.save(gamex);
		assertNotNull(gameDao.findById(gamex.getId()));
		assertEquals(gamex, gameDao.findById(gamex.getId()).get());
	}

	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateTest() {
		assertNotNull(gameDao);
		game.setName("Name updated");
		gameDao.update(game);
		assertNotNull(gameDao.findById(game.getId()).get());
		assertEquals(game.getName(), gameDao.findById(game.getId()).get().getName());
		game.setName("GameXYZ");
		gameDao.update(game);
		assertEquals(game.getName(), gameDao.findById(game.getId()).get().getName());
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteTest() {
		assertNotNull(gameDao);
		gameDao.delete(game);	
		assertFalse(gameDao.findById(game.getId()).isPresent());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByIdTest() {
		assertNotNull(gameDao);
		assertNotNull(gameDao.findById(game.getId()));
		assertEquals(game.getName(), gameDao.findById(game.getId()).get().getName());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findAllTest() {
		assertNotNull(gameDao);
		List<TsscGame> ad =  gameDao.findAll();
		assertEquals(1, ad.size());
		assertEquals(game.getName(), ad.get(0).getName());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByNameTest() {
		assertNotNull(gameDao);
		assertNotNull(gameDao.findByName(game.getName()));
		assertEquals(game.getScheduledDate(), gameDao.findByName(game.getName()).getScheduledDate());
	}
	
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByLinkedTopicTest() {
		assertNotNull(gameDao);
		List<TsscGame> result = gameDao.findByLinkedTopic(topic.getId());
		assertNotNull(result);
		assertNotNull(topicDao.findById(topic.getId()).get());
		assertNotNull(gameDao.findById(game.getId()).get());
		assertNotNull(gameDao.findById(game.getId()).get().getTsscTopic());
		assertEquals(1, gameDao.findByLinkedTopic(topic.getId()).size());
	}
	

	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByDateRangeTest() {
		assertNotNull(gameDao);
		assertNotNull(gameDao.findById(game.getId()).get());
		List<TsscGame> result = gameDao.findByDateRange(LocalDate.of(2000, 1, 1), LocalDate.of(2021, 1, 1));
		assertNotNull(result);
		assertNotNull(gameDao.findById(game.getId()).get());
		assertEquals(game.getName(), result.get(0).getName());
		result = gameDao.findByDateRange(LocalDate.of(2000, 1, 1), LocalDate.of(2017, 1, 1));
		assertNotNull(result);
		assertEquals(0, result.size());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByDateAndTimeRangeTest() {
		assertNotNull(gameDao);
		assertNotNull(gameDao.findById(game.getId()).get());
		List<TsscGame> result = gameDao.findByDateAndTimeRange(LocalDate.of(2020, 7, 25), LocalTime.MIN, LocalTime.MAX);
		assertNotNull(result);
		assertEquals(game.getName(), result.get(0).getName());
		result = gameDao.findByDateAndTimeRange(LocalDate.of(2010, 7, 25), LocalTime.MIN, LocalTime.MAX);		
		assertNotNull(result);
		assertEquals(0, result.size());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findGamesByDateWithMax9StoriesOr0Timecontrols(){
		assertNotNull(gameDao);
		assertNotNull(gameDao.findById(game.getId()));			
		assertNotNull(gameDao.findGamesByDateWithMax9StoriesOr0Timecontrols(LocalDate.of(2020, 7, 25)));
		assertEquals(1, gameDao.findGamesByDateWithMax9StoriesOr0Timecontrols(LocalDate.of(2020, 7, 25)).size() );	
		
		TsscStory story = new TsscStory();
		story.setDescription("Nueva historia de prueba");;
		story.setInitialSprint(BigDecimal.ONE);
		story.setBusinessValue(BigDecimal.TEN);
		story.setPriority(BigDecimal.TEN);
		story.setShortDescription("Historia");
		story.setTsscGame(game);
		storyDao.save(story);
		
		TsscTimecontrol tc = new TsscTimecontrol();
		tc.setName("tc");
		tc.setTsscGame(game);
		timecontrolDao.save(tc);
		
		assertNotNull(timecontrolDao.findById(tc.getId()).get());
		assertNotNull(timecontrolDao.findById(tc.getId()).get().getTsscGame());
		assertEquals(1, gameDao.findGamesByDateWithMax9StoriesOr0Timecontrols(LocalDate.of(2020, 7, 25)).size() );	
	}


}
