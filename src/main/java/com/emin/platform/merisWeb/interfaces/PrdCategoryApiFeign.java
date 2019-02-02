package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 产品分类接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul")
public interface PrdCategoryApiFeign {
	
	/**
	 * 产品分类树加载
	 * @param ecmId 主体编号
	 * @param parentId 父级id
	 * @return
	 */
	@RequestMapping(value = "api-pro/category/tree",method = RequestMethod.GET)
	String getTree(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="parentId") String parentId);
	
	/**
	 * 产品分类保存
	 * @param ecmId 主体编号
	 * @param id 分类编号
	 * @param parentId 父级id
	 * @param name 分类名称
	 * @param description 分类描述（备用字段）
	 * @return
	 */
	@RequestMapping(value = "api-pro/category/saveOrUpdate",method = RequestMethod.POST)
	JSONObject save(@RequestHeader(value="ecmId") Long ecmId, 
			@RequestParam(value="id") Long id,
			@RequestParam(value="parentId") Long parentId,
			@RequestParam(value="name") String name,
			@RequestParam(value="description") String description);
	
	/**
	 * 产品分类删除
	 * @param ecmId 主体编号
	 * @param id 分类id
	 * @return
	 */
	@RequestMapping(value = "api-pro/category/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId, 
			@RequestParam(value="id") String id);
	
	
}
