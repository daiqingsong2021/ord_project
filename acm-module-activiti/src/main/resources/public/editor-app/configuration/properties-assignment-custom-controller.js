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

/*
 * Assignment
 */
var KisBpmAssignmentCustomCtrl = [ '$scope', '$modal', function($scope, $modal) {
    // Config for the modal window
    var opts = {
        template :  'editor-app/configuration/properties/assignment-popup-custom.html?version=' + Date.now(),
        backdrop : false,
        keyboard : false,
        scope : $scope
    };
    // Open the dialog
    $modal(opts);
}];

var KisBpmAssignmentPopupCustomCtrl = [ '$scope', '$http', function($scope, $http) {
    //参数初始化
    $scope.findContent = ""; //Grid 筛选
    $scope.gridData = [];//表格数据
    $scope.gridedData = [];//表格数据
    $scope.selectTitle = '选择候选人';
    $scope.selectedTitle = '已选候选人';
    $scope.columnData = [];//表格列数据
    $scope.columnDataName = 'columnData';
    $scope.selectType = 1;//0-代理人，1-候选人，2-候选组
    $scope.totalServerItems = 0;//表格总条数
    $scope.totalServeredItems = 0;//表格总条数
    $scope.assignment = {assignee : '', candidateUsers : [], candidateGroups : []}; //
    $scope.candidates = {users : [], groups : []};//表格数据
    $scope.selectedGridRows = [];
    $scope.selectedGridedRows = [];
    //分页初始化
    $scope.pagingOptions = {
        pageSizes : [10, 20, 30, 40, 50],//page 行数可选值
        pageSize : 10, //每页行数
        currentPage : 1, //当前显示页数
    };
    //分页初始化
    $scope.pagingedOptions = {
        pageSizes : [10, 20, 30, 40, 50],//page 行数可选值
        pageSize : 10, //每页行数
        currentPage : 1, //当前显示页数
    };

    //选择用户时表头
    $scope.userColumns = [{
        field : 'id',
        displayName : '用户Id',
        minWidth : 100,
        width : '15%'
    },{
        field : 'loginId',
        displayName : '登录名',
        minWidth : 100,
        width : '25%'
    },{
        field : 'userName',
        displayName : '姓名',
        minWidth : 100,
        width : '25%'
    },{
        field : 'orgName',
        displayName : '部门',
        minWidth : 100,
        width : '32%'
    }];

    //选择用户组时表头
    $scope.groupColumns = [{
        field : 'id',
        displayName : '角色Id',
        minWidth : 100,
        width : '20%'
    }, {
        field : 'code',
        displayName : '角色代码',
        minWidth : 100,
        width : '35%'
    }, {
        field : 'name',
        displayName : '角色名称',
        minWidth : 100,
        width : '42%'
    }];

    //异步请求表格数据
    var getCandidateByIds = function(type){
        var ids = [];
        var candidates = [];
        var assignment = $scope.property.value.assignment || {};
        if(type == 'user' && assignment.candidateUsers){
            candidates = $scope.property.value.assignment.candidateUsers;
        }else if(type == 'group' && assignment.candidateGroups){
            candidates = $scope.property.value.assignment.candidateGroups;
        }
        if(candidates && candidates.length > 0){
            for (var i = 0; i < candidates.length; i++){
                ids.push(candidates[i].value);
            }
        }
        return ids;
    };

    //异步请求表格数据
    var getCandidateAsync = function(){
        var ids = [];
        var groupIds = getCandidateByIds('group').toString();
        var userIds = getCandidateByIds('user').toString();
        $http({
            method : 'POST',
            url : ACTIVITI.CONFIG.contextRoot + '/candidate/getCandidateByIds',
            params :{
                'groupIds': groupIds,
                'userIds': userIds
            },
       }).then(function successCallback(response) {
            $scope.candidates.groups = response.data.groups;
            $scope.candidates.users = response.data.users;
            $scope.getPagedCandidate();
       }, function errorCallback(response) {
           // 请求失败执行代码
       });
    };
    getCandidateAsync();

    //得到已选中表格数据
    $scope.getPagedCandidate = function(){
        if($scope.selectType == 2){
            $scope.gridedData = $scope.candidates.groups;
            $scope.totalServeredItems = $scope.candidates.groups.length;
        }else{
            $scope.gridedData = $scope.candidates.users;
            $scope.totalServeredItems = $scope.candidates.users.length;
        }
    }

    //数据监视
    $scope.dataWatch = function (){
        //分页数据监视
        $scope.$watch('pagingOptions', function (newValue, oldValue) {
            if(newValue != oldValue) {
                $scope.getPagedDataAsync();
            }
        }, true);

        //分页数据监视
        $scope.$watch('pagingedOptions', function (newValue, oldValue) {
            if(newValue != oldValue) {
                $scope.getPagedCandidate();
            }
        }, true);

        //当切换类型时，初始化当前页
        $scope.$watch('selectType', function (newValue, oldValue) {
            if(newValue != oldValue){
                $scope.pagingOptions.currentPage = 1;
                $scope.pagingedOptions.currentPage = 1;
                $scope.getPagedDataAsync();
                $scope.getPagedCandidate();
            }
        },true);

        //查询
        $scope.find = function(){
            $scope.pagingOptions.currentPage = 1;
            $scope.getPagedDataAsync();
        };
    };
    $scope.dataWatch();

    //异步请求表格数据
    $scope.getPagedDataAsync = function(){
        var url = null;
        if($scope.selectType == 2){
            url = '/candidate/getGroupList';
            $scope.columnData = $scope.groupColumns;
        }else{
            url = '/candidate/getUserList';
            $scope.columnData = $scope.userColumns;
        }
        $http({
            method: 'POST',
            url: ACTIVITI.CONFIG.contextRoot + url,
            params:{
                'pageNum': $scope.pagingOptions.currentPage,
                'pageSize': $scope.pagingOptions.pageSize,
                'findContent': $scope.findContent
            },
        }).then(function successCallback(response) {
            $scope.gridData = response.data.rows;
            $scope.totalServerItems = response.data.total;
        }, function errorCallback(response) {
            // 请求失败执行代码
            $scope.gridData = [];
            $scope.totalServerItems = 0;
        });
    }

    //表格属性配置
    $scope.gridOptions = {
        primaryKey : "id",
        data : "gridData",
        multiSelect : false,//不可多选
        enablePaging : true, //是否分页
        pagingOptions : $scope.pagingOptions, //分页参数
        totalServerItems : 'totalServerItems',
        i18n : 'zh-cn',
        showFooter : true,
        showSelectionCheckbox : false,
        selectWithCheckboxOnly : false, //只能选中checkbox选中行
        columnDefs : $scope.columnDataName,
        selectedItems : $scope.selectedGridRows, //选中行数据
        beforeSelectionChange : function (event) {
            if($scope.selectType == 1 && !arrayContain($scope.candidates.users, event.entity.id)){//候选人
                $scope.candidates.users.push(event.entity);
            }else if($scope.selectType == 2 && !arrayContain($scope.candidates.groups, event.entity.id)){//候选组
                $scope.candidates.groups.push(event.entity);
            }
            $scope.totalServeredItems = $scope.gridedData.length;
            return true;
        }
    };
    $scope.find(); //初始化查询

    //表格属性配置
    $scope.gridedOptions = {
        primaryKey : "id",
        data : "gridedData",
        multiSelect : false,//不可多选
        enablePaging : true, //是否分页
        pagingOptions : $scope.pagingedOptions, //分页参数
        totalServerItems : 'totalServeredItems',
        i18n : 'zh-cn',
        showFooter : true,
        showSelectionCheckbox : false,
        selectWithCheckboxOnly : false, //只能选中checkbox选中行
        columnDefs : $scope.columnDataName,
        selectedItems : $scope.selectedGridedRows, //选中行数据
        beforeSelectionChange : function (event) {
            var index = event.rowIndex;
            $scope.gridedOptions.selectItem(index, false);
            $scope.gridedData.splice(index, 1);
            return true;
        }
    };

    //代理人(审批人)
    $scope.selectAssignee = function () {
        //$scope.selectType = 0;
        //$scope.selectTitle = '选择代理人';
        //$scope.selectedTitle = '已选代理人';
    };

    //候选人
    $scope.selectCandidate = function () {
        $scope.selectType = 1;
        $scope.selectTitle = '选择候选人';
        $scope.selectedTitle = '已选候选人';
    };

    //候选组
    $scope.selectGroup = function () {
        $scope.selectType = 2;
        $scope.selectTitle = '选择候选组';
        $scope.selectedTitle = '已选候选组';

        var url =  '/activitiTemplate/getActivitiTemplate';
        var resourceId = $scope.selectedShape.resourceId;
        $http({
            method: 'GET',
            url:  url,
            params:{
                'activitiId': resourceId,
            },
        }).then(function successCallback(response) {
            $scope.sccanCode = response.data.data;
        }, function errorCallback(response) {
        });
    };

    //保存
    $scope.save = function() {
        for(var i = 0; i < $scope.candidates.groups.length; i++){
            var obj = {name : $scope.candidates.groups[i].name, value : $scope.candidates.groups[i].id};
            $scope.assignment.candidateGroups.push(obj);
        }
        for(var i = 0; i < $scope.candidates.users.length; i++){
            var obj = {name : $scope.candidates.users[i].userName, value : $scope.candidates.users[i].id};
            $scope.assignment.candidateUsers.push(obj);
        }
        $scope.property.value = {};
        $scope.property.value.assignment = $scope.assignment;
        $scope.updatePropertyInModel($scope.property);

        $scope.savesTemplate();
        $scope.close();
    };
    $scope.savesTemplate = function(){

        var url =  '/activitiTemplate/addActivitiTemplate';
       //var id = $scope.selectedShape.id;
       var resourceId = $scope.selectedShape.resourceId;
       var code = document.getElementById("xuanze_template").value;

    $http({
        method: 'POST',
        url:  url,
        data:{
            'activitiId': resourceId,
            'screenCode': code
        },
    }).then(function successCallback(response) {
        $scope.gridData = response.data.rows;
        $scope.totalServerItems = response.data.total;
    }, function errorCallback(response) {
        // 请求失败执行代码
        $scope.gridData = [];
        $scope.totalServerItems = 0;
    });
};

    //关闭
    $scope.close = function() {
        $scope.property.mode = 'read';
        $scope.$hide();
    };

    //声明----如果有此 contains 直接用最好
    var arrayContain = function(array, id) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].id == id){ //如果要求数据类型也一致，这里可使用恒等号===
                return true;
            }
        }
        return false;
    };
}];
