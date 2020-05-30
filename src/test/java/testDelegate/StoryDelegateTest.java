package testDelegate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import com.computacion.taller.delegate.StoryDelegate;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTopic;


@SpringBootTest
@ContextConfiguration(classes = StoryDelegateTest.class)
@TestInstance(Lifecycle.PER_METHOD)
public class StoryDelegateTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private StoryDelegate storyDelegate;
	
	private TsscTopic topic;
	private TsscStory[] stories;
	private TsscGame game;
	private TsscStory storyA;
	private TsscStory storyB;
	
	@Value("http://localhost:8080/api/stories")
	private String resource;
	 
	@Value("http://localhost:8080/api/stories/{id}")
	private String idResource;
	 
	@Value("http://localhost:8080/api/stories/{id}/game")
	private String gameResource;
		
	@BeforeEach
	public void setUp(){
		topic= new TsscTopic();
		topic.setName("100-MGP");
		topic.setDescription("Scrum 100-MGP");
		topic.setDefaultGroups(5);
		topic.setDefaultSprints(5);
		topic.setGroupPrefix("100-Groups");
		
		storyA = new TsscStory();
		storyA.setShortDescription("Best Story");
		storyA.setPriority(BigDecimal.TEN);
		storyA.setBusinessValue(BigDecimal.TEN);
		storyA.setInitialSprint(BigDecimal.ONE);
		storyA.setTsscTopic(topic);
		
		storyB = new TsscStory();
		storyB.setShortDescription("Best Story");
		storyB.setPriority(BigDecimal.TEN);
		storyB.setBusinessValue(BigDecimal.TEN);
		storyB.setInitialSprint(BigDecimal.ONE);
		storyB.setTsscTopic(topic);
		
		stories = new TsscStory[2];
		stories[0] = storyA;
		stories[1] = storyB;
		
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
		

		storyDelegate.setResource(resource);
		storyDelegate.setIdResource(idResource);
		storyDelegate.setGameResource(gameResource);
	
	}
	
	
	@Test
	public void findAll() {
		when(restTemplate.getForObject(resource, TsscStory[].class)).thenReturn(stories);
		List<TsscStory> storiex = storyDelegate.findAll();
		assertNotNull(storiex);
		assertEquals(2, storiex.size());
		verify(restTemplate).getForObject(resource, TsscStory[].class);
		verifyNoMoreInteractions(restTemplate);
	}
	 
	@Test
	public void save() {
		when(restTemplate.postForObject(resource, storyA, TsscStory.class)).thenReturn(storyA);
		storyDelegate.save(storyA);
		verify(restTemplate).postForObject(resource, storyA, TsscStory.class);
		verifyNoMoreInteractions(restTemplate);
	}
	
	@Test
	public void update() {
		when(restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(storyA), TsscStory.class, storyA.getId()))
		.thenReturn(ResponseEntity.of(Optional.of(storyA)));
		storyDelegate.update(storyA.getId(), storyA);
		verify(restTemplate).exchange(idResource, HttpMethod.PUT, new HttpEntity<>(storyA), TsscStory.class, storyA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	
	@Test
	public void delete() {
		Mockito.doNothing().when(restTemplate).delete(idResource, storyA.getId());
		storyDelegate.delete(storyA.getId());
		verify(restTemplate).delete(idResource, storyA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
 
	@Test
	public void findById() {
		when(restTemplate.getForObject(idResource, TsscStory.class, storyA.getId())).thenReturn(storyA);
		TsscStory finded = storyDelegate.findById(storyA.getId());
		assertEquals(storyA, finded);
		verify(restTemplate).getForObject(idResource, TsscStory.class, storyA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	 
	
	
	@Test
	public void findGame() {
		when(restTemplate.getForObject(gameResource, TsscGame.class, storyA.getId()))
			.thenReturn(game);
		TsscGame finded = storyDelegate.findGame(storyA.getId());
		assertEquals(game, finded);
		verify(restTemplate).getForObject(gameResource, TsscGame.class, storyA.getId());
		verifyNoMoreInteractions(restTemplate);
	 }
	
	
	
}
