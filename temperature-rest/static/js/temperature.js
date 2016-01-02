
var temperatureApp = angular.module('temperatureApp', ['highcharts-ng'])
  .controller('TemperatureController', ['$scope', '$http',
                                        function ($scope, $http) {
	  
	  
	  $scope.chartConfigInside = {
  	        options: {
  	            chart: {
  	                type: 'solidgauge'
  	            },
  	            pane: {
  	                center: ['50%', '85%'],
  	                size: '150%',
  	                startAngle: -90,
  	                endAngle: 90,
  	                background: {
  	                    backgroundColor:'#EEE',
  	                    innerRadius: '60%',
  	                    outerRadius: '100%',
  	                    shape: 'arc'
  	                }
  	            },
  	            solidgauge: {
  	                dataLabels: {
  	                    y: -30,
  	                    borderWidth: 0,
  	                    useHTML: true
  	                }
  	            }
  	        },
  	        series: [{
  	            data: [0]
  	        }],
  	        title: {
  	            text: '',
  	            y: 0
  	        },
  	        yAxis: {
  	            currentMin: 0,
  	            currentMax: 35,
  	            title: {
  	                y: 140
  	            },      
  				stops: [
  	                [0.1, '#DF5353'], // red
  		        	[0.5, '#DDDF0D'], // yellow
  		        	[0.9, '#55BF3B'] // green
  				],
  				lineWidth: 0,
  	            tickInterval: 20,
  	            tickPixelInterval: 400,
  	            tickWidth: 0,
  	            labels: {
  	                y: 15
  	            }   
  	        },
  	        loading: false
  	    }
	  
	  $scope.chartConfigOutside = {
	  	        options: {
	  	            chart: {
	  	                type: 'solidgauge'
	  	            },
	  	            pane: {
	  	                center: ['50%', '85%'],
	  	                size: '150%',
	  	                startAngle: -90,
	  	                endAngle: 90,
	  	                background: {
	  	                    backgroundColor:'#EEE',
	  	                    innerRadius: '60%',
	  	                    outerRadius: '100%',
	  	                    shape: 'arc'
	  	                }
	  	            },
	  	            solidgauge: {
	  	                dataLabels: null
	  	            }
	  	        },
	  	        series: [{
	  	            data: [0]
	  	        }],
	  	        title: {
	  	            text: '',
	  	            y: 0
	  	        },
	  	        yAxis: {
	  	            currentMin: 0,
	  	            currentMax: 35,
	  	            title: {
	  	                y: 140
	  	            },      
	  				stops: [
	  	                [0.1, '#DF5353'], // red
	  		        	[0.5, '#DDDF0D'], // yellow
	  		        	[0.9, '#55BF3B'] // green
	  				],
	  				lineWidth: 0,
	  	            tickInterval: 20,
	  	            tickPixelInterval: 400,
	  	            tickWidth: 0,
	  	            labels: {
	  	                y: 15
	  	            }   
	  	        },
	  	        loading: false
	  	    }
	  
	  $scope.temperatureInside = {value: 0};
	  $scope.temperatureOutside = {value: 0};
	  
	  $http.get('/latest/outside').
      success(function(data) {
          $scope.temperatureOutside = data;
          $scope.chartConfigOutside.series.pop();
          $scope.chartConfigOutside.series.push({
              data: [data.value]
          });
      });
	  
	  $http.get('/latest/inside').
      success(function(data) {
          $scope.temperatureInside = data;
          $scope.chartConfigInside.series.pop();
          $scope.chartConfigInside.series.push({
              data: [data.value]
          });
      });
	  
	  console.log('Inside: ' + $scope.temperatureInside + ' ' + $scope.temperatureInside.value);
	  console.log('Outside: ' + $scope.temperatureOutside + ' ' + $scope.temperatureOutside.value);
	  
	  var latestTemperatureSource = new EventSource('/latest/sse');

	  latestTemperatureSource.onmessage = function (event) {
          $scope.latestTemperature = JSON.parse(event.data);
          
          
          if($scope.latestTemperature.source === "INSIDE"){
        	  $scope.temperatureInside = $scope.latestTemperature;
        	  $scope.chartConfigInside.series.pop();
              $scope.chartConfigInside.series.push({
                  data: [$scope.latestTemperature.value]
              });
          } else {
        	  $scope.temperatureOutside = $scope.latestTemperature;
        	  $scope.chartConfigOutside.series.pop();
              $scope.chartConfigOutside.series.push({
                  data: [$scope.latestTemperature.value]
              });
          }
          $scope.$apply();
          console.log($scope.latestTemperature + ' ' + $scope.latestTemperature.value);
          
      };
      
     
      
  }]);

temperatureApp.config(['highchartsNGProvider', function (highchartsNGProvider) {
    highchartsNGProvider.lazyLoad();// will load hightcharts (and standalone framework if jquery is not present) from code.hightcharts.com

    //highchartsNGProvider.lazyLoad([highchartsNGProvider.HIGHCHART/HIGHSTOCK, "maps/modules/map.js", "mapdata/custom/world.js"]);// you may add any additional modules and they will be loaded in the same sequence

    //highchartsNGProvider.basePath("/js/"); // change base path for scripts, default is http(s)://code.highcharts.com/

  }]);
