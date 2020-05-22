package testDao;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
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
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.repository.StoryDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
@Rollback(false)
@TestInstance(Lifecycle.PER_METHOD)
public class TestStoryDao {
	
	@Autowired
	private StoryDao storyDao;
	
	private TsscStory story;
	
	@BeforeEach
	public void setUp() {
		story = new TsscStory();
		story.setDescription("Nueva historia de prueba");;
		story.setInitialSprint(BigDecimal.ONE);
		story.setBusinessValue(BigDecimal.TEN);
		story.setPriority(BigDecimal.TEN);
		story.setShortDescription("Historia");
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTest() {
		assertNotNull(storyDao);
		TsscStory storyx = new TsscStory();
		storyx.setDescription("Otra historia de prueba");;
		storyx.setInitialSprint(BigDecimal.ONE);
		storyx.setBusinessValue(BigDecimal.TEN);
		storyx.setPriority(BigDecimal.TEN);
		storyx.setShortDescription("Historia");
		storyDao.save(storyx);
		assertNotNull(storyDao.findById(storyx.getId()));
		assertEquals(storyx, storyDao.findById(storyx.getId()).get());
		storyDao.delete(storyx);
	}

	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateTest() {
		assertNotNull(storyDao);
		storyDao.save(story);
		story.setDescription("Description updated");
		storyDao.update(story);
		assertNotNull(storyDao.findById(story.getId()).get());
		assertEquals(story.getDescription(), storyDao.findById(story.getId()).get().getDescription());
		story.setDescription("Nueva historia de prueba");
		storyDao.update(story);
		assertEquals(story.getDescription(), storyDao.findById(story.getId()).get().getDescription());
		storyDao.delete(story);
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteTest() {
		assertNotNull(storyDao);
		storyDao.save(story);
		storyDao.delete(story);	
		assertFalse(storyDao.findById(story.getId()).isPresent());
		storyDao.delete(story);
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByIdTest() {
		assertNotNull(storyDao);
		storyDao.save(story);
		assertNotNull(storyDao.findById(story.getId()));
		assertEquals(story.getDescription(), storyDao.findById(story.getId()).get().getDescription());
		storyDao.delete(story);
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findAllTest() {
		assertNotNull(storyDao);
		storyDao.save(story);
		List<TsscStory> ad =  storyDao.findAll();
		assertEquals(1, ad.size());
		assertEquals(story.getDescription(), ad.get(0).getDescription());
		storyDao.delete(story);
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByDescriptionTest() {
		assertNotNull(storyDao);
		storyDao.save(story);
		assertNotNull(storyDao.findByDescription(story.getDescription()));
		assertEquals(story.getShortDescription(), storyDao.findByDescription(story.getDescription()).getShortDescription());
		storyDao.delete(story);
	}

}
