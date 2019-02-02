package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 行业接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul")
public interface IndustryApiFeign {

	/**
	 * 获取有效行业数据集合
	 * @return
	 */
	@RequestMapping(value = "/api-ecm/industry/getIndustries",method = RequestMethod.GET)
	String getValidList();
	
	/**
	 * 分页获取行业
	 * @param page
	 * @param limit
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/api-ecm/industry/queryPage",method = RequestMethod.GET)
	String getPages(@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	
	/**
	 * 删除行业
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api-ecm/industry/deleteById",method = RequestMethod.POST)
	String delete(@RequestParam(value="id") Long id);
	
	/**
	 * 保存行业
	 * @param industryStr
	 * @return
	 */
	@RequestMapping(value = "/api-ecm/industry/saveOrUpdateIndustry",method = RequestMethod.POST)
	JSONObject save(@RequestParam(value="industryStr") String industryStr);
	
}
