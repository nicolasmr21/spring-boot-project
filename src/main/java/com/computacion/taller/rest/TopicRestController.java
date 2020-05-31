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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.service.TopicService;

@RestController
public class TopicRestController {
	
	@Autowired
	TopicService topicService;

	@GetMapping("/api/topics")
	public Iterable<TsscTopic> getTopics() {
		return topicService.findAll();
	}
	
	@GetMapping("/api/topics/query/{date}")
	public Iterable<TsscTopic> getTopicsByDate(@PathVariable("date") String date) {		
		return topicService.findByDate(LocalDate.parse(date));
	}
	
	@GetMapping("/api/topics/{id}")
	public TsscTopic getTopic(@PathVariable("id") long id) {
		TsscTopic t = topicService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t;
	}
	
	@GetMapping("/api/topics/{id}/stories")
	public List<TsscStory> getTopicStories(@PathVariable("id") long id) {
		TsscTopic t = topicService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscStories();
	}
	
	@GetMapping("/api/topics/{id}/games")
	public List<TsscGame> getTopicGames(@PathVariable("id") long id) {
		TsscTopic t = topicService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscGames();
	}
	
	
	@PostMapping("/api/topics")
	public ResponseEntity<Object> add(@RequestBody TsscTopic topic) {
		TsscTopic savedTopic = topicService.save(topic);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedTopic.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	

	@PutMapping("/api/topics/{id}")
	public ResponseEntity<Object> edit(@RequestBody TsscTopic topic, @PathVariable long id) {
		TsscTopic t = topicService.findById(id);
		if(t==null) {
			return ResponseEntity.notFound().build();
		} 
		topic.setId(id);
		topicService.edit(topic);
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/api/topics/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id) {
		topicService.delete(topicService.findById(id));
		return ResponseEntity.ok().build();
	}
		
	
}
