package taller1Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.repository.GameDao;
import com.computacion.taller.repository.StoryDao;
import com.computacion.taller.repository.TimecontrolDao;
import com.computacion.taller.repository.TopicDao;
import com.computacion.taller.service.GameService;
import com.computacion.taller.service.StoryService;
import com.computacion.taller.service.TopicService;

@SpringBootTest
@ContextConfiguration(classes = UnitTests.class)
@TestInstance(Lifecycle.PER_METHOD)
class UnitTests {
	
	@Mock
	private  TopicDao topicRepository;
	@Mock
	private  GameDao gameRepository;
	@Mock
	private  StoryDao storyRepository;
	@Mock
	private  TimecontrolDao timecontrolRepository;
	
	@InjectMocks
	private  TopicService topicService;
	@InjectMocks
	private  GameService gameService;
	@InjectMocks
	private  StoryService storyService;

	
	private TsscTopic validTopicA;
	private TsscGame validGameB;
	private TsscStory validStoryC;


	
	@BeforeEach
	public  void setUp() {
		validTopicA = new TsscTopic();
		validTopicA.setName("Action");
		validTopicA.setDefaultGroups(2);
		validTopicA.setDefaultSprints(2);
		
		validGameB = new TsscGame();
		validGameB.setId(2);
		validGameB.setName("Game");
		validGameB.setNGroups(10);
		validGameB.setNSprints(10);
		
		validStoryC = new TsscStory();
		validStoryC.setDescription("Historia x");
		validStoryC.setTsscGame(validGameB);
		validStoryC.setBusinessValue(BigDecimal.valueOf(10000));
		validStoryC.setPriority(BigDecimal.TEN);
		validStoryC.setInitialSprint(BigDecimal.ONE);
	}
	
	
	
	/**
	 * PUNTO A
	 */
	@Test
	@DisplayName("Save Null Topic")
	public void saveNullTopic() {
		assertNull(topicService.save(null));
		verifyNoMoreInteractions(topicRepository);
	}
	
	
	@Test
	@DisplayName("Save Valid Topic")
	public void saveValidTopic() {
		when(topicRepository.save(validTopicA)).thenReturn(validTopicA);
		assertEquals(topicService.save(validTopicA), validTopicA);
		verify(topicRepository).save(validTopicA);
		verifyNoMoreInteractions(topicRepository);
	}
	
	@Test
	@DisplayName("Save Topic Without Groups")
	public void saveTopicWithoutGroups() {
		TsscTopic notValidTopicA = validTopicA;
		notValidTopicA.setDefaultGroups(0);
		when(topicRepository.save(notValidTopicA)).thenReturn(notValidTopicA);
		assertAll(() -> assertNull(topicService.save(notValidTopicA))
					, () -> assertNotEquals(topicService.save(notValidTopicA), notValidTopicA));
		verifyNoMoreInteractions(topicRepository); 
	}
	
	@Test
	@DisplayName("Save Topic Without Sprints")
	public void saveTopicWithoutSprints() {
		TsscTopic notValidTopicA = validTopicA;
		notValidTopicA.setDefaultSprints(0);
		when(topicRepository.save(notValidTopicA)).thenReturn(notValidTopicA);
		assertAll(() -> assertNull(topicService.save(notValidTopicA))
					, () -> assertNotEquals(topicService.save(notValidTopicA), notValidTopicA));
		verifyNoMoreInteractions(topicRepository); 
	}
	
	@Test
	@DisplayName("Save Topic Without Groups And Sprints")
	public void saveTopicWithoutGroupsAndSprints() {
		TsscTopic notValidTopicA = validTopicA;
		notValidTopicA.setDefaultGroups(0);
		notValidTopicA.setDefaultSprints(0);
		when(topicRepository.save(notValidTopicA)).thenReturn(notValidTopicA);
		assertAll(() -> assertNull(topicService.save(notValidTopicA))
					, () -> assertNotEquals(topicService.save(notValidTopicA), notValidTopicA));
		verifyNoMoreInteractions(topicRepository); 
	}
	
	
	@Test
	@DisplayName("Edit Existing Topic With CorrectValues")
	public void editExistingTopicWithCorrectValues() {
		TsscTopic editedTopic = validTopicA;
		editedTopic.setName("Edited");
		Optional<TsscTopic> opt = Optional.of(editedTopic);
		when(topicRepository.findById(editedTopic.getId())).thenReturn(opt);
		when(topicRepository.save(editedTopic)).thenReturn(editedTopic);
		assertEquals(editedTopic, topicService.edit(editedTopic));
		verify(topicRepository).findById(editedTopic.getId());
		verify(topicRepository).save(editedTopic);
		verifyNoMoreInteractions(topicRepository); 
	}
	
	@Test
	@DisplayName("Edit Existing Topic With IncorrectValues")
	public void editExistingTopicWithIncorrectValues() {
		TsscTopic editedTopic = validTopicA;
		Optional<TsscTopic> opt = Optional.of(editedTopic);
		editedTopic.setName("Edited");
		editedTopic.setDefaultGroups(0);
		editedTopic.setDefaultSprints(0);
		when(topicRepository.findById(editedTopic.getId())).thenReturn(opt);
		assertNull(topicService.edit(editedTopic));
		verify(topicRepository).findById(editedTopic.getId());
		verifyNoMoreInteractions(topicRepository); 	
	}
	
	
	@Test
	@DisplayName("Edit Not Existing Topic")
	public void editNotExistingTopic() {
		TsscTopic editedTopic = validTopicA;
		editedTopic.setDefaultGroups(0);
		editedTopic.setDefaultSprints(0);
		editedTopic.setName("Edited are not existing");
		when(topicRepository.findById(editedTopic.getId())).thenReturn(Optional.ofNullable(null));
		assertNull(topicService.edit(editedTopic));
		verify(topicRepository).findById(editedTopic.getId());
		verifyNoMoreInteractions(topicRepository); 
	}
	
		
	
	
	/**
	 * PUNTO B
	 */

	@Test
	@DisplayName("Save Null Game")
	public void saveNullGame() {
		assertNull(gameService.save(null));
		verifyNoMoreInteractions(gameRepository);
	}
	
	
	@Test
	@DisplayName("Save Valid Game Without Topic")
	public void saveValidGameWithoutTopic() {
		when(gameRepository.save(validGameB)).thenReturn(validGameB);
		assertEquals(gameService.save(validGameB), validGameB);
		verify(gameRepository).save(validGameB);
		verifyNoMoreInteractions(gameRepository);
	}
	
	@Test
	@DisplayName("Save Valid Game With Existing Topic")
	public void saveValidGameWithExistingTopic() {
		validGameB.setTsscTopic(validTopicA);
		Optional<TsscTopic> opt = Optional.of(validTopicA);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(opt);
		when(gameRepository.save(validGameB)).thenReturn(validGameB);
		assertEquals(gameService.save(validGameB), validGameB);
		verify(gameRepository).save(validGameB);
		verify(topicRepository).findById(validTopicA.getId());
		verifyNoMoreInteractions(gameRepository);
	}
	
	@Test
	@DisplayName("Save Valid Game With Not Existing Topic")
	public void saveValidGameWithNotExistingTopic() {
		validGameB.setTsscTopic(validTopicA);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(Optional.ofNullable(null));
		when(gameRepository.save(validGameB)).thenReturn(validGameB);
		assertNull(gameService.save(validGameB));
		verify(topicRepository).findById(validTopicA.getId());
		verifyNoMoreInteractions(gameRepository);
	}
	
	
	@Test
	@DisplayName("Save Not Valid Game Without Topic")
	public void saveNotValidGameWithoutTopic() {
		TsscGame notValidGame = validGameB;
		notValidGame.setId(5);
		notValidGame.setNGroups(0);
		notValidGame.setNSprints(0);
		when(gameRepository.save(notValidGame)).thenReturn(notValidGame);
		assertAll(() -> assertNull(gameService.save(notValidGame))
					, () -> assertNotEquals(gameService.save(notValidGame), notValidGame));
		verifyNoMoreInteractions(gameRepository); 
	}
	
	@Test
	@DisplayName("Save Not Valid Game With Existing Topic")
	public void saveNotValidGameWithExistingTopic() {
		TsscGame notValidGame = validGameB;
		notValidGame.setId(5);
		notValidGame.setTsscTopic(validTopicA);
		notValidGame.setNGroups(0);
		notValidGame.setNSprints(0);
		Optional<TsscTopic> opt = Optional.of(validTopicA);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(opt);
		when(gameRepository.save(notValidGame)).thenReturn(notValidGame);
		assertAll(() -> assertNull(gameService.save(notValidGame))
					, () -> assertNotEquals(gameService.save(notValidGame), notValidGame));
		verifyNoMoreInteractions(gameRepository); 
	}
	
	@Test
	@DisplayName("Save Not Valid Game With Not Existing Topic")
	public void saveNotValidGameWithNotExistingTopic() {
		TsscGame notValidGame = validGameB;
		notValidGame.setId(5);
		notValidGame.setTsscTopic(validTopicA);
		notValidGame.setNGroups(0);
		notValidGame.setNSprints(0);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(Optional.ofNullable(null));
		when(gameRepository.save(notValidGame)).thenReturn(notValidGame);
		assertAll(() -> assertNull(gameService.save(notValidGame))
					, () -> assertNotEquals(gameService.save(notValidGame), notValidGame));
		verifyNoMoreInteractions(gameRepository); 
	}
	
	@Test
	@DisplayName("Edit Existing Game With Correct Values")
	public void editExistingGameWithCorrectValues() {
		TsscGame editedGame = validGameB;
		editedGame.setName("Edited");
		Optional<TsscGame> opt = Optional.of(editedGame);
		when(gameRepository.findById(editedGame.getId())).thenReturn(opt);
		when(gameRepository.save(editedGame)).thenReturn(editedGame);
		assertEquals(editedGame, gameService.edit(editedGame));
		verify(gameRepository).findById(editedGame.getId());
		verify(gameRepository).save(editedGame);
		verifyNoMoreInteractions(gameRepository); 
	}
	
	@Test
	@DisplayName("Edit Existing Game With Incorrect Values")
	public void editExistingGameWithIncorrectValues() {
		TsscGame editedGame = validGameB;
		Optional<TsscGame> opt = Optional.of(editedGame);
		editedGame.setName("Edited");
		editedGame.setNGroups(0);
		editedGame.setNSprints(0);
		when(gameRepository.findById(editedGame.getId())).thenReturn(opt);
		assertNull(gameService.edit(editedGame));
		verify(gameRepository).findById(editedGame.getId());
		verifyNoMoreInteractions(gameRepository); 	
	}
	
	@Test
	@DisplayName("Edit Existing Game With Existing Topic")
	public void editExistingGameWithExistingTopic() {
		TsscGame editedGame = validGameB;
		editedGame.setTsscTopic(validTopicA);
		editedGame.setName("Edited");
		Optional<TsscGame> opt = Optional.of(editedGame);
		Optional<TsscTopic> opt2 = Optional.of(validTopicA);
		when(gameRepository.findById(editedGame.getId())).thenReturn(opt);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(opt2);
		when(gameRepository.save(editedGame)).thenReturn(editedGame);
		assertEquals(editedGame, gameService.edit(editedGame));
		verify(gameRepository).findById(editedGame.getId());
		verify(topicRepository).findById(validTopicA.getId());
		verify(gameRepository).save(editedGame);
		verifyNoMoreInteractions(gameRepository); 
	}
	
	@Test
	@DisplayName("Edit Existing Game With Not Existing Topic")
	public void editExistingGameWithNotExistingTopic() {
		TsscGame editedGame = validGameB;
		editedGame.setTsscTopic(validTopicA);
		editedGame.setName("Edited");
		Optional<TsscGame> opt = Optional.of(editedGame);
		when(gameRepository.findById(editedGame.getId())).thenReturn(opt);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(Optional.ofNullable(null));
		when(gameRepository.save(editedGame)).thenReturn(editedGame);
		assertNull(gameService.edit(editedGame));
		verify(gameRepository).findById(editedGame.getId());
		verify(topicRepository).findById(validTopicA.getId());
		verifyNoMoreInteractions(gameRepository); 
	}
	
	
	@Test
	@DisplayName("Edit Not Existing Game")
	public void editNotExistingGame() {
		TsscGame editedGame = validGameB;
		editedGame.setId(5);	
		editedGame.setName("Edited is not existing");
		when(gameRepository.findById(editedGame.getId())).thenReturn(Optional.ofNullable(null));
		assertNull(gameService.edit(editedGame));
		verify(gameRepository).findById(editedGame.getId());
		verifyNoMoreInteractions(gameRepository); 
	}

	
	/**
	 * PUNTO C
	 */

	
	@Test
	@DisplayName("Save Null Story")
	public void saveNullStory() {
		assertNull(storyService.save(null));
		verifyNoMoreInteractions(storyRepository);
	}
	
	
	@Test
	@DisplayName("Save Valid Story With Not Existing Game")
	public void saveValidStoryWithNotExistingGame() {
		when(storyRepository.save(validStoryC)).thenReturn(validStoryC);
		when(gameRepository.findById(validGameB.getId())).thenReturn(Optional.ofNullable(null));
		assertNull(storyService.save(validStoryC));
		verify(gameRepository).findById(validGameB.getId());
		verifyNoMoreInteractions(storyRepository);
	}
	
	
	@Test
	@DisplayName("Save Not Valid Story With Existing Game")
	public void saveNotValidStoryWhitExistingGame() {
		validStoryC.setInitialSprint(BigDecimal.valueOf(0));
		when(storyRepository.save(validStoryC)).thenReturn(validStoryC);
		when(gameRepository.findById(validGameB.getId())).thenReturn(Optional.of(validGameB));
		assertNull(storyService.save(validStoryC));
		verifyNoMoreInteractions(storyRepository);
	}
	
	@Test
	@DisplayName("Save Not Valid Story With Not Existing Game")
	public void saveNotValidStoryWhitNotExistingGame() {
		validStoryC.setInitialSprint(BigDecimal.valueOf(0));
		when(storyRepository.save(validStoryC)).thenReturn(validStoryC);
		when(gameRepository.findById(validGameB.getId())).thenReturn(Optional.ofNullable(null));
		assertNull(storyService.save(validStoryC));
		verifyNoMoreInteractions(storyRepository);
	}
	
	
	@Test
	@DisplayName("Edit Not Existing Story")
	public void editNotExistingStory() {
		TsscStory editedStory = validStoryC;
		editedStory.setDescription("Edited is not existing");
		when(storyRepository.findById(editedStory.getId())).thenReturn(Optional.ofNullable(null));
		assertNull(storyService.edit(editedStory));
		verify(storyRepository).findById(editedStory.getId());
		verifyNoMoreInteractions(storyRepository); 
	}
	
	@Test
	@DisplayName("Edit Existing Story With Incorrect Values")
	public void editExistingStoryWithIncorrectValues() {
		TsscStory editedStory = validStoryC;
		Optional<TsscStory> opt = Optional.of(editedStory);
		editedStory.setDescription("Edited");
		editedStory.setBusinessValue(BigDecimal.valueOf(0));;
		when(storyRepository.findById(editedStory.getId())).thenReturn(opt);
		assertNull(storyService.edit(editedStory));
		verify(storyRepository).findById(editedStory.getId());
		verifyNoMoreInteractions(storyRepository); 	
	}
	
	@Test
	@DisplayName("Save Valid Story With Existing Game")
	public void saveValidStoryWithExistingGame() {
		when(storyRepository.save(validStoryC)).thenReturn(validStoryC);
		when(gameRepository.findById(validGameB.getId())).thenReturn(Optional.of(validGameB));
		assertEquals(storyService.save(validStoryC), validStoryC);
	}
	
	@Test
	@DisplayName("Edit Existing Story With Correct Values")
	public void editExistingStoryWithCorrectValues() {
		TsscStory editedStory = validStoryC;
		editedStory.setBusinessValue(BigDecimal.valueOf(1350));
		Optional<TsscStory> opt = Optional.of(editedStory);
		when(storyRepository.findById(editedStory.getId())).thenReturn(opt);
		when(gameRepository.findById(validGameB.getId())).thenReturn(Optional.of(validGameB));
		when(storyRepository.save(editedStory)).thenReturn(editedStory);
		assertEquals(editedStory, storyService.edit(editedStory));
		verify(storyRepository).findById(editedStory.getId());
		verify(gameRepository).findById(editedStory.getTsscGame().getId());
		verify(storyRepository).save(editedStory);
		verifyNoMoreInteractions(storyRepository); 	
	}
	
	@Test
	@DisplayName("Edit Existing Story With Existing Game")
	public void editExistingStoryWithExistingGame() {
		TsscStory editedStory = validStoryC;
		TsscGame editedGame = validGameB;
		editedGame.setName("Edited");
		Optional<TsscStory> opt = Optional.of(editedStory);
		when(storyRepository.findById(editedStory.getId())).thenReturn(opt);
		when(gameRepository.findById(editedGame.getId())).thenReturn(Optional.of(editedGame));
		when(storyRepository.save(editedStory)).thenReturn(editedStory);
		assertEquals(editedStory, storyService.edit(editedStory));
		verify(storyRepository).findById(editedStory.getId());
		verify(gameRepository).findById(editedGame.getId());
		verify(storyRepository).save(editedStory);
		verifyNoMoreInteractions(storyRepository); 	
	}
	
	
	@Test
	@DisplayName("Edit Existing Story With Not Existing Game")
	public void editExistingStoryWithNotExistingGame() {
		TsscStory editedStory = validStoryC;
		TsscGame editedGame = validGameB;
		editedGame.setName("Edited");
		Optional<TsscStory> opt = Optional.of(editedStory);
		when(storyRepository.findById(editedStory.getId())).thenReturn(opt);
		when(gameRepository.findById(editedGame.getId())).thenReturn(Optional.ofNullable(null));
		when(storyRepository.save(editedStory)).thenReturn(editedStory);
		assertNull(storyService.edit(editedStory));
		verify(gameRepository).findById(editedGame.getId());
		verify(storyRepository).findById(editedStory.getId());
		verifyNoMoreInteractions(storyRepository); 	
	}
		
	/**
	 * PUNTO D
	 */
	
	@Test
	@DisplayName("Save a Game With New Business logic")
	public void saveGame2() {
		
		//A topic for this test that contains a story and
		//a game with 2 time controls
		
		TsscTimecontrol t1 = new TsscTimecontrol();
		TsscTimecontrol t2 = new TsscTimecontrol();
		t1.setName("t1"); 
		t2.setName("t2");
		ArrayList<TsscTimecontrol> ts = new ArrayList<>(); ts.add(t1); ts.add(t2);
		ArrayList<TsscStory> stories = new ArrayList<>(); stories.add(validStoryC);
		validTopicA.setTsscStories(stories);
		validTopicA.setTsscTimecontrols(ts);
		validGameB.setTsscTopic(validTopicA);
		
		when(gameRepository.save(validGameB)).thenReturn(validGameB);
		when(topicRepository.findById(validTopicA.getId())).thenReturn(Optional.of(validTopicA));
		TsscGame gameSaved = gameService.save(validGameB);
		
		assertEquals(validGameB, gameSaved);
		assertTrue(gameSaved.getTsscStories().size()==1);
		assertTrue(gameSaved.getTsscTimecontrols().size()==2);
		
		verify(topicRepository).findById(validTopicA.getId());
		verify(gameRepository).save(validGameB);
		verifyNoMoreInteractions(gameRepository);
		verifyNoMoreInteractions(topicRepository);
	
	}

	

}
