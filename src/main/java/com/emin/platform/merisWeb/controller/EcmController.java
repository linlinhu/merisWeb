package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.dao.PageRequest;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.EcmApiFeign;
import com.emin.platform.merisWeb.interfaces.IndustryApiFeign;

/**
 * 控制层-主体信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/ecm")
public class EcmController extends MerisBaseController{
	
	@Autowired
	EcmApiFeign ecmApiFeign;//主体数据接口实现
	@Autowired
	IndustryApiFeign industryApiFeign;//行业数据接口实现
	
	/**
	 * 主体信息管理页面加载
	 * @param keyword 关键字，可选字段
	 * @param industryId 行业id，多个用","分开，可选字段
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword, String industryId){
		ModelAndView mv = new ModelAndView("modules/ecm/manage");
		PageRequest pageRequest = getPageRequestData();
		if (industryId != null && industryId.equals("0")) {
			industryId = null;
		}
		String pageRes = ecmApiFeign.getPages(pageRequest.getCurrentPage(), pageRequest.getLimit(), industryId, keyword);
		if (pageRes != null) {
			JSONObject pageResJson = JSONObject.parseObject(pageRes);
			if (!pageResJson.getBooleanValue("success")) {
				throw new EminException(pageResJson.getString("code"));
			}
			JSONObject pageResult = pageResJson.getJSONObject("result");
			mv.addObject("pageResult", pageResult);
		}
		String industryListRes = industryApiFeign.getValidList();
		
		if (industryListRes != null) {
			JSONObject industryListResJson = JSONObject.parseObject(industryListRes);
			mv.addObject("industries", industryListResJson.get("result"));
		}
		mv.addObject("keyword", keyword);
		mv.addObject("industryId", industryId);
		return mv;
    }
	
	/**
	 * 主体信息表单界面加载
	 * @param id 主体id，可选字段
	 * @return
	 */
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/ecm/form");
		//加载行业集合
		String industryListRes = industryApiFeign.getValidList();
		if (industryListRes != null) {
			JSONObject industryListResJson = JSONObject.parseObject(industryListRes);
			mv.addObject("industries", industryListResJson.get("result"));
		}
		
		if (id != null) {//根据id获取实体详情
			JSONObject ecm = ecmApiFeign.detail(id);
			if (ecm != null) {
				if(!ecm.getBooleanValue("success")) {
					throw new EminException(ecm.getString("code"));
				}
				JSONObject ecmJson = ecm.getJSONObject("result");
				if(ecm.getJSONObject("result").getJSONObject("logo")!=null) {
					String logoStr =  ecm.getJSONObject("result").getJSONObject("logo").toJSONString();
					ecmJson.put("logoStr", logoStr);	
				}
				
				mv.addObject("ecm", ecmJson);
			}
		}
		return mv;
    }
	
	/**
	 * 保存主体信息实体
	 * @param jsonStr 主体信息狮子字符串
	 * @return
	 */
	@RequestMapping("/saveEcm")
	@ResponseBody
	public JSONObject saveEcm(String jsonStr){
		JSONObject resJson = new JSONObject();
		resJson = ecmApiFeign.save(jsonStr);
		if(!resJson.getBoolean("success")){
			throw new EminException(resJson.getString("code"));
		}
		return resJson;
    }
	
	/**
	 * 禁用主体
	 * @param ids 主体id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping("/disableEcm")
	@ResponseBody
	public JSONObject disableEcm(String ids){
		
		JSONObject resJson = new JSONObject();
		
		String resJsonStr = ecmApiFeign.disable(ids);
		if (resJsonStr != null) {
			resJson = JSONObject.parseObject(resJsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		}
		
		return resJson;
    }
	/**
	 * 启用主体
	 * @param ids 主体id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping("/enableEcm")
	@ResponseBody
	public JSONObject enableEcm(String ids){
		
		JSONObject resJson = new JSONObject();
		String resJsonStr = ecmApiFeign.activate(ids);
		if (resJsonStr != null) {
			resJson = JSONObject.parseObject(resJsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		}
		return resJson;
    }
	
	/**
	 * 删除主体
	 * @param ids 主体id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping("/deleteEcm")
	@ResponseBody
	public JSONObject deleteEcm(String ids){
		
		JSONObject resJson = new JSONObject();
		Long id = Long.valueOf(ids);
		String resJsonStr = ecmApiFeign.delete(id);
		if (resJsonStr != null) {
			resJson = JSONObject.parseObject(resJsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		}
		
		return resJson;
    }
	
	/**
	 * 根据关键字模糊匹配获得所有主体信息数据集合
	 * @param keyword 关键字
	 * @return
	 */
	@RequestMapping("/getEcms")
	@ResponseBody
	public JSONObject getEcms(String keyword){
		JSONObject resJson = new JSONObject();
		if (keyword != null) {
			String resJsonStr = ecmApiFeign.getList(keyword);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "缺少查询关键字");
		}
		
		
		return resJson;
    }
	
}
