package com.computacion.taller.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.computacion.taller.delegate.GameDelegate;
import com.computacion.taller.delegate.StoryDelegate;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.service.GameService;

@Controller
public class StoryController {
	
	@Autowired
	StoryDelegate storyDelegate;
	@Autowired
	GameDelegate gameService;

	@GetMapping("/stories/")
	public String index(Model model) {
		model.addAttribute("stories", storyDelegate.findAll());
		return "stories/index";
	}
		
	@GetMapping("/stories/add")
	public String add(Model model) {
		model.addAttribute("tsscStory", new TsscStory());
		model.addAttribute("games", gameService.findAll());
		return "stories/add";
	}
	
	@PostMapping("/stories/add")
	public String add(@Validated TsscStory story, BindingResult bd, @RequestParam(value = "action", required = true) String action, Model model) {
			
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscStory", story);
			model.addAttribute("games", gameService.findAll());
			return "stories/add";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			storyDelegate.save(story);
		}
		return "redirect:/stories/";
	}
	
	@GetMapping("/stories/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {
		model.addAttribute("tsscStory", storyDelegate.findById(id));
		model.addAttribute("games", gameService.findAll());
		return "stories/edit";
	}
	
	@PostMapping("/stories/edit/{id}")
	public String edit(@Validated TsscStory story, BindingResult bd, @RequestParam(value = "action", required = true) String action, @PathVariable("id") long id,  Model model) {
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscStory", story);
			model.addAttribute("games", gameService.findAll());
			return "topics/edit";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			TsscStory t = storyDelegate.findById(id);
			t.setShortDescription(story.getShortDescription());
			t.setPriority(story.getPriority());
			t.setBusinessValue(story.getBusinessValue());
			t.setInitialSprint(story.getInitialSprint());
			t.setTsscGame(story.getTsscGame());
			storyDelegate.update(id, t);
		}
		return "redirect:/stories/";
	}
	
	@GetMapping("/stories/showGame/{id}")
	public String showGame(Model model, @PathVariable("id") long id) {
		List<TsscGame> g = new ArrayList<>();
		g.add(storyDelegate.findGame(id));
		model.addAttribute("games", g);
		return "stories/game-story";
	}
	
	@GetMapping("/stories/remove/{id}")
	public String remove(@PathVariable("id") long id) {
		storyDelegate.delete(id);
		return "redirect:/stories/";
	}
		
}
