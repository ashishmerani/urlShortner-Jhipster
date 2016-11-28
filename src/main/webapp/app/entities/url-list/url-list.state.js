(function() {
    'use strict';

    angular
        .module('urlShortnerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('url-list', {
            parent: 'entity',
            url: '/url-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UrlLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/url-list/url-lists.html',
                    controller: 'UrlListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('url-list-detail', {
            parent: 'entity',
            url: '/url-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UrlList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/url-list/url-list-detail.html',
                    controller: 'UrlListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UrlList', function($stateParams, UrlList) {
                    return UrlList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'url-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('url-list-detail.edit', {
            parent: 'url-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/url-list/url-list-dialog.html',
                    controller: 'UrlListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UrlList', function(UrlList) {
                            return UrlList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('url-list.new', {
            parent: 'url-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/url-list/url-list-dialog.html',
                    controller: 'UrlListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                longUrl: null,
                                shortUrl: null,
                                visitCount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('url-list', null, { reload: 'url-list' });
                }, function() {
                    $state.go('url-list');
                });
            }]
        })
        .state('url-list.edit', {
            parent: 'url-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/url-list/url-list-dialog.html',
                    controller: 'UrlListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UrlList', function(UrlList) {
                            return UrlList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('url-list', null, { reload: 'url-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('url-list.delete', {
            parent: 'url-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/url-list/url-list-delete-dialog.html',
                    controller: 'UrlListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UrlList', function(UrlList) {
                            return UrlList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('url-list', null, { reload: 'url-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
