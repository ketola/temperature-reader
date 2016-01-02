
angular.module('temperatureApp', [])
  .controller('TemperatureController', ['$scope',
                                        function ($scope) {
	  var source = new EventSource('/latest/sse');

      source.onmessage = function (event) {
          $scope.openListingsReport = event.data;
          $scope.$apply();
          console.log($scope.openListingsReport);
      };
  }]);

