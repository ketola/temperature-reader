
angular.module('temperatureApp', [])
  .controller('TemperatureController', ['$scope', '$http',
                                        function ($scope, $http) {
	  $scope.temperatureInside = {value: 0};
	  $scope.temperatureOutside = {value: 0};
	  
	  $http.get('/latest/outside').
      success(function(data) {
          $scope.temperatureOutside = data;
      });
	  
	  $http.get('/latest/inside').
      success(function(data) {
          $scope.temperatureInside = data;
      });
	  
	  console.log('Inside: ' + $scope.temperatureInside + ' ' + $scope.temperatureInside.value);
	  console.log('Outside: ' + $scope.temperatureOutside + ' ' + $scope.temperatureOutside.value);
	  
	  var latestTemperatureSource = new EventSource('/latest/sse');

	  latestTemperatureSource.onmessage = function (event) {
          $scope.latestTemperature = JSON.parse(event.data);
          
          
          
          if($scope.latestTemperature.source === "INSIDE"){
        	  $scope.temperatureInside = $scope.latestTemperature;
          } else {
        	  $scope.temperatureOutside = $scope.latestTemperature;
          }
          $scope.$apply();
          console.log($scope.latestTemperature + ' ' + $scope.latestTemperature.value);
      };
  }]);

