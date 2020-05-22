package testDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.repository.GameDao;
import com.computacion.taller.repository.TopicDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
@Rollback(false)
@TestInstance(Lifecycle.PER_METHOD)
public class TestTopicDao {
	
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private GameDao gameDao;
	
	private TsscGame game;
	private TsscTopic topic;
	
	@BeforeEach
	public void setUp() {	
		for (TsscGame t : gameDao.findAll()) {
			gameDao.delete(t);
		}
		for (TsscTopic t : topicDao.findAll()) {
			topicDao.delete(t);
		}		
		topic = new TsscTopic();
		topic.setName("100-MGP");
		topic.setDescription("Scrum 100-MGP");
		topic.setDefaultGroups(5);
		topic.setDefaultSprints(5);
		topic.setGroupPrefix("100-Groups");
		topicDao.save(topic);		
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTest() {
		assertNotNull(topicDao);
		TsscTopic topicx = new TsscTopic();
		topicx.setName("Otro tema");
		topicx.setDescription("XYZ Nuevo");
		topicx.setDefaultGroups(5);
		topicx.setDefaultSprints(5);
		topicx.setGroupPrefix("100-Groups");
		topicDao.save(topicx);
		assertNotNull(topicDao.findById(topicx.getId()));
		assertEquals(topicx, topicDao.findById(topicx.getId()).get());
		topicDao.delete(topicx);
	}

	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateTest() {
		assertNotNull(topicDao);
		topic.setDescription("Description updated");
		topicDao.update(topic);
		assertNotNull(topicDao.findById(topic.getId()).get());
		assertEquals(topic.getName(), topicDao.findById(topic.getId()).get().getName());
		topic.setDescription("Scrum 100-MGP");
		topicDao.update(topic);
		assertEquals(topic.getName(), topicDao.findById(topic.getId()).get().getName());
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteTest() {
		assertNotNull(topicDao);
		topicDao.delete(topic);	
		assertFalse(topicDao.findById(topic.getId()).isPresent());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByIdTest() {
		assertNotNull(topicDao);
		assertNotNull(topicDao.findById(topic.getId()));
		assertEquals(topic.getName(), topicDao.findById(topic.getId()).get().getName());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findAllTest() {
		assertNotNull(topicDao);
		List<TsscTopic> ad =  topicDao.findAll();
		assertEquals(1, ad.size());
		assertEquals(topic.getName(), ad.get(0).getName());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByDescriptionTest() {
		assertNotNull(topicDao);
		assertNotNull(topicDao.findByDescription(topic.getDescription()));
		assertEquals(topic.getName(), topicDao.findByDescription(topic.getDescription()).getName());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findTopicWithGameAmountByDateTest() {
		assertNotNull(topicDao);
		assertNotNull(topicDao.findById(topic.getId()));		
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
		assertNotNull(gameDao.findById(game.getId()));
		assertNotNull(gameDao.findById(game.getId()).get().getTsscTopic());
		assertEquals(topic.getId(), gameDao.findById(game.getId()).get().getTsscTopic().getId());
		assertNotNull(topicDao.findById(topic.getId()).get());
		assertNotNull(gameDao.findById(game.getId()).get());
		assertNotNull(topicDao.findTopicWithGameAmountByDate(LocalDate.of(2020, 7, 25)));
		assertEquals(1, topicDao.findTopicWithGameAmountByDate(LocalDate.of(2020, 7, 25)).size() );	
	}


}
