package com.computacion.taller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import com.computacion.taller.model.TsscAdmin;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.repository.GameDao;
import com.computacion.taller.service.AdminService;
import com.computacion.taller.service.GameService;
import com.computacion.taller.service.StoryService;
import com.computacion.taller.service.TimecontrolService;
import com.computacion.taller.service.TopicService;

/**
 * @author Nicolas Martinez
 * user -> super, password -> 123 (ROL SUPERADMIN)
 * user -> admin, password -> 123 (ROL ADMIN)
 */
@SpringBootApplication
@EnableJpaRepositories("com.computacion.taller.repository")
@EntityScan("com.computacion.taller.model")
@ComponentScan({"com.computacion.taller"})
public class TallerAPP {

	public static void main(String[] args) {
		ConfigurableApplicationContext c = SpringApplication.run(TallerAPP.class, args);
	}

	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@Bean
	public CommandLineRunner runner(AdminService adminService, TopicService topicService, GameService gameService,
			StoryService storyService, GameDao gameDao, TimecontrolService tcService) {
		return args -> {
			TsscAdmin superadmin = new TsscAdmin();
			superadmin.setSuperAdmin("YES");
			superadmin.setUser("super");
			superadmin.setPassword("{noop}123");
			adminService.save(superadmin);
			TsscAdmin admin = new TsscAdmin();
			admin.setSuperAdmin("NO");
			admin.setUser("admin");
			admin.setPassword("{noop}123");
			adminService.save(admin);
			TsscTopic topic= new TsscTopic();
			topic.setName("100-MGP");
			topic.setDescription("Scrum 100-MGP");
			topic.setDefaultGroups(5);
			topic.setDefaultSprints(5);
			topic.setGroupPrefix("100-Groups");
			TsscStory story = new TsscStory();
			story.setShortDescription("Best Story");
			story.setPriority(BigDecimal.TEN);
			story.setBusinessValue(BigDecimal.TEN);
			story.setInitialSprint(BigDecimal.ONE);
			story.setTsscTopic(topic);
			TsscGame game = new TsscGame();
			game.setName("GameXYZ");
			game.setNGroups(5);
			game.setNSprints(5);
			game.setAdminPassword("123");
			game.setGuestPassword("123");
			game.setScheduledTime(LocalTime.MIDNIGHT);
			game.setScheduledDate(LocalDate.of(2020, 7, 25));
			game.setStartTime(LocalTime.NOON);
			game.setTsscTopic(topic);
			ArrayList<TsscGame> games = new ArrayList<>();
			games.add(game);
			topicService.save(topic);
			game.setTsscTopic(topic);
			gameService.save(game);
			story.setTsscGame(game);
			story.setTsscTopic(topic);
			storyService.save(story);
			TsscTimecontrol tc = new TsscTimecontrol();
			tc.setType("r");
			tc.setState("ss");
			tc.setTimeInterval(new BigDecimal(2));
			tc.setTsscGame(game);
			tcService.save(tc);
		};
	}
}

	

