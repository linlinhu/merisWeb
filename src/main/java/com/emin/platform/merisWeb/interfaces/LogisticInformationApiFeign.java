package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 物流信息接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface LogisticInformationApiFeign {
	
	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 当前页
	 * @param limit 每页显示的条数
	 * @param keyword 查询关键字
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/queryPage",method = RequestMethod.GET)
	String queryPage(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	
	/**
	 * 查询指定物流公司的详情
	 * @param ecmId 主体编号
	 * @param cId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/queryDetail",method = RequestMethod.GET)
	JSONObject queryDetail(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="cId") Long cId);
	
	/**
	 * 删除物流公司
	 * @param ecmId 主体编号
	 * @param cId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/deleteLC",method = RequestMethod.GET)
	String deleteLC(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="cId") Long cId);
	
	/**
	 * 保存物流公司
	 * @param ecmId 主体编号
	 * @param lcStr 物流公司json
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/saveOrUpdateLC",method = RequestMethod.GET)
	JSONObject saveOrUpdateLC(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="lcStr") String lcStr);
	
	/**
	 * 删除货车司机
	 * @param ecmId 主体编号
	 * @param driverMobile 司机的手机号码
	 */
	@RequestMapping(value = "api-rdg/td/deleteTD",method = RequestMethod.GET)
	String deleteTD(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="driverMobile") String driverMobile);
	
	/**
	 * 保存货车司机
	 * @param ecmId 主体编号
	 * @param tdStr 司机信息json
	 * @return
	 */
	@RequestMapping(value = "api-rdg/td/saveOrUpdateTD",method = RequestMethod.GET)
	String saveOrUpdateTD(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="tdStr") String tdStr);
	
	/**
	 * 删除车辆
	 * @param ecmId 主体编号
	 * @param carNo 车牌号码
	 */
	@RequestMapping(value = "api-rdg/truck/deleteTruck",method = RequestMethod.GET)
	String deleteTruck(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="carNo") String carNo);
	
	/**
	 * 保存车辆
	 * @param ecmId 主体编号
	 * @param truckStr 车牌号码
	 */
	@RequestMapping(value = "api-rdg/truck/saveOrUpdateTruck",method = RequestMethod.GET)
	String saveOrUpdateTruck(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="truckStr") String truckStr);
	
}
