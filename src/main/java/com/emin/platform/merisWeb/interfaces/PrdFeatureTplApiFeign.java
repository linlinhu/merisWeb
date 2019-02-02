package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 产品特性模板桥梁定义
 * @author kakadanica
 *
 */
@FeignClient(value = "zuul")
public interface PrdFeatureTplApiFeign {
	
	/**
	 * 根据分类查询特性模板
	 * @param ecmId 主体编号
	 * @param categoryIds 分类id，多个用逗号分开
	 * @return
	 */
	@RequestMapping(value = "/api-pro/featureTpl/{categoryIds}/featureTemplates",method = RequestMethod.GET)
	String getFeatureTpl(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("categoryIds") String categoryIds);
	
	/**
	 * 保存包装特性模板
	 * @param ecmId 主体编号
	 * @param id 模板编号
	 * @param categoryId 分类编号
	 * @param name 模板名称
	 * @param level 级别（越低优先级越高，预留字段）
	 * @return
	 */
	@RequestMapping(value = "/api-pro/featureTpl/packing/saveOrUpdate",method = RequestMethod.POST)
	JSONObject packingSave(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id,
			@RequestParam(value="categoryId") Long categoryId,
			@RequestParam(value="name") String name,
			@RequestParam(value="level") Integer level);
	
	/**
	 * 保存基础特性模板
	 * @param ecmId 主体编号
	 * @param id 模板编号
	 * @param categoryId 分类编号
	 * @param name 模板名称
	 * @param level 级别（越低优先级越高，预留字段）
	 * @return
	 */
	@RequestMapping(value = "/api-pro/featureTpl/basic/saveOrUpdate",method = RequestMethod.POST)
	JSONObject basicSave(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id,
			@RequestParam(value="categoryId") Long categoryId,
			@RequestParam(value="name") String name,
			@RequestParam(value="level") Integer level);
	
	/**
	 * 特性模板删除
	 * @param ecmId 主体编号
	 * @param id 模板编号
	 * @return
	 */
	@RequestMapping(value = "/api-pro/featureTpl/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
	
}
