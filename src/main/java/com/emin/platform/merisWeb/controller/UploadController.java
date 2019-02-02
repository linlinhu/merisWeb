package com.emin.platform.merisWeb.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.emin.base.controller.BaseController;

@Controller
@RequestMapping("/uploadFile")
public class UploadController extends BaseController{
	
	@ResponseBody
	@RequestMapping(value = "/index",method = RequestMethod.GET)
	public ModelAndView startIndex(HttpServletRequest request) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView("modules/upload/manage");
		return mv;
	}
}