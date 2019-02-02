package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 产品接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul")
public interface ProductApiFeign {
	
	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 页码
	 * @param limit 每页限制条数
	 * @param name 产品名称模糊查询
	 * @param sn 产品编号模糊查询
	 * @param tagIds 产品标签id，多个用逗号隔开
	 * @param prdCategoryId 产品所属分类id，单个查询
	 * @return 
	 */
	@RequestMapping(value = "/api-pro/product/queryPage",method = RequestMethod.GET)
	String getPages(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="name") String name,
			@RequestParam(value="sn") String sn,
			@RequestParam(value="tagIds") String tagIds,
			@RequestParam(value="prdCategoryId") Long prdCategoryId);
	
	@RequestMapping(value = "/api-pro/product/getPageByBrandId",method = RequestMethod.GET)
	String getAgentPages(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="brandId") Long brandId,
			@RequestParam(value="name") String name,
			@RequestParam(value="sn") String sn,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit);
	
	/**
	 * 查询详情
	 * @param ecmId 主体id
	 * @param id 产品编号
	 * @return 
	 */
	@RequestMapping(value = "/api-pro/product/queryDetail",method = RequestMethod.GET)
	JSONObject detail(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	

	/**
	 * 保存
	 * @param ecmId 主体编号
	 * @param productStr 对象字符串
	 * @return
	 */
	@RequestMapping(value = "/api-pro/product/saveOrUpdate",method = RequestMethod.POST)
	JSONObject save(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="productStr") String productStr);
	
	/**
	 * 保存图片
	 * @param ecmId 主体编号
	 * @param productStr 图片地址，多个用逗号分开
	 * @return
	 */
	@RequestMapping(value = "/api-pro/product/savePictures",method = RequestMethod.GET)
	JSONObject savePics(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="pictures") String pictures);
	
	
	/**
	 * 移除
	 * @param ecmId 主体编号
	 * @param id 产品编号
	 * @return
	 */
	@RequestMapping(value = "/api-pro/product/deleteById",method = RequestMethod.GET)
	String remove(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
	/**
	 * 保存产品特性
	 * @param ecmId 主体id
	 * @param pfStr 产品特性对象数组字符串
	 * @return
	 */
	@RequestMapping(value = "/api-pro/pf/saveOrUpdate",method = RequestMethod.POST)
	JSONObject savePf(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="pfStr") String pfStr);
	

	/**
	 * 根据品牌编号查询产品集合
	 * @param ecmId
	 * @param brandId
	 * @return
	 */
	@RequestMapping(value = "/api-pro/product/findByBrandId",method = RequestMethod.GET)
	String findByBrandId(
			@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="brandId") Long brandId);
	

	@RequestMapping(value = "/api-pro/product/getAgentBrandByEcmId",method = RequestMethod.GET)
	String getAgentBrands(@RequestHeader(value="ecmId") Long ecmId);
	

	

	
	
	
	
}
