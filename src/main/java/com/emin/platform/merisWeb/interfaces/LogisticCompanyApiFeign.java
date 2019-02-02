package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;


/***
 * 物流公司管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface LogisticCompanyApiFeign {
	
	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 当前页
	 * @param limit 每页显示的条数
	 * @param keyword 查询关键字
	 * @param lcId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/queryPage",method = RequestMethod.GET)
	JSONObject queryPage(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="lcId") Long lcId);
	
	/**
	 * 查询指定物流公司的详情
	 * @param ecmId 主体编号
	 * @param cId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/queryDetail",method = RequestMethod.GET)
	JSONObject queryDetail(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="cId") Long cId);
	
	/**
	 * 删除物流公司
	 * @param ecmId 主体编号
	 * @param cId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/deleteLC",method = RequestMethod.GET)
	JSONObject deleteLC(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="cId") Long cId);
	
	/**
	 * 保存物流公司
	 * @param ecmId 主体编号
	 * @param lcStr 物流公司json
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/saveOrUpdateLC",method = RequestMethod.GET)
	JSONObject saveOrUpdateLC(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="lcStr") String lcStr);

}
