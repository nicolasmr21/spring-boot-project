package com.computacion.taller.rest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.service.GameService;

@RestController
public class GameRestController {
	
	@Autowired
	GameService gameService;

	@GetMapping("/api/games")
	public Iterable<TsscGame> getTopics() {
		return gameService.findAll();
	}
	
	@GetMapping("/api/games/{id}")
	public TsscGame getTopic(@PathVariable("id") long id) {
		TsscGame t = gameService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t;
	}
	
	@GetMapping("/api/games/query/{date}")
	public Iterable<TsscGame> getGamesByDate(@PathVariable("date") String date) {		
		return gameService.findByDate(LocalDate.parse(date));
	}
	
	@GetMapping("/api/games/{id}/topic")
	public TsscTopic getGameTopic(@PathVariable("id") long id) {
		TsscGame t = gameService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscTopic();
	}
	
	@GetMapping("/api/games/{id}/stories")
	public List<TsscStory> getGameStories(@PathVariable("id") long id) {
		TsscGame t = gameService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscStories();
	}
	
	@GetMapping("/api/games/{id}/timecontrols")
	public List<TsscTimecontrol> getGameTimecontrols(@PathVariable("id") long id) {
		TsscGame t = gameService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscTimecontrols();
	}
			
	@PostMapping("/api/games")
	public ResponseEntity<Object> add(@RequestBody TsscGame game) {
		TsscGame saveGame = gameService.save(game);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(saveGame.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	

	@PutMapping("/api/games/{id}")
	public ResponseEntity<Object> edit(@RequestBody TsscGame game, @PathVariable long id) {
		TsscGame t = gameService.findById(id);
		if(t==null) {
			return ResponseEntity.notFound().build();
		} 
		game.setId(id);
		gameService.edit(game);
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/api/games/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id) {
		gameService.delete(gameService.findById(id));
		return ResponseEntity.ok().build();
	}
		
	
}
