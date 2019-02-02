package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.PrdCategoryApiFeign;
import com.emin.platform.merisWeb.interfaces.PrdFeatureTplApiFeign;

/**
 * 控制层-产品分类信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/prdCategory")
public class PrdCategoryController extends BaseController{
	@Autowired
	PrdCategoryApiFeign categoryApiFeign;//产品分类数据接口实现
	@Autowired
	PrdFeatureTplApiFeign prdFeatureTplApiFeign;//产品特性数据接口实现


	/***
	 * 跳转至产品分类管理主页
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(){
		ModelAndView mv = new ModelAndView("modules/prd_category/manage");
		return mv;
    }
	
	@RequestMapping("/tree")
	@ResponseBody
	public JSONArray getTree(String parentId) {
		JSONArray tree = new JSONArray();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resStr = categoryApiFeign.getTree(ecmId, parentId);
//			String resStr = "{result:[{\"id\":\"" + System.currentTimeMillis() + "\", \"name\":\"分类" + System.currentTimeMillis() + "\",\"isParent\":true}]}";
			if (resStr != null) {
				JSONObject res = JSONObject.parseObject(resStr);
				tree = res.getJSONArray("result");
			}
		}
		return tree;
    }
	
	/**
	 * 保存主体信息实体
	 * @param jsonStr 主体信息狮子字符串
	 * @return
	 */
	@RequestMapping("/savePrdCategory")
	@ResponseBody
	public JSONObject saveEcm(Long id, Long parentId, String name, String description){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = categoryApiFeign.save(ecmId, id, parentId, name, description);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	/**
	 * 保存主体信息实体
	 * @param jsonStr 主体信息狮子字符串
	 * @return
	 */
	@RequestMapping("/deletePrdCategory")
	@ResponseBody
	public JSONObject saveEcm(String ids){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = categoryApiFeign.delete(ecmId, ids);
			if (res != null) {
				resJson = JSONObject.parseObject(res);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	

	@RequestMapping("/getFeatures")
	@ResponseBody
	public JSONObject getFeatures(String categoryIds) {
		JSONObject res = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null && categoryIds != null && categoryIds.length() > 0) {
			String resStr = prdFeatureTplApiFeign.getFeatureTpl(ecmId, categoryIds);
			if (resStr != null) {
				res = JSONObject.parseObject(resStr);
				if(!res.getBoolean("success")){
					throw new EminException(res.getString("code"));
				}
			}
		} else {
			res.put("success", false);
			res.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return res;
	}
	
	@RequestMapping("/saveFeatureTpl")
	@ResponseBody
	public JSONObject saveFeatureTpl(Long categoryId, Long id, String name, Integer type){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			
			if (type == 1) {
				resJson = prdFeatureTplApiFeign.basicSave(ecmId, id, categoryId, name, 1);
			}
			if (type == 2) {
				resJson = prdFeatureTplApiFeign.packingSave(ecmId, id, categoryId, name, 1);
			}
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	@RequestMapping("/deleteFeatureTpl")
	@ResponseBody
	public JSONObject deleteFeatureTpl(Long id){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = prdFeatureTplApiFeign.delete(ecmId, id);
			if (res != null) {
				resJson = JSONObject.parseObject(res);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
}
