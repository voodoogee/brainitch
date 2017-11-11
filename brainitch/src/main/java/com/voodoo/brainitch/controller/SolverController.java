package com.voodoo.brainitch.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SolverController {
	
	@RequestMapping("/index")
	public String solve(Map<String,Object> map) {
		map.put("hello","hello");
		map.put("title","oh god");  
		return"/index";  
	}
}
