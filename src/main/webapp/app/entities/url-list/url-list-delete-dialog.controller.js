(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListDeleteController',UrlListDeleteController);

    UrlListDeleteController.$inject = ['$uibModalInstance', 'entity', 'UrlList'];

    function UrlListDeleteController($uibModalInstance, entity, UrlList) {
        var vm = this;

        vm.urlList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UrlList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
