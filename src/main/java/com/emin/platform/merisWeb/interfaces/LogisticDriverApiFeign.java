package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



/***
 * 司机管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface LogisticDriverApiFeign {
	
	/**
	 * 分页查询
	 * @param ecmId 主体编号
	 * @param page 当前页
	 * @param limit 每页显示的条数
	 * @param keyword 查询关键字
	 * @param lcId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/td/queryPage",method = RequestMethod.GET)
	JSONObject queryPage(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="lcId") Long lcId);
	
	/**
	 * 删除货车司机
	 * @param ecmId 主体编号
	 * @param driverMobile 司机的手机号码
	 */
	@RequestMapping(value = "api-rdg/td/deleteTD",method = RequestMethod.GET)
	JSONObject deleteTD(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="driverMobile") String driverMobile);
	
	/**
	 * 保存货车司机
	 * @param ecmId 主体编号
	 * @param tdStr 司机信息json
	 * @return
	 */
	@RequestMapping(value = "api-rdg/td/saveOrUpdateTD",method = RequestMethod.GET)
	JSONObject saveOrUpdateTD(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="tdStr") String tdStr);
	
	/**
	 * 获取所有物流公司
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "api-rdg/lc/findAll",method = RequestMethod.GET)
	JSONObject findAllLC(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 获取所有司机
	 * @param ecmId 主体编号
	 * @param lcId 物流公司id
	 * @return
	 */
	@RequestMapping(value = "api-rdg/td/queryAll",method = RequestMethod.GET)
	JSONObject queryAllDriver(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="lcId") Long lcId);
}
