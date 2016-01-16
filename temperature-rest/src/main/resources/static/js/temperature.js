
var temperatureApp = angular.module('temperatureApp', ['chart.js'])
  .controller('TemperatureController', ['$scope', '$http',
                                        function ($scope, $http) {
	  
	  $scope.labelsOutside = [];
      $scope.seriesOutside = ['Outside'];
      $scope.dataOutside = [[0]];
      
      $scope.labelsInside = [];
      $scope.seriesInside = ['Inside'];
      $scope.dataInside = [[0]];
      
	  
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
	  
	  $http.get('/history/outside/values-and-labels').
      success(function(data) {
          $scope.dataOutside[0] = data.values;
          $scope.labelsOutside = data.labels;
      });
	  
	  $http.get('/history/inside/values-and-labels').
      success(function(data) {
          $scope.dataInside[0] = data.values;
          $scope.labelsInside = data.labels;
      });
	  
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
