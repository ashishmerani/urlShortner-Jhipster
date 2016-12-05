(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListDetailController', UrlListDetailController);

    UrlListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'UrlList', 'User'];

    function UrlListDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, UrlList, User) {
        var vm = this;

        vm.urlList = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('urlShortnerApp:urlListUpdate', function(event, result) {
            vm.urlList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
