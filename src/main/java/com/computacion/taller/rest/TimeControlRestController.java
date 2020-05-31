package com.computacion.taller.rest;

import java.net.URI;

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
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.service.TimecontrolService;

@RestController
public class TimeControlRestController {
	
	@Autowired
	TimecontrolService timecontrolService;

	@GetMapping("/api/timecontrols")
	public Iterable<TsscTimecontrol> getTimecontrols() {
		return timecontrolService.findAll();
	}
	
	@GetMapping("/api/timecontrols/{id}")
	public TsscTimecontrol getTimecontrol(@PathVariable("id") long id) {
		TsscTimecontrol t = timecontrolService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t;
	}
	
	@GetMapping("/api/timecontrols/{id}/game")
	public TsscGame getTimecontrolGame(@PathVariable("id") long id) {
		TsscTimecontrol t = timecontrolService.findById(id);
		if(t==null) {
			throw new RuntimeException();
		} 
		return t.getTsscGame();
	}
			
	@PostMapping("/api/timecontrols")
	public ResponseEntity<Object> add(@RequestBody TsscTimecontrol timecontrol) {
		TsscTimecontrol savedTimecontrol = timecontrolService.save(timecontrol);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedTimecontrol.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	

	@PutMapping("/api/timecontrols/{id}")
	public ResponseEntity<Object> edit(@RequestBody TsscTimecontrol timecontrol, @PathVariable long id) {
		TsscTimecontrol t = timecontrolService.findById(id);
		if(t==null) {
			return ResponseEntity.notFound().build();
		} 
		timecontrol.setId(id);
		timecontrolService.edit(timecontrol);
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/api/timecontrols/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id) {
		timecontrolService.delete(timecontrolService.findById(id));
		return ResponseEntity.ok().build();
	}
		
	
}
