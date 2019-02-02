package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 品牌接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul")
public interface BrandApiFeign {

	/**
	 * 查询所有品牌信息
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "/api-pro/brand/findAll",method = RequestMethod.GET)
	String getAll(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 查询品牌详情
	 * @param ecmId 主体编号
	 * @param id 品牌编号
	 * @return
	 */
	@RequestMapping(value = "/api-pro/brand/queryDetail",method = RequestMethod.GET)
	JSONObject detail(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="id") Long id);
	
	@RequestMapping(value = "/api-pro/brand/queryPage",method = RequestMethod.GET)
	String getPages(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	
	@RequestMapping(value = "/api-pro/brand/deleteById",method = RequestMethod.GET)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
	
	@RequestMapping(value = "/api-pro/brand/saveOrUpdateBrand",method = RequestMethod.POST)
	JSONObject save(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="brandStr") String brandStr);
	
	
	
}
