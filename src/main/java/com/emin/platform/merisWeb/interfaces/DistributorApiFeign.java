package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 分销商接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul")
public interface DistributorApiFeign {
	
	/**
	 * 分页查询
	 * @param page 页码
	 * @param limit 条数
	 * @param pid 父级分销商id
	 * @param regionId 区域id
	 * @param name 名称关键字
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/queryPage",method = RequestMethod.GET)
	String getPages(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="parentNodeId") Long parentNodeId,
			@RequestParam(value="name") String name,
			@RequestParam(value="regionId") Long regionId);
	
	/**
	 * 分页查询
	 * @param page 页码
	 * @param limit 条数
	 * @param pid 父级分销商id
	 * @param regionId 区域id
	 * @param name 名称关键字
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/getPage",method = RequestMethod.GET)
	String getPages(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="parentNodeId") Long parentNodeId,
			@RequestParam(value="name") String name,
			@RequestParam(value="regionIds") String regionIds);
	
	@RequestMapping(value = "/api-dm/dis/findByRegionIds",method = RequestMethod.GET)
	String findByRegionIds(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="regionIds") String regionIds);
	
	

	@RequestMapping(value = "/api-dm/dis/findTree",method = RequestMethod.GET)
	String getTree(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 查询分销商详情
	 * @param id 分销商id
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/queryDetail",method = RequestMethod.GET)
	JSONObject detail(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="id") Long id);
	
	/**
	 * 查询分销商父节点详情
	 * @param id 分销商id
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/findByParentNodeId",method = RequestMethod.GET)
	String detailParent(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="parentNodeId") Long parentNodeId);
	


	/**
	 * 保存分销商信息
	 * @param disStr 分销商信息对象json字符串
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/saveOrUpdate",method = RequestMethod.POST)
	JSONObject save(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="disStr") String disStr);
	
	/**
	 * 禁用分销商
	 * @param ids 分销商id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/disable",method = RequestMethod.GET)
	String disable(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="ids") String ids);
	
	/**
	 * 启用分销商
	 * @param ids 分销商id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/activate",method = RequestMethod.GET)
	String activate(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="ids") String ids);
	
	/**
	 * 删除分销商
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/delete",method = RequestMethod.GET)
	String delete(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="id") Long id);
	
	@RequestMapping(value = "/api-dm/dis/findAllDisableIds",method = RequestMethod.GET)
	String findAllDisableIds(@RequestHeader(value="ecmId") Long ecmId);
	
	
	@RequestMapping(value = "/api-dm/dis/queryBrandByNodeId",method = RequestMethod.GET)
	String queryBrandByNodeId(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="nodeId") Long nodeId);
	
	
}
