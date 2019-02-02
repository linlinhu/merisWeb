/**
 * 初始化分类树
 */
var eminZtree = function(params, callback) {
	var zTree = null,
		ztreeId = params.id ? params.id : null,
		sync = params.sync ? params.sync : {},
		async = params.async ? params.async : {},
		idKey = params.idKey ? params.idKey : 'id',
		pIdKey = params.pIdKey ? params.pIdKey : 'pId',
		diyDom = params.diyDom ? params.diyDom : null,
		checkList = params.checkList ? params.checkList : [],
		disabledIds = params.disabledIds ? params.disabledIds : [],
		chkDisabled = false,
		searchNodeList = [],
		searchNode = function(val) {
			updateNodeHighlight(false);
			searchNodeList = zTree.getNodesByParamFuzzy('name', val, null);
			if (val != "") {
				updateNodeHighlight(true);
			}
		},
		updateNodeHighlight = function(isHighlight) {
			for( var i=0, l=searchNodeList.length; i<l; i++) {
				searchNodeList[i].highlight = isHighlight;
				zTree.updateNode(searchNodeList[i]);
			}
		},
		expandNodes = function(nodes) {
			for (var i = 0; i < nodes.length; i++) {
	            zTree.expandNode(nodes[i], true); 
	            if (nodes[i].isParent && nodes[i].zAsync) {//存在子级
	            	expandNodes(nodes[i].children);//递归
	            }
			} 
		},
		setting = {
			view: {
				fontCss: function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {color:"#5a98de", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
				},
				addDiyDom: diyDom,
				nameIsHTML: true
			},
			data: {
				key: {
					title: "name"
				},
				simpleData: {
					enable: true,
					idKey: idKey,
					pIdKey: pIdKey
				}
			},
			callback: {
				onClick: function (event, treeId, treeNode, clickFlag) {
					callback(zTree, treeNode);
				},
				onCheck: function(event, treeId, treeNode, clickFlag) {
					callback(zTree, treeNode);
				},
				onAsyncSuccess: function (event, treeId, treeNode, msg) {
					var nodes = zTree.getNodes();
					if (nodes.length > 0) {
						if (async && async.expandAll) {
							expandNodes(nodes);
						}
					} else {
						var ztreeObj = $('#' + ztreeId);
						ztreeObj.html('<div style="padding: 7px;">暂无可用节点数据</div>');
					}
				}
			}
		};  

	
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			for (var j = 0; j < checkList.length; j++) {
				if (childNodes[i][idKey] == checkList[j][idKey]) {
					childNodes[i].checked = true;
				}
			}
			for (j = 0; j < disabledIds.length; j++) {
				if (parseInt(childNodes[i][idKey]) == parseInt(disabledIds[j])) {
					childNodes[i].chkDisabled = true;
				}
			}
		}
		return childNodes;
	}

	
	if (params.check) {
		chkDisabled = params.check.chkDisabled;
		setting.check = params.check;
	}
	
	if (ztreeId) { 
		var ztreeObj = $('#' + ztreeId),
			keywordObj = $('#' + ztreeId + 'Key');
		if (sync && sync.zNodes && sync.zNodes.length > 0) { // 同步所有数据
			$.fn.zTree.init(ztreeObj, setting, sync.zNodes);	
			zTree = $.fn.zTree.getZTreeObj(ztreeId);
			if (sync.expandAll) {
				zTree.expandAll(true);
			}
			
//			if (checkList.length == 1 && checkList[0][idKey]) {
//				var node = zTree.getNodeByParam(idKey, checkList[0][idKey], null);
//				if (!$('#' + node.tId + '_a').hasClass('curSelectedNode')) {
//					$('#' + node.tId + '_a').addClass('curSelectedNode');
//				}
//			}
			
			for(i = 0; i < checkList.length; i++) {
				if (checkList[i][idKey]) {
					var node = zTree.getNodeByParam(idKey, checkList[i][idKey], null);
					zTree.checkNode(node, true);
					if (!$('#' + node.tId + '_a').hasClass('curSelectedNode')) {
						$('#' + node.tId + '_a').addClass('curSelectedNode');
					}
				}
			}
			
			for(i = 0; i < disabledIds.length; i++) {
				if (disabledIds[i]) {
					var node = zTree.getNodeByParam(idKey, disabledIds[i], null);
					zTree.setChkDisabled(node, true);
				}
			}
			
		} else {
			ztreeObj.html('<div style="padding: 7px;">暂无可用节点数据</div>');
		}
		
		if (async && async.url) { // 异步加载数据
			setting.async = {
				enable: true,
				url: async.url,
				autoParam:["id=parentId"],
				dataFilter: filter	
			}
			
			$.fn.zTree.init(ztreeObj, setting);	
			zTree = $.fn.zTree.getZTreeObj(ztreeId);
		}
		
		if (keywordObj && keywordObj.length == 1) {
			keywordObj.on('input', function(){
				searchNode(keywordObj.val());
			}).on('propertychange', function(){
				searchNode(keywordObj.val());
			});
		}
	}
}


function setCurSelected (ztreeId, nodeId) {
	var selectedEls = $('#' + ztreeId + ' .curSelectedNode'),
		zTree = $.fn.zTree.getZTreeObj(ztreeId);
	
	if ($('#' + nodeId + '_a').hasClass()) {
		$('#' + nodeId + '_a').removeClass('curSelectedNode');
	} else {
		for (i = 0; i < selectedEls.length; i++) {
			$(selectedEls[i]).removeClass('curSelectedNode');
		}
		$('#' + nodeId + '_a').addClass('curSelectedNode');
	}
	
}


function concatNodeName(ztree, node, showTitle) {
	if (node.parentTId) {
		node = ztree.getNodeByTId(node.parentTId);
		showTitle = node.name + '&nbsp;&gt;&nbsp;' + showTitle;
		return concatNodeName(ztree, node, showTitle)
	} else {
		return showTitle;
	}
}

function treeDisplay (treeName) {
	var filter = treeName + 'Filter',
		wrap = treeName + 'Wrap',
		showTree = function() {
			var select = $('#' + filter);
			if (select.hasClass('user-defined')) {
				$('#' + wrap).slideDown("fast");
			} else {
				var selectOffset = select.position();
				$('#' + wrap).css({
					left: selectOffset.left + "px", 
					top: selectOffset.top + select.outerHeight() + "px",
					width: select.css('width')
				}).slideDown("fast");

			}
			$("body").bind("mousedown", onBodyDown);
		},
		hideTree = function() {
			$('#' + wrap).fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		},
		onBodyDown = function(event) {
			if (!(event.target.id == wrap  || event.target.id == filter || $(event.target).parents('#' + wrap).length>0)) {
				hideTree();
			}
		};
		
	$('#' + filter).bind('click', showTree);
}
