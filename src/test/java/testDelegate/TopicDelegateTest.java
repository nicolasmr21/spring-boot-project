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
import com.computacion.taller.delegate.TopicDelegate;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTopic;


@SpringBootTest
@ContextConfiguration(classes = TopicDelegateTest.class)
@TestInstance(Lifecycle.PER_METHOD)
public class TopicDelegateTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private TopicDelegate topicDelegate;
	
	private TsscTopic topicA;
	private TsscTopic topicB;
	private TsscTopic[] topics;
	private TsscGame game;
	private TsscStory story;	
	@Value("http://localhost:8080/api/topics")
	private String resource;
	 
	@Value("http://localhost:8080/api/topics/{id}")
	private String idResource;
	 
	@Value("http://localhost:8080/api/topics/{id}/games")
	private String gamesResource;
	
	@Value("http://localhost:8080/api/topics/{id}/stories")
	private String storiesResource;
	
	@BeforeEach
	public void setUp(){
		topicA= new TsscTopic();
		topicA.setName("100-MGP");
		topicA.setDescription("Scrum 100-MGP");
		topicA.setDefaultGroups(5);
		topicA.setDefaultSprints(5);
		topicA.setGroupPrefix("100-Groups");
		
		topicB= new TsscTopic();
		topicB.setName("100-MGPB");
		topicB.setDescription("Scrum 100-MGPB");
		topicB.setDefaultGroups(3);
		topicB.setDefaultSprints(3);
		topicB.setGroupPrefix("100-GroupsB");
		
		topics = new TsscTopic[2];
		topics[0] = topicA;
		topics[1] = topicB;
		
		story = new TsscStory();
		story.setShortDescription("Best Story");
		story.setPriority(BigDecimal.TEN);
		story.setBusinessValue(BigDecimal.TEN);
		story.setInitialSprint(BigDecimal.ONE);
		story.setTsscTopic(topicA);
		
		game = new TsscGame();
		game.setName("GameXYZ");
		game.setNGroups(5);
		game.setNSprints(5);
		game.setAdminPassword("123");
		game.setGuestPassword("123");
		game.setScheduledTime(LocalTime.MIDNIGHT);
		game.setScheduledDate(LocalDate.of(2020, 7, 25));
		game.setStartTime(LocalTime.NOON);
		game.setTsscTopic(topicA);
		
		topicDelegate.setResource(resource);
		topicDelegate.setIdResource(idResource);
		topicDelegate.setGamesResource(gamesResource);
		topicDelegate.setStoriesResource(storiesResource);
	}
	
	
	@Test
	public void findAll() {
		when(restTemplate.getForObject(resource, TsscTopic[].class)).thenReturn(topics);
		List<TsscTopic> topicsx = topicDelegate.findAll();
		assertNotNull(topicsx);
		assertEquals(2, topicsx.size());
		verify(restTemplate).getForObject(resource, TsscTopic[].class);
		verifyNoMoreInteractions(restTemplate);
	}
	 
	@Test
	public void save() {
		when(restTemplate.postForObject(resource, topicA, TsscTopic.class)).thenReturn(topicA);
		topicDelegate.save(topicA);
		verify(restTemplate).postForObject(resource, topicA, TsscTopic.class);
		verifyNoMoreInteractions(restTemplate);
	}
	
	@Test
	public void update() {
		when(restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(topicA), TsscTopic.class, topicA.getId()))
		.thenReturn(ResponseEntity.of(Optional.of(topicA)));
		topicDelegate.update(topicA.getId(), topicA);
		verify(restTemplate).exchange(idResource, HttpMethod.PUT, new HttpEntity<>(topicA), TsscTopic.class, topicA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	
	@Test
	public void delete() {
		Mockito.doNothing().when(restTemplate).delete(idResource, topicB.getId());
		topicDelegate.delete(topicB.getId());
		verify(restTemplate).delete(idResource, topicB.getId());
		verifyNoMoreInteractions(restTemplate);
	}
 
	@Test
	public void findById() {
		when(restTemplate.getForObject(idResource, TsscTopic.class, topicA.getId())).thenReturn(topicA);
		TsscTopic finded = topicDelegate.findById(topicA.getId());
		assertEquals(topicA, finded);
		verify(restTemplate).getForObject(idResource, TsscTopic.class, topicA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	 
	@Test
	public void findGames() {
		TsscGame[] games = new TsscGame[1];
		games[0] = game;
		when(restTemplate.getForObject(gamesResource, TsscGame[].class, topicA.getId()))
			.thenReturn(games);
		List<TsscGame> gamex = topicDelegate.findGames(topicA.getId());
		assertNotNull(gamex);
		assertEquals(1, gamex.size());
		verify(restTemplate).getForObject(gamesResource, TsscGame[].class, topicA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	 
	@Test
	public void findStories() {
		TsscStory[] stories = new TsscStory[1];
		stories[0] = story;
		when(restTemplate.getForObject(storiesResource, TsscStory[].class, topicA.getId()))
			.thenReturn(stories);
		List<TsscStory> storiex = topicDelegate.findStories(topicA.getId());
		assertNotNull(storiex);
		assertEquals(1, storiex.size());
		verify(restTemplate).getForObject(storiesResource, TsscStory[].class, topicA.getId());
		verifyNoMoreInteractions(restTemplate);
	}
	
	
}
