(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListDialogController', UrlListDialogController);

    UrlListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UrlList', 'User'];

    function UrlListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UrlList, User) {
        var vm = this;

        vm.urlList = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.urlList.id !== null) {
                UrlList.update(vm.urlList, onSaveSuccess, onSaveError);
            } else {
                UrlList.save(vm.urlList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('urlShortnerApp:urlListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
