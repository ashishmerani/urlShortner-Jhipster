(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .controller('UrlListDialogController', UrlListDialogController);

    UrlListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'UrlList', 'User'];

    function UrlListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, UrlList, User) {
        var vm = this;

        vm.urlList = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            console.log(vm.urlList.id);
            if (vm.urlList.id !== null) {
            	console.log("beforee");
            	vm.urlList.visitCount = 0;
            	console.log("after count");
            	vm.urlList.shortUrl = "sampleshortUrl";
            	console.log("after url");
            	vm.urlList.user = "sampleUser";
            	console.log("after user");
            	
                UrlList.update(vm.urlList, onSaveSuccess, onSaveError);
                console.log("after save");
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
