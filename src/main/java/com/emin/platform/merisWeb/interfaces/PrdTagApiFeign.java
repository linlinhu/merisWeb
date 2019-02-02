package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 产品标签接口桥梁定义
 * @author kakadanica
 *
 */
@FeignClient(value = "zuul")
public interface PrdTagApiFeign {

	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 当前页
	 * @param limit 页显示条数
	 * @param keyword 关键字查询
	 * @return
	 */
	@RequestMapping(value = "/api-pro/tag/queryPage",method = RequestMethod.GET)
	String getHots(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	
	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 当前页
	 * @param limit 页显示条数
	 * @param keyword 关键字查询
	 * @return
	 */
	@RequestMapping(value = "/api-pro/tag/queryPageOrderByTime",method = RequestMethod.GET)
	String getPages(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	
	/**
	 * 保存
	 * @param ecmId 主体编号
	 * @param tagStr 产品标签对象字符串
	 * @return
	 */
	@RequestMapping(value = "/api-pro/tag/saveOrUpdateTag",method = RequestMethod.POST)
	JSONObject save(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="tagStr") String tagStr);
	
	/**
	 * 删除
	 * @param ecmId 主体编号
	 * @param id 产品标签id
	 * @return
	 */
	@RequestMapping(value = "/api-pro/tag/deleteTagById",method = RequestMethod.GET)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
	/**
	 * 查询所有标签
	 * @param ecmId 主体编号
	 * @param keyword 关键字查询
	 * @return
	 */
	@RequestMapping(value = "/api-pro/tag/findAll",method = RequestMethod.GET)
	String findAll(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="keyword") String keyword);
}
