(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListController', UrlListController);

    UrlListController.$inject = ['$scope', '$state', 'DataUtils', 'UrlList'];

    function UrlListController ($scope, $state, DataUtils, UrlList) {
        var vm = this;

        vm.urlLists = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            UrlList.query(function(result) {
                vm.urlLists = result;
            });
        }
    }
})();
