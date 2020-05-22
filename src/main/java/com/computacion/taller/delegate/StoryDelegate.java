package com.computacion.taller.delegate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;

@Component
public class StoryDelegate {

	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${resource.stories}")
	 private String resource;
	 
	 @Value("${resource.stories}/{id}")
	 private String idResource;
	 
	 @Value("${resource.stories}/{id}/game")
	 private String gameResource;
	
	 
	 public List<TsscStory> findAll() {
		return Arrays.stream(restTemplate.getForObject(resource, TsscStory[].class)).collect(Collectors.toList());
	 }
	 
	 public TsscStory findById(long id) {
		return restTemplate.getForObject(idResource, TsscStory.class, id);
	 }
	 
	 public TsscGame findGame(long id) {
		return restTemplate.getForObject(gameResource, TsscGame.class, id);
	 }
	 
	 public void save(TsscStory story) {
		restTemplate.postForObject(resource, story, TsscStory.class);
	 }
	 
	 public void update(long id, TsscStory story) {
		restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(story), TsscStory.class, id).getBody();
	 }
	 
	 public void delete(long id) {
		restTemplate.delete(idResource, id);
	 }
}
