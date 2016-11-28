(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListController', UrlListController);

    UrlListController.$inject = ['$scope', '$state', 'UrlList'];

    function UrlListController ($scope, $state, UrlList) {
        var vm = this;

        vm.urlLists = [];

        loadAll();

        function loadAll() {
            UrlList.query(function(result) {
                vm.urlLists = result;
            });
        }
    }
})();
