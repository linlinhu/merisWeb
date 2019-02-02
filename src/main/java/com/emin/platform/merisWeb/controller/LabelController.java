package com.emin.platform.merisWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.emin.base.controller.BaseController;

@Controller
@RequestMapping("/label")
public class LabelController extends BaseController{
	
	/*@Autowired
	PersonApiFeign personApiFeign;*/
	
	// 搜索
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String ecmId, String keyword, String token, Integer page, Integer limit, String ecmName){
		ModelAndView mv = new ModelAndView("modules/label/manage");
		return mv;
		
	}
	
}