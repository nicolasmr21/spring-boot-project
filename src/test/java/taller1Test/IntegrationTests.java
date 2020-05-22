package taller1Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.computacion.taller.TallerAPP;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.service.GameService;
import com.computacion.taller.service.StoryService;
import com.computacion.taller.service.TimecontrolService;
import com.computacion.taller.service.TopicService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TallerAPP.class})
@TestInstance(Lifecycle.PER_METHOD)
class IntegrationTests {
	
	
	@Autowired
	private  TopicService topicService;
	@Autowired
	private  GameService gameService;
	@Autowired
	private  StoryService storyService;
	@Autowired
	private  TimecontrolService timecontrolService;

	private TsscTopic validTopicA;
	private TsscGame validGameB;
	private TsscStory validStoryC;

	
	
	@BeforeEach
	public  void setUp() {
		
		validTopicA = new TsscTopic();
		validTopicA.setName("Action");
		validTopicA.setDescription("Nuevo tema");
		validTopicA.setDefaultGroups(2);
		validTopicA.setDefaultSprints(2);
		validTopicA.setGroupPrefix("AAA");
		
		validGameB = new TsscGame();
		validGameB.setName("Game");
		validGameB.setNGroups(10);
		validGameB.setNSprints(10);
		validGameB.setAdminPassword("12345");
		validGameB.setGuestPassword("12345");
		validGameB.setScheduledTime(LocalTime.MIDNIGHT);
		validGameB.setScheduledDate(LocalDate.of(2020, 7, 25));
		validGameB.setStartTime(LocalTime.NOON);
		
		validStoryC = new TsscStory();
		validStoryC.setTsscGame(validGameB);
		validStoryC.setDescription("Hello");
		validStoryC.setShortDescription("HHHhhh");
		validStoryC.setBusinessValue(BigDecimal.valueOf(10000));
		validStoryC.setPriority(BigDecimal.TEN);
		validStoryC.setInitialSprint(BigDecimal.ONE);
	}
	
	
	
	/**
	 * PUNTO A
	 */
	
	@Test
	@DisplayName("Save Valid Topic")
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveValidTopic() {
		assertAll(() -> assertEquals(topicService.save(validTopicA), validTopicA),
					() -> assertNotNull(topicService.findById(validTopicA.getId()))
				);
		
	}
	
			
	@Test
	@DisplayName("Edit Existing Topic With CorrectValues")
	public void editExistingTopicWithCorrectValues() {
		topicService.save(validTopicA);
		TsscTopic editedTopic = validTopicA;
		editedTopic.setName("Edited");
		assertAll(() -> assertEquals(editedTopic, topicService.edit(editedTopic)),
				() -> assertEquals(editedTopic.getName(), topicService.findById(editedTopic.getId()).getName())
			);
		
	}
	
	
	
	@Test
	@DisplayName("Edit Not Existing Topic")
	public void editNotExistingTopic() {
		TsscTopic editedTopic = validTopicA;
		editedTopic.setDefaultGroups(0);
		editedTopic.setDefaultSprints(0);
		editedTopic.setName("Edited is not existing");
		assertAll(() -> assertNull(topicService.edit(editedTopic)),
				() -> assertNull(topicService.findById(editedTopic.getId()))
			);
		
	}
		
	/**
	 * PUNTO B	
	 */
	
	
	@Test
	@DisplayName("Save Valid Game Without Topic")
	public void saveValidGameWithoutTopic() {
		validGameB = new TsscGame();
		validGameB.setName("Gsssame");
		validGameB.setNGroups(10);
		validGameB.setNSprints(10);
		validGameB.setAdminPassword("12345");
		validGameB.setGuestPassword("12345");
		validGameB.setScheduledTime(LocalTime.MIDNIGHT);
		validGameB.setScheduledDate(LocalDate.of(2020, 7, 25));
		validGameB.setStartTime(LocalTime.NOON);
		assertAll(() -> assertEquals(gameService.save(validGameB), validGameB),
				() -> assertNotNull(gameService.findById(validGameB.getId()))
			);
		
	}
	
	@Test
	@DisplayName("Save Valid Game With Existing Topic")
	public void saveValidGameWithExistingTopic() {
		topicService.save(validTopicA);
		validGameB.setTsscTopic(validTopicA);
		validGameB.setName("Other");
		assertEquals(gameService.save(validGameB), validGameB);
				
	}
	
	@Test
	@DisplayName("Save Valid Game With Not Existing Topic")
	public void saveValidGameWithNotExistingTopic() {
		validGameB.setTsscTopic(validTopicA);
		assertAll(() -> assertNull(gameService.save(validGameB)),
				() -> assertNull(gameService.findById(validGameB.getId()))
			);
		
	}
	
	
	
	@Test
	@DisplayName("Edit Existing Game With Correct Values")
	public void editExistingGameWithCorrectValues() {
		gameService.save(validGameB);
		TsscGame editedGame = validGameB;
		editedGame.setName("Edited");
		editedGame.setNSprints(200);
		editedGame.setNGroups(100);
		assertAll(() -> assertEquals(editedGame, gameService.edit(editedGame)),
				() -> assertEquals(editedGame.getName(),gameService.findById(editedGame.getId()).getName())
			);
		
	}
	
	
	
	@Test
	@DisplayName("Edit Existing Game With Existing Topic")
	public void editExistingGameWithExistingTopic() {
		gameService.save(validGameB);
		TsscGame editedGame = validGameB;
		topicService.save(validTopicA);
		editedGame.setTsscTopic(validTopicA);
		editedGame.setName("Edited");
		assertAll(() -> assertEquals(editedGame, gameService.edit(editedGame)),
				() -> assertEquals(validTopicA.getId(),gameService.findById(editedGame.getId()).getTsscTopic().getId())
			);		
	}
	
	@Test
	@DisplayName("Edit Existing Game With Not Existing Topic")
	public void editExistingGameWithNotExistingTopic() {
		gameService.save(validGameB);
		TsscGame editedGame = validGameB;
		editedGame.setTsscTopic(validTopicA);
		editedGame.setName("Edited");
		assertAll(() -> assertNull(gameService.edit(editedGame)),
				() -> assertNull(topicService.findById(validTopicA.getId()))
			);
		
	}
	
	
	@Test
	@DisplayName("Edit Not Existing Game")
	public void editNotExistingGame() {
		TsscGame editedGame = new TsscGame();
		validGameB.setName("Game");
		validGameB.setNGroups(10);
		validGameB.setNSprints(10);
		validGameB.setAdminPassword("12345");
		validGameB.setGuestPassword("12345");
		validGameB.setScheduledTime(LocalTime.MIDNIGHT);
		validGameB.setScheduledDate(LocalDate.of(2020, 7, 25));
		validGameB.setStartTime(LocalTime.NOON);
		editedGame.setName("Edited is not existing");
		assertAll(() -> assertNull(gameService.edit(editedGame)),
				() -> assertNull(gameService.findById(validGameB.getId()))
			);		
	}

	
	/**
	 * PUNTO C
	 */
	
	@Test
	@DisplayName("Save Valid Story With Existing Game")
	public void saveValidStoryWithExistingGame() {
		gameService.save(validGameB);
		assertAll(() -> assertEquals(storyService.save(validStoryC), validStoryC),
				() -> assertNotNull(storyService.findById(validStoryC.getId()))
			);
		
	}
	
	
	@Test
	@DisplayName("Save Valid Story With Not Existing Game")
	public void saveValidStoryWithNotExistingGame() {
		validGameB.setId(7);
		validStoryC.setTsscGame(validGameB);
		assertAll(() -> assertNull(storyService.save(validStoryC)),
				() -> assertNull(gameService.findById(validGameB.getId()))
			);
		
	}
	

	
	@Test
	@DisplayName("Edit Not Existing Story")
	public void editNotExistingStory() {
		TsscStory editedStory = validStoryC;
		editedStory.setDescription("Edited is not existing");
		assertAll(() -> assertNull(storyService.edit(editedStory)),
				() -> assertNull(storyService.findById(validStoryC.getId()))
			);
		
	}
	

	
	@Test
	@DisplayName("Edit Existing Story With Correct Values")
	public void editExistingStoryWithCorrectValues() {
		gameService.save(validGameB);
		storyService.save(validStoryC);
		TsscStory editedStory = validStoryC;
		editedStory.setDescription("Edited");
		editedStory.setBusinessValue(BigDecimal.TEN);
		assertAll(() -> assertEquals(editedStory, storyService.edit(editedStory)),
				() -> assertEquals(editedStory.getDescription(),storyService.findById(validStoryC.getId()).getDescription())
			);
		
	}
	
	@Test
	@DisplayName("Edit Existing Story With Existing Game")
	public void editExistingStoryWithExistingGame() {
		gameService.save(validGameB);
		storyService.save(validStoryC);
		TsscStory editedStory = validStoryC;
		editedStory.setDescription("Edited");
		TsscGame editedGame = validGameB;
		editedGame.setName("Edited");
		assertAll(() -> assertEquals(editedStory, storyService.edit(editedStory)),
				() -> assertEquals(editedStory.getDescription(),storyService.findById(validStoryC.getId()).getDescription()),
				() -> assertNotNull(gameService.findById(editedGame.getId()))
			);
		
	}
	
	
	@Test
	@DisplayName("Edit Existing Story With Not Existing Game")
	public void editExistingStoryWithNotExistingGame() {
		TsscStory editedStory = validStoryC;
		TsscGame editedGame = validGameB;
		editedGame.setId(7);
		editedGame.setName("Edited");
		assertAll(() -> assertNull(storyService.edit(editedStory)),
					() -> assertNull(gameService.findById(editedGame.getId()))
			);
		
	}
	

	/**
	 * PUNTO D
	 */


	@Test
	@DisplayName("Save a Game With New Business logic")
	public void saveGame2() {
		
		topicService.save(validTopicA);
		gameService.save(validGameB);
		validStoryC.setTsscTopic(validTopicA);
		storyService.save(validStoryC);
		TsscTimecontrol t1 = new TsscTimecontrol();
		TsscTimecontrol t2 = new TsscTimecontrol();
		t1.setName("t1"); 
		t1.setTsscTopic(validTopicA);
		timecontrolService.save(t1);
		t2.setTsscTopic(validTopicA);
		t2.setName("t2");
		timecontrolService.save(t2);
		
		TsscGame gamex = new TsscGame();
		gamex.setName("Game");
		gamex.setNGroups(10);
		gamex.setNSprints(10);
		gamex.setAdminPassword("12345");
		gamex.setGuestPassword("12345");
		gamex.setScheduledTime(LocalTime.MIDNIGHT);
		gamex.setScheduledDate(LocalDate.of(2020, 7, 25));
		gamex.setStartTime(LocalTime.NOON);
		gamex.setTsscTopic(validTopicA);
		TsscGame gameSaved = gameService.save(gamex);
		assertEquals(gamex, gameSaved);
		assertTrue(gameSaved.getTsscTimecontrols().size()>=2);
		
	}

	
}
