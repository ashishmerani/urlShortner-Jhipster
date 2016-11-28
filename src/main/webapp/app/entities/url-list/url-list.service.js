(function() {
    'use strict';
    angular
        .module('urlShortnerApp')
        .factory('UrlList', UrlList);

    UrlList.$inject = ['$resource'];

    function UrlList ($resource) {
        var resourceUrl =  'api/url-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
