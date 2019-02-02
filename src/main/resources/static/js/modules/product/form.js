var i = 0,
	j = 0,
	isRepeat = false,
	productId = "",
	cateUlWidth = 0, // 默认分类选择器宽度为0
	productCates = [], //产品分类全局变量储存
	productTags = [], // 产品标签全局变量储存
	productFeaturesMap = [],
	productFeatures = [],
	maxPt = 10; // 呈放用户最终选择的产品标签库标签
/**
 * 产品分类赋值函数
 */
function valCateLst (lst) {
	var ids = '',
		v = '';
		
	for (i = 0, l = lst.length; i < l; i++) {
		ids += lst[i].id + ',';
		v += '<li>' + lst[i].name + '</li>';
	}
	if (ids.length > 0 ) {
		ids = ids.substring(0, ids.length - 1);
	}
	$('#pformCateTreeFilter').html(v);// 赋显示值
	$('#productForm input[name="prdCategories"]').val(ids);//赋id值，用于提交到数据库
	loadFeatureTpl();
}

// 加载特性模板
function loadFeatureTpl() {

	var featureSetTpl = pformFeatureDatas.innerHTML,//数据模板
		featureSetView = $('#pformFeature-view'),
		ftpls = [],
		categoryIds = $('#productForm input[name="prdCategories"]').val();
	
	if (categoryIds.length == 0) {
		productFeaturesMap = [];
		$('#pformFeature-view').html('<div class="ft-tip">未选择所属分类，或已选择的分类不存在特性设置模板</div>');
		return false;
	}
	ajaxRequest({
		url: base + 'prdCategory/getFeatures',
    	data: {
    		categoryIds: categoryIds
    	}
	}, function(data) {
		if (typeof data == 'string') {
			data = JSON.parse(data);
		}
		if (!data.success){
			layer.msg(data.message ? data.message : '保存失败！', {icon: 5});
		} else {
			var features = data.result,
				selfFs = features.featureTemplates, // 分类对应的特性模板
				parentFs = features.parentFeatureTemplates; //分类继承的特性模板
			
			// 分类特性模板和继承的特性模板存在重复，需要去重
			for (i = 0; i < selfFs.length; i++) {
				ftpls.push(selfFs[i]);
			}
			for (i = 0; i < parentFs.length; i++) {
				isRepeat = false;
				for (j = 0; j < selfFs.length; j++) {
					if (selfFs[j].id == parentFs[i].id) {
						isRepeat = true;
					}
				}
				if (!isRepeat) {
					ftpls.push(parentFs[i]);
				}
			}
			featureSetView.html('');
			laytpl(featureSetTpl).render(ftpls, function(html){
				featureSetView.html(html);
				for (i = 0; i < ftpls.length; i++) {
					var flId = 'feature-labels-' + ftpls[i].id,
						values = [];

					if (productFeaturesMap.length > 0) { // 产品特性配置信息集合不为空
						for (j = 0; j < productFeaturesMap.length; j++) {
   							if (productFeaturesMap[j].id == flId) { // 根据自定义的全局唯一的特性标签组id，获取对应特性配置信息
   								values = productFeaturesMap[j].prdFeatures;
   							}
   						}
					}
					if (values.length == 0) { // 编辑产品时赋值，编辑时产品特性信息初始化在productFeatures集合中
   						for (j = 0; j < productFeatures.length; j++) {
   							if (productFeatures[j].prdFeatureTemplateId == ftpls[i].id) { // 根据模板id，对应获取信息
   								values.push(productFeatures[j]);
   							}
   						}
					}
					// 将标签对象的固定信息保存在唯一id对应的dom元素上
					$('#' + flId).attr('prdFeatureTemplateId', ftpls[i].id); // 模板id
					$('#' + flId).attr('prdFeatureTemplateName', ftpls[i].name); // 模板名称
					
					Labels({
						id: flId,//保证标签生成器的id全局唯一
						min: 3,
						max: 10,
						valLen: 10,
						vals: values,
						isReverse: true
					},function(res, labelId){
						var tplId = $('#' + labelId).attr('prdFeatureTemplateId'),
							tplName = $('#' + labelId).attr('prdFeatureTemplateName'),
							submitArray = [];
						
						for (i = 0; i < res.length; i++) { // 如果用户有添加或者编辑多个标签信息
							// 将添加或者编辑的信息记录下来，拼装成约定提交的数据格式
							submitArray.push({
								prdFeatureTemplateId: tplId,
								prdFeatureTemplateName: tplName,
								productId: productId, //全局唯一
								value: res[i].name,
								id: res[i].id ? res[i].id : '',
								name: res[i].name
							})
						}
						if (res.length == 0) { // 如果用户删除了所有的标签信息
							// 约定传递空值到后台
							submitArray.push({
								prdFeatureTemplateId: tplId,
								prdFeatureTemplateName: tplName,
								productId: productId,
								value: '',
								id: '',
								name: ''
							})
						}
						// 替换掉该模板之前对应的配置信息
						for (i = 0; i < productFeaturesMap.length; i++) {
							if (productFeaturesMap[i].id == labelId) {
								productFeaturesMap.splice(i, 1);
							}
						}
						// 把最新的配置信息放入产品特性配置信息集合中
						productFeaturesMap.push({
							id: labelId,
							prdFeatures: submitArray
						})
  							
					});
				}
			});
		}
	});
}

/*** 产品标签库选择逻辑 begin ***/
/**
 * 生成产品标签添加器
 */
function initTagsLabel() {
	if (productTags >= 10) {
		$("#productForm .chosen-tags").hide();
	} 
	Labels({
		id: 'productTagsGenerate',
		max: 10,
		valLen: 10,
		vals: productTags
	},function(res, id, operObj){
		productTags = res;
		if (res.length >= 10) {
			$("#productForm .chosen-tags").hide();
			layer.msg('产品标签最多可添加10个', {icon: 6});
		} else {
			$("#productForm .chosen-tags").show();
		}
		
		if (operObj.flag == 'edit') {
			pro_prdTagSave({
				id: operObj.id,
				name: operObj.name
			}, function(res) {
				if (!res.success) {
					//保存失败
				}
			})
		}
	});
}
/**
 * 编辑或新增产品标签
 */
function pro_prdTagEdit(id, name) {
	layer.prompt({
		title: (name ? '编辑' : '新增') + '产品标签', 
		formType: 0,
		value: name
	}, function(val, index){
		val = val.replace(/\s/g, '');
		if (val.length == 0) {
			layer.tips('标签名称不能为空！', '.layui-layer-input', {time: 3000});
		} else  if (charLen(val) > 20) {
			layer.tips('标签名称过长，最多允许20个字符（1个中文=2个字符）', '.layui-layer-input', {time: 3000});
		} else {
			pro_prdTagSave({
				id: id ? id : '',
				name: val
			}, function(res) {
				if (!res.success) {
					layer.msg('保存失败！' + res.message ? '原因是' + res.message : '', {icon: 5});
				} else {
					layer.msg('保存成功！', {icon: 6});
				}
				pageTagsChosen(1, 20);
			})
			layer.close(index);
		}
	});
}
/**
 * 根据id删除产品标签
 */
function pro_prdTagDel_a(id) {
	pro_prdTagDel({
		id: id
	}, function(res) {
		if (!res.success) {
			layer.msg('删除失败！' + res.message ? '原因是' + res.message : '', {icon: 5});
		} else {
			productTagsSplice(id);
			layer.msg('删除成功！', {icon: 6});
			pageTagsChosen(1, 20);
		}
	})	
}
//保存产品标签
function pro_prdTagSave(p, callback) {
	ajaxRequest({
		url: base + 'prdTag/savePrdTag',
    	data: {
    		jsonStr: JSON.stringify(p)
    	}
	}, function(data) {
		if (typeof data == 'string') {
			data = JSON.parse(data);
		}
		if (typeof callback == 'function') {
			callback(data);
		}
	});
}
//删除产品标签
function pro_prdTagDel(p, callback) {
	ajaxRequest({
		url: base + 'prdTag/deletePrdTag',
    	data: {
    		ids: p.id
    	}
	}, function(data) {
		if (typeof data == 'string') {
			data = JSON.parse(data);
		}
		if (typeof callback == 'function') {
			callback(data);
		}
	});
}

function openTagsChosen() {
	tags = [];
	//默认加载产品标签库的第一页
	pageTagsChosen(1, 20);

	proPrdTagsChoosenShow();
	//显示产品标签库选择dom，并使用弹窗打开
	$('#tagsChosen').removeClass('hide');
	layer.open({
		type : 1,
		title : '产品标签库选择', 
		skin : 'layui-layer-rim', //加上边框
		area : [ '60%', '450px' ], //宽高
		content : $('#tagsChosen')
	});
}

/**
 * 分页加载标签库
 */
function pageTagsChosen(cur, limit) {
	var tagsChosenTpl = tagsChosenLstDatas.innerHTML,//数据模板
		tagsChosenView = $('#tagsChosenLst-view');
	
	ajaxRequest({
		url: base + 'prdTag/getPages',
    	data: {
    		limit: limit, // 默认每页展示10个
    		page: cur, 
    		keyword: $('#tagsChosenSearchForm input[name="keyword"]').val(), // 关键字
    		selectedLabels: tagsToSelectedNames() // 要传递用户已经选中了的标签id，在后台添加是否选中的逻辑字段，到页面控制呈现选中样式
    	}
	}, function(data) {
		if (typeof data == 'string') {
			data = JSON.parse(data);
		}
		if (!data.success){
			layer.msg(data.message ? data.message : '加载标签失败！', {icon: 5});
		} else {
			var result= data.result,
				totalPage = result.totalPageNum,
				cur = result.currentPage,
				totalCount = result.totalCount;
			
			tagsChosenView.html('');
			loading = layer.load();
			laytpl(tagsChosenTpl).render(result, function(html){
				tagsChosenView.html(html);
	    		layer.close(loading);
				$('#tagsChosenPage').addClass('hide');
				if(totalPage > 0) {
					$('#tagsChosenPage').removeClass('hide');
					pageList({
						modelName: 'tagsChosen',
						totalCount: totalCount,
						limit: limit,
						curr: cur
					}, function(obj) {
						pageTagsChosen(obj.curr, obj.limit);
					})
				}
				
			});
		}
	});
	
}

function productTagsSplice(tagId) {
	var index = -1;
	for (i = 0; i < productTags.length; i++) {
		if (productTags[i].id == tagId) {
			index = i;
		}
	}
	if (index >= 0) {
		productTags.splice(index, 1);
	}
}
function productTagsSpliceByName(tagName) {
	var index = -1;
	for (i = 0; i < productTags.length; i++) {
		if (productTags[i].name == tagName) {
			index = i;
		}
	}
	if (index >= 0) {
		productTags.splice(index, 1);
	}
}

function proPrdTagsChoosenShow() {
	var tpl = proprdTagsShowData.innerHTML,
		view = $('#proprdTagsShow-view');
	
	laytpl(tpl).render(productTags, function(html){
		view.html(html);
	});
}
/**
 * 控制标签库标签的选中和清除选中
 */
function toggleTclabelsSelect(self) {
	var val = {
			id: $(self).attr('data-id'),
			name: $(self).html()
		};
	
	// 清除选中，同时去除tags数组中push的标签对象
	if ($(self).hasClass('selected')) {
		productTagsSpliceByName(val.name);
		$(self).removeClass('selected');
	} else { // 添加选中，tags数组push标签对象
		if (productTags.length >= 10) {
			layer.msg('每个产品最多设置10个标签!', {icon: 5});
		} else {
			$(self).addClass('selected');
			productTags.push(val);
		}
	}
	if (productTags.length >= 10) {
		$('#productForm .chosen-tags').hide();
	} else {
		$('#productForm .chosen-tags').show();
	}
	
	proPrdTagsChoosenShow();
	initTagsLabel();// 重新渲染产品标签
}
/**
 * 将标签对象转换成标签id字符串
 * 分页加载产品标签库时要传递用户选中了的标签id串
 */
function tagsToSelectedNames() {
	var selectedNames = ",";
	
	for (i = 0; i < productTags.length; i++) {
		selectedNames += productTags[i].name + ",";
	}
	return selectedNames;
}
/**
 * 重置产品标签库选择
 */
function resetTagsChosen() {
	$('#tagsChosenSearchForm input[name="keyword"]').val('');
	pageTagsChosen(1, 20);
}

/*** 产品标签库选择逻辑 end***/