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
import com.computacion.taller.delegate.GameDelegate;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTopic;


@SpringBootTest
@ContextConfiguration(classes = GameDelegateTest.class)
@TestInstance(Lifecycle.PER_METHOD)
public class GameDelegateTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private GameDelegate gameDelegate;
	
	private TsscTopic topicA;
	private TsscTopic[] topics;
	private TsscGame[] games;
	private TsscGame gameA;
	private TsscGame gameB;
	private TsscStory story;
	
	@Value("http://localhost:8080/api/games")
	private String resource;
	 
	@Value("http://localhost:8080/api/games/{id}")
	private String idResource;
	 
	@Value("http://localhost:8080/api/games/{id}/topic")
	private String topicResource;
	
	@Value("http://localhost:8080/api/games/{id}/stories")
	private String storiesResource;
	
	@BeforeEach
	public void setUp(){
		topicA= new TsscTopic();
		topicA.setName("100-MGP");
		topicA.setDescription("Scrum 100-MGP");
		topicA.setDefaultGroups(5);
		topicA.setDefaultSprints(5);
		topicA.setGroupPrefix("100-Groups");
		
		topics = new TsscTopic[1];
		topics[0] = topicA;
		
		story = new TsscStory();
		story.setShortDescription("Best Story");
		story.setPriority(BigDecimal.TEN);
		story.setBusinessValue(BigDecimal.TEN);
		story.setInitialSprint(BigDecimal.ONE);
		story.setTsscTopic(topicA);
		
		gameA = new TsscGame();
		gameA.setName("GameXYZ");
		gameA.setNGroups(5);
		gameA.setNSprints(5);
		gameA.setAdminPassword("123");
		gameA.setGuestPassword("123");
		gameA.setScheduledTime(LocalTime.MIDNIGHT);
		gameA.setScheduledDate(LocalDate.of(2020, 7, 25));
		gameA.setStartTime(LocalTime.NOON);
		gameA.setTsscTopic(topicA);
		
		gameB = new TsscGame();
		gameB.setName("GameXYZb");
		gameB.setNGroups(2);
		gameB.setNSprints(2);
		gameB.setAdminPassword("123b");
		gameB.setGuestPassword("123b");
		gameB.setScheduledTime(LocalTime.MIDNIGHT);
		gameB.setScheduledDate(LocalDate.of(2020, 7, 25));
		gameB.setStartTime(LocalTime.NOON);
		gameB.setTsscTopic(topicA);
		
		games = new TsscGame[2];
		games[0] = gameA;
		games[1] = gameB;
		
		gameDelegate.setResource(resource);
		gameDelegate.setIdResource(idResource);
		gameDelegate.setStoriesResource(storiesResource);
		gameDelegate.setTopicResource(topicResource);
	}
	
	
	@Test
	public void findAll() {
		when(restTemplate.getForObject(resource, TsscGame[].class)).thenReturn(games);
		List<TsscGame> gamex = gameDelegate.findAll();
		assertNotNull(gamex);
		assertEquals(2, gamex.size());
		verify(restTemplate).getForObject(resource, TsscGame[].class);
		verifyNoMoreInteractions(restTemplate);
	}
	 
	@Test
	public void save() {
		when(restTemplate.postForObject(resource, gameA, TsscGame.class)).thenReturn(gameA);
		gameDelegate.save(gameA);
		verify(restTemplate).postForObject(resource, gameA, TsscGame.class);
		verifyNoMoreInteractions(restTemplate);
	}
	
	@Test
	public void update() {
		when(restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(gameA), TsscGame.class, gameA.getId()))
		.thenReturn(ResponseEntity.of(Optional.of(gameA)));
		gameDelegate.update(gameA.getId(), gameA);
		verify(restTemplate).exchange(idResource, HttpMethod.PUT, new HttpEntity<>(gameA), TsscGame.class, gameA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	
	@Test
	public void delete() {
		Mockito.doNothing().when(restTemplate).delete(idResource, gameB.getId());
		gameDelegate.delete(gameB.getId());
		verify(restTemplate).delete(idResource, gameB.getId());
		verifyNoMoreInteractions(restTemplate);
	}
 
	@Test
	public void findById() {
		when(restTemplate.getForObject(idResource, TsscGame.class, gameA.getId())).thenReturn(gameA);
		TsscGame finded = gameDelegate.findById(gameA.getId());
		assertEquals(gameA, finded);
		verify(restTemplate).getForObject(idResource, TsscGame.class, gameA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	 

	@Test
	public void findStories() {
		TsscStory[] stories = new TsscStory[1];
		stories[0] = story;
		when(restTemplate.getForObject(storiesResource, TsscStory[].class, topicA.getId()))
			.thenReturn(stories);
		List<TsscStory> storiex = gameDelegate.findStories(topicA.getId());
		assertNotNull(storiex);
		assertEquals(1, storiex.size());
		verify(restTemplate).getForObject(storiesResource, TsscStory[].class, topicA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	
	
	@Test
	public void findTopic() {
		when(restTemplate.getForObject(topicResource, TsscTopic.class, topicA.getId()))
			.thenReturn(topicA);
		TsscTopic finded = gameDelegate.findTopic(topicA.getId());
		assertEquals(topicA, finded);
		verify(restTemplate).getForObject(topicResource, TsscTopic.class, topicA.getId());
		verifyNoMoreInteractions(restTemplate);
	 }
	
	
	
}
