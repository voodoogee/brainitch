package com.voodoo.brainitch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.voodoo.brainitch.picross.entity.RequestModel;
import com.voodoo.brainitch.picross.solver.Solver;

@Controller
public class SolverController {
	
	@RequestMapping("/index")
	public String index(Map<String,Object> map) {
		return"/index";  
	}
	@ResponseBody
	@RequestMapping(value = "/solve", method = RequestMethod.POST)
	public int[][] solve(@RequestBody RequestModel model) {
		Solver solver = new Solver(model.getHtips(), model.getVtips());
		solver.solve();
		int[][] result = solver.gethModel().state;
		return result;
	}
}
