(function () {
    angular.module("app")
        .factory('SearchService', SearchService);

    SearchService.$inject = ['$http', '$q'];

    function SearchService($http, $q) {

        var service = {
            getMovies: getMovies,
            getTvShows: getTvShows
        };

        return service;

        var moviesList = [];
        var tvShowsList = [];

        function getMovies() {
            var def = $q.defer();

            var req = {
                method: 'GET',
                url: "movies"
            };

            return $http(req)
                .success(function (response) {
                    return moviesList = response.data;
                })
                .error(function () {
                    return def.reject("Failed to get movies");
                });

            return def.promise;
        }

        function getTvShows() {
            var def = $q.defer();

            var req = {
                method: 'GET',
                url: "tvshows"
            };

            return $http(req)
                .success(function (response) {
                    return tvShowsList = response.data;
                })
                .error(function () {
                    return def.reject("Failed to get tvshows");
                });

            return def.promise;
        }
    }
})();