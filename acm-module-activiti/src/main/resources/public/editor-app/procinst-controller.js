/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
'use strict';

angular.module('activitiModeler')
    .controller('ProcInstController', ['$rootScope', '$scope', '$http', '$timeout', function ($rootScope, $scope, $http, $timeout) {

        $timeout(function () {
            updateProcInstStatus(); //修改流程活实例状态
        });

		//修改流程活动实例状态
		var yc = 120/*秒*/ * 2;
		var updateProcInstStatus = function(){
			setTimeout(function(){
				if($rootScope.modelData) {
                    if ($rootScope.modelData.activityInstances && $rootScope.modelData.flows) {
						var svgs = jQuery(".ORYX_Editor").find("svg");
						var actdefs = svgs.find("g[class='children']").children(); //活动节点
						var edges = svgs.find("g[class='edge']").children(); //连接线
						if (actdefs.length > 0 && edges.length > 0) {
							_updateProcInstStatus(svgs.eq(0), $rootScope.modelData);
                            _updateActTitle(svgs.eq(0), $rootScope.modelData);
						} else if (yc > 0) {
							updateProcInstStatus();
							yc--;
						}
					}
				} else if(yc > 0){
					updateProcInstStatus();
					yc--;
				}
			}, 500);
		};

		//修改流程活动实例状态
		var _updateProcInstStatus = function(svg, modelData){
			var colour = "#4e9bf6";
			var actInss= {}; //按map整合已经走的过活动
			var resources= {}; //按map整合流程所有资源
			if(modelData.activityInstances){
                for(var i = 0; i < modelData.activityInstances.length; i++){
                    var actInst = modelData.activityInstances[i];
                    actInss[actInst.activityId] = actInst;
                }
            }
			for(var i = 0; i < modelData.model.childShapes.length; i++){
				var resource = modelData.model.childShapes[i];
				resources[resource.resourceId] = resource;
			}
			_updateProcInstActStatus(svg, modelData, actInss, resources, colour); //修改活动定义的样式式
			_updateProcInstActSequenceFlowssStatus(svg, modelData.flows, colour); //修改连接线的样式
		}

		//修改流程活动实例状态
		var _updateProcInstActStatus = function(svg, modelData, actInss, resources, colour){
			for(var i = 0; i < modelData.model.childShapes.length; i++) {
				var resource = modelData.model.childShapes[i];
				var actInst = actInss[resource.resourceId]; //找到对应的活动实例
				if (actInst) {
                    var act = svg.find("#svg-" + resource.resourceId); //活动对象
					if (act) {
						if (resource.stencil.id == "StartNoneEvent" || resource.stencil.id == "EndNoneEvent") { //如果为开始结点
                            var node = act.find("circle[stroke='#bbbbbb']");
							node.attr("stroke", colour);
						} else if (resource.stencil.id == "UserTask") { //人工活动
                            var node = act.find("rect[stroke='#bbbbbb']");
							node.attr("stroke", colour);
							node = act.find("path[style='fill:#bbbbbb;stroke:none;']"); //人工活动图标
							node.attr("style", "fill:" + colour + ";stroke:none;");
						} else if (resource.stencil.id == "ExclusiveGateway" || resource.stencil.id == "ParallelGateway" || resource.stencil.id == "InclusiveGateway") { //网关
                            var node = act.find("path[stroke='#bbbbbb']");
                            node.eq(0).attr("stroke", colour);
                        }
						//_updateProcInstActSequenceStatus(svg, actInss, resources, resource.outgoing, colour); //修改流程活动实例连接线状态(通过活动定义查找，有一定的缺陷）
					}
				}
			}
		}

		//修改流程活动实例连接线状态(通过活动定义查找，有一定的缺陷）
		var _updateProcInstActSequenceStatus = function(svg, actInss, resources, outgoing, colour) {
			var sequence = null;
			var node = null;
			for (var j = 0; j < outgoing.length > 0; j++) {
				var sequenceFlow = resources[outgoing[j].resourceId]
				if (sequenceFlow && actInss[sequenceFlow.target.resourceId]) {
					sequence = svg.find("#svg-" + sequenceFlow.resourceId); //连接线
					_updateProcInstActSequenceFlowsStatus(svg, sequenceFlow.resourceId, colour);
				}
			}
		}

        //修改流程活动实例连接线状态
        var _updateProcInstActSequenceFlowssStatus = function(svg, flows, colour) {
            for (var j = 0; j < flows.length > 0; j++) {
                _updateProcInstActSequenceFlowsStatus(svg, flows[j], colour);
            }
        }

		//修改流程活动实例连接线状态
		var _updateProcInstActSequenceFlowsStatus = function(svg, resourceId, colour) {
			var sequence = svg.find("#svg-" + resourceId); //连接线
			var node = sequence.find("path[stroke='#bbbbbb']");
			node.attr("stroke", colour);
			var id = node.attr("id");
			if (id) {
				id = id.split("_")[0] + "arrowhead";
				node = svg.find("#" + id);
				node.attr("stroke", colour).attr("fill", colour);
			}
		}

        //修改流程活动提示
        var _updateActTitle = function(svg, modelData){
            for(var i in modelData.activityTitles) {
                var title = modelData.activityTitles[i];
				var act = svg.find("#svg-" + i +" title"); //活动对象
				if (act) {
                    act.text(title);
				}
            }
        }
}]);