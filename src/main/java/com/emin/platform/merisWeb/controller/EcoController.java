package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.EcoApiFeign;
import com.emin.platform.merisWeb.interfaces.EcmApiFeign;

/**
 * 控制层-我的生态链信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/eco")
public class EcoController extends BaseController{
	
	@Autowired
	EcoApiFeign ecoApiFeign; //生态链接口服务
	@Autowired
	EcmApiFeign ecmApiFeign; //生态链接口服务
	
	/***
	 * 跳转至产品分类管理主页
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(){
		ModelAndView mv = new ModelAndView("modules/eco/manage");
		return mv;
    }
	
	/**
	 * 我的生态链信息获取
	 * @return
	 */
	@RequestMapping("/getMain")
	@ResponseBody
	public JSONObject getMain(){
		JSONObject ecos = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			ecos = ecoApiFeign.getMainChainDis(ecmId);
			if(!ecos.getBoolean("success")){
				throw new EminException(ecos.getString("code"));
			}
			JSONArray ecoArray = ecos.getJSONArray("result");
			JSONObject eco = null;
			Long rootEcmId = null;
			String subEcmIds = "";
			for (int i = 0; i < ecoArray.size(); i++) {
				eco = ecoArray.getJSONObject(i);
				Long ecoEcmId = eco.getLong("ecmId");
				if (eco.getLong("parentNodeId") != null) {
					subEcmIds += subEcmIds.length() > 0 ? "," + ecoEcmId : ecoEcmId;
				} else {
					rootEcmId = ecoEcmId;
				}
			}
			JSONObject ecmResult = null;
			JSONObject rootEcmInfo = null;
			if (rootEcmId != null) {
				ecmResult = ecmApiFeign.detail(rootEcmId);
				if (!ecmResult.getBooleanValue("success")) {
					throw new EminException(ecmResult.getString("code"));
				}
				rootEcmInfo = ecmResult.getJSONObject("result");
			}
			JSONArray subEcmArray = new JSONArray();
			if (subEcmIds != null && subEcmIds.length() > 0) {
				ecmResult = ecmApiFeign.findEcmByIds(subEcmIds);
				if (!ecmResult.getBooleanValue("success")) {
					throw new EminException(ecmResult.getString("code"));
				}
				subEcmArray = ecmResult.getJSONArray("result");
			}
			JSONArray namedEcos = new JSONArray();
			for (int i = 0; i < ecoArray.size(); i++) {
				eco = ecoArray.getJSONObject(i);
				if (eco.getLong("parentNodeId") != null) {
					for (int j = 0; j < subEcmArray.size(); j++) {
						JSONObject subEcm = subEcmArray.getJSONObject(j);
						if (subEcm.getLong("id").longValue() == eco.getLong("ecmId").longValue()) {
							eco.put("name", subEcm.getString("name"));
						}
					}
				} else {
					eco.put("name", rootEcmInfo.getString("name"));
				}
				namedEcos.add(eco);
			}
			ecos.put("result", namedEcos);
		}
		
		return ecos;
    }
	
	/**
	 * 我参与的生态链信息获取
	 * @return
	 */
	@RequestMapping("/getJoins")
	@ResponseBody
	public JSONObject getJoins(){
		JSONObject ecos = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			// 查询品牌信息，表单必选项赋值
			ecos = ecoApiFeign.getJoinChainDis(ecmId);
			if(!ecos.getBoolean("success")){
				throw new EminException(ecos.getString("code"));
			}
			JSONArray joinEcos = ecos.getJSONArray("result");
			JSONObject joinEco = null;
			JSONArray jeNodes = null;
			JSONObject jeNode = null;
			String joinEcmIds = ",";
			for (int i = 0; i < joinEcos.size(); i++) {
				joinEco = joinEcos.getJSONObject(i);
				jeNodes = joinEco.getJSONArray("tree");
				for (int j = 0; j < jeNodes.size(); j++) {
					jeNode = jeNodes.getJSONObject(j);
					if (joinEcmIds.indexOf(jeNode.getLong("ecmId").toString()) < 0) {
						joinEcmIds += jeNode.getLong("ecmId") + ",";
					}
				}
			}
			joinEcmIds = joinEcmIds.substring(1);
			JSONObject ecmsResult = null;
			JSONArray ecmsArray = null;
			if (joinEcmIds.length() > 0) {
				ecmsResult = ecmApiFeign.findEcmByIds(joinEcmIds);
				if (!ecmsResult.getBooleanValue("success")) {
					throw new EminException(ecmsResult.getString("code"));
				}
				ecmsArray = ecmsResult.getJSONArray("result");
			}
			JSONArray namedEcos = new JSONArray();
			for (int i = 0; i < joinEcos.size(); i++) {
				joinEco = joinEcos.getJSONObject(i);
				joinEco.put("name", getEcmByIdInList(joinEco.getLong("ecmId"), ecmsArray).getString("name"));
				jeNodes = joinEco.getJSONArray("tree");
				JSONArray namedNodes = new JSONArray();
				for (int j = 0; j < jeNodes.size(); j++) {
					jeNode = jeNodes.getJSONObject(j);
					jeNode.put("name", getEcmByIdInList(jeNode.getLong("ecmId"), ecmsArray).getString("name"));
					namedNodes.add(jeNode);
				}
				joinEco.put("tree", namedNodes);
				namedEcos.add(joinEco);
			}
			ecos.put("result", namedEcos);
		}
		
		return ecos;
    }
	
	private JSONObject getEcmByIdInList(Long id, JSONArray list) {
		JSONObject ecm = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			JSONObject cur = list.getJSONObject(i);
			if (cur.getLong("id").longValue() == id.longValue()) {
				ecm = cur;
				break;
			}
		}
		return ecm;
	}
	
}
