package com.computacion.taller.rest;

import java.net.URI;
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
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.service.StoryService;
import com.computacion.taller.service.TopicService;

@RestController
public class StoryRestController {
	
	@Autowired
	StoryService storyService;

	@GetMapping("/api/stories")
	public Iterable<TsscStory> getStories() {
		return storyService.findAll();
	}
	
	@GetMapping("/api/stories/{id}")
	public TsscStory getStory(@PathVariable("id") long id) {
		TsscStory t = storyService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t;
	}
	
	@GetMapping("/api/stories/{id}/game")
	public TsscGame getStoryGame(@PathVariable("id") long id) {
		TsscStory t = storyService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscGame();
	}
			
	@PostMapping("/api/stories")
	public ResponseEntity<Object> add(@RequestBody TsscStory story) {
		TsscStory savedStory = storyService.save(story);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedStory.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	

	@PutMapping("/api/stories/{id}")
	public ResponseEntity<Object> edit(@RequestBody TsscStory story, @PathVariable long id) {
		TsscStory t = storyService.findById(id);
		if(t==null) {
			return ResponseEntity.notFound().build();
		} 
		story.setId(id);
		storyService.edit(story);
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/api/stories/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id) {
		System.out.println("kkk");
		storyService.delete(storyService.findById(id));
		return ResponseEntity.ok().build();
	}
		
	
}
