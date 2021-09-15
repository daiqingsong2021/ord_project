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
    .controller('StencilCustomController', ['$rootScope', '$scope', '$http', '$modal', '$timeout', function ($rootScope, $scope, $http, $modal, $timeout) {

        // Property window toggle state
        $scope.propertyWindowState = {'collapsed': false};

        // Add reference to global header-config
        $scope.headerConfig = KISBPM.HEADER_CONFIG;

        $scope.propertyWindowState.toggle = function () {
            $scope.propertyWindowState.collapsed = !$scope.propertyWindowState.collapsed;
            $timeout(function () {
                jQuery(window).trigger('resize');
            });
        };
        $timeout(function () {
            jQuery(".canvas-wrapper").height($rootScope.window.height);
        });
    }]);
