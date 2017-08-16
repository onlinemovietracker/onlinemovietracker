(function () {
    angular.module('app')
        .controller('AuthenticationController', AuthenticationController);

    AuthenticationController.$inject = ['$location', '$http', '$route', 'AuthenticationService'];

    function AuthenticationController($location, $http, $route, AuthenticationService) {

        var self = this;

        self.fireLogin = fireLogin;
        self.logout = logout;
        self.login = login;
        self.register = register;

        self.registerInput = {};
        self.loginCredentials = {};

        self.errors = {};
        self.errors.register = '';
        self.registerForm = {};


        self.user;
        
        function init() {
            $('#auth-modal').modal('hide');
        }

        function fireLogin() {
            $('#auth-modal').modal('show');
        }

        function login() {
            // creating base64 encoded String from username and password
            var base64Credential = btoa(self.loginCredentials.username + ':' + self.loginCredentials.password);

            // calling GET request for getting the user details
            $http.get('users/user', {
                headers: {
                    // setting the Authorization Header
                    'Authorization': 'Basic ' + base64Credential
                }
            }).success(function (res) {
                self.loginCredentials.password = null;
                self.message = '';
                // setting the same header value for all request calling from this app
                $http.defaults.headers.common['Authorization'] = 'Basic ' + base64Credential;
                self.user = res;
                init();
            }).error(function (error) {
                self.error = 'Bad credentials!';
            });
        }

        function logout() {
            $http.defaults.headers.common['Authorization'] = null;
            delete self.user;
            $location.url("/");
        }

        function register(user) {
            user.roles = [{
                "id" : 1
            }];
            AuthenticationService.saveUser(user).then(function(response) {
                init();
                console.log("registered");
            }, function(error) {
                console.log(error)

            });
        }
    }
})();