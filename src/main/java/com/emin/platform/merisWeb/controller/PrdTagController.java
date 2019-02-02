package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.dao.PageRequest;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.PrdTagApiFeign;
import com.emin.platform.merisWeb.interfaces.ProductApiFeign;

/**
 * 控制层-产品标签信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/prdTag")
public class PrdTagController extends BaseController{

	@Autowired
	ProductApiFeign productApiFeign;//产品数据接口实现
	
	@Autowired
	PrdTagApiFeign prdTagApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword,String viewType){
		ModelAndView mv = new ModelAndView("modules/prd_tag/manage");
		/*Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			mv = new ModelAndView("500");
		} else {
			PageRequest pageRequest = getPageRequestData();
			String resStr = prdTagApiFeign.getPages(ecmId, pageRequest.getCurrentPage(),pageRequest.getLimit(), keyword);
			if (resStr != null) {
				JSONObject res = JSONObject.parseObject(resStr);
				if(!res.getBoolean("success")){
					throw new EminException(res.getString("code"));
				} else {
					mv.addObject("keyword", keyword);
					mv.addObject("pageResult", res.getJSONObject("result"));
				}
			}
		}*/
		mv.addObject("keyword", keyword);
		mv.addObject("viewType", viewType);
		return mv;
    }
	
	/**
	 * 保存产品标签信息实体
	 * @param jsonStr 产品标签信息实体字符串
	 * @return
	 */
	@RequestMapping("/savePrdTag")
	@ResponseBody
	public JSONObject savePrgTag(String jsonStr){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = prdTagApiFeign.save(ecmId, jsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	@RequestMapping("/getHot")
	@ResponseBody
	public JSONObject getHot(String selectedObjsStr){
		JSONObject pageResult = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			JSONObject res = new JSONObject();
			PageRequest pageReq = getPageRequestData();
			String resStr = prdTagApiFeign.getHots(ecmId, pageReq.getCurrentPage(), pageReq.getLimit(), "");
			if (resStr != null) {
				res = JSON.parseObject(resStr);
				if(!res.getBoolean("success")){
					throw new EminException(res.getString("code"));
				} else {
					pageResult = res.getJSONObject("result");
					JSONArray resultList = new JSONArray();
					if (selectedObjsStr != null && selectedObjsStr.length() > 0) {
						JSONArray choosenTags = JSONArray.parseArray(selectedObjsStr);
						JSONObject choosenObj = new JSONObject();
						for (int i = 0; i < choosenTags.size(); i++) {//用户选中的标签显示头前面
							choosenObj = choosenTags.getJSONObject(i);
							choosenObj.put("check", true);
							resultList.add(choosenObj);
						}
						JSONArray objs = pageResult.getJSONArray("resultList");//数据返回，去掉用户选中的标签，拼接在后面
						for (int i = 0; i < objs.size(); i++) {
							JSONObject obj = objs.getJSONObject(i);
							boolean isRepeat = false;
							for (int j = 0; j < choosenTags.size(); j++) {
								choosenObj = choosenTags.getJSONObject(j);
								if (choosenObj.getLong("id") == obj.getLong("id")) {
									isRepeat = true;
								}
							}
							if (!isRepeat) {
								resultList.add(obj);
							}
							
						}
					} else {
						resultList = pageResult.getJSONArray("resultList");
					}
					pageResult.put("resultList", resultList);
					pageResult.put("limit", pageReq.getLimit());
					pageResult.put("success", true);
					pageResult.put("message", "");
				}
			} else {
				res.put("success", false);
				res.put("message", "无法绑定主体信息，请重新登录！");
			}
		}
		return pageResult;
    }
	
	/**
	 * 删除产品标签
	 * @param id 产品标签id
	 * @return
	 */
	@RequestMapping("/deletePrdTag")
	@ResponseBody
	public JSONObject deleteEcm(Long ids){
		JSONObject resJson = new JSONObject();

		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
		
			String resJsonStr = prdTagApiFeign.delete(ecmId, ids);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
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
	
	/**
	 * 获取标签分页数据
	 * @param id 产品标签id
	 * @return
	 */
	@RequestMapping("/getPages")
	@ResponseBody
	public JSONObject getPages(String keyword, String selectedLabels){
		JSONObject resJson = new JSONObject();

		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("BASE_0.0.001");
		} else {
			PageRequest pageRequest = getPageRequestData();
			String resStr = prdTagApiFeign.getPages(ecmId, pageRequest.getCurrentPage(),pageRequest.getLimit(), keyword);
			if (resStr != null) {
				resJson = JSONObject.parseObject(resStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
				if (selectedLabels != null && selectedLabels.length() > 1) {
					JSONArray rlist = resJson.getJSONObject("result").getJSONArray("resultList");
					for (int  i = 0; i <  rlist.size(); i++) {
						String lname = "," + rlist.getJSONObject(i).getString("name") + ",";
						if (selectedLabels.indexOf(lname) >= 0) {
							rlist.getJSONObject(i).put("isSelect", true);
						}
					}
				}
			}
		}
		resJson.put("keyword", keyword);
		return resJson;
    }
	
	/**
	 * 获取所有标签
	 * @param id 产品标签id
	 * @return
	 */
	@RequestMapping("/findAll")
	@ResponseBody
	public JSONObject findAll(String keyword){
		JSONObject resJson = new JSONObject();

		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("BASE_0.0.001");
		} else {
			String resStr = prdTagApiFeign.findAll(ecmId, keyword);
			if (resStr != null) {
				resJson = JSONObject.parseObject(resStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				} 
			}
		}
		resJson.put("keyword", keyword);
		return resJson;
    }
	
	

	
}
