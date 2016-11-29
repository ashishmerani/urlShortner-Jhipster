(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListDialogController', UrlListDialogController);

    UrlListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'UrlList'];

    function UrlListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, UrlList) {
        var vm = this;

        vm.urlList = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.urlList.id !== null) {
            	vm.urlList.visitCount = 0;
            	vm.urlList.shortUrl = "sample";
            	
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
