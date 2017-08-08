(function () {
    angular.module('app')
        .config(config);

    config.$inject = ['$routeProvider'];

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: '/views/home.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            })
            .when('/movie-details', {
                templateUrl: '/views/movie-details.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            })
            .otherwise('/');
    }
})();