package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 车辆管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface LogisticTruckApiFeign {
	
	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 当前页
	 * @param limit 每页显示的条数
	 * @param keyword 查询关键字
	 * @param lcId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/truck/queryPage",method = RequestMethod.GET)
	JSONObject queryPage(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="lcId") Long lcId);
	
	/**
	 * 删除车辆
	 * @param ecmId 主体编号
	 * @param carNo 车牌号码
	 */
	@RequestMapping(value = "api-rdg/truck/deleteTruck",method = RequestMethod.GET)
	JSONObject deleteTruck(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="carNo") String carNo);
	
	/**
	 * 保存车辆
	 * @param ecmId 主体编号
	 * @param truckStr 车牌号码
	 */
	@RequestMapping(value = "api-rdg/truck/saveOrUpdateTruck",method = RequestMethod.GET)
	JSONObject saveOrUpdateTruck(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="truckStr") String truckStr);
	
}