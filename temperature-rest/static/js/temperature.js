
var temperatureApp = angular.module('temperatureApp', ['highcharts-ng'])
  .controller('TemperatureController', ['$scope', '$http',
                                        function ($scope, $http) {
	  
	  var createChartConfig = function(){
		  return {
	  	        options: {
	  	            chart: {
	  	                type: 'solidgauge'
	  	            },
	  	            pane: {
	  	                center: ['50%', '85%'],
	  	                size: '100%',
	  	                startAngle: -90,
	  	                endAngle: 90,
	  	                background: {
	  	                    backgroundColor:'#EEE',
	  	                    innerRadius: '60%',
	  	                    outerRadius: '100%',
	  	                    shape: 'arc'
	  	                }
	  	            },
		  	        tooltip: {
		  	            enabled: false
		  	        }
	  	            
	  	        },
	  	        series: [{
	  	            data: [35]
	  	        }],
	  	        title: {
	  	            text: '',
	  	            y: 0
	  	        },
	  	        yAxis: {
	  	            currentMin: 0,
	  	            currentMax: 35,
	  	                
	  				
	  				lineWidth: 0,
	  				minorTickInterval: null,
	  	            tickPixelInterval: 0,
	  	            tickWidth: 0,
	  	            
	  	            labels: {
	  	                y: 16
	  	            }   
	  	        },
	  	        loading: false,
	  	        plotOptions: {
	              solidgauge: {
	                  dataLabels: {
	                      y: 5,
	                      borderWidth: 0,
	                      useHTML: true
	                  }
	              }
	  	        }
	  	    };
	  };
	  
	  $scope.chartConfigInside = createChartConfig();
	  $scope.chartConfigOutside = createChartConfig();
	  
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
	  
	  var latestTemperatureSource = new EventSource('/latest/sse');

	  latestTemperatureSource.onmessage = function (event) {
          $scope.latestTemperature = JSON.parse(event.data);
          
          if($scope.latestTemperature.source === "INSIDE"){
        	  $scope.temperatureInside = $scope.latestTemperature;
              
              var chart = $scope.chartConfigInside.getHighcharts();
              var point = chart.series[0].points[0];
              point.update($scope.temperatureInside.value);
          } else {
        	  $scope.temperatureOutside = $scope.latestTemperature;
        	  
        	  var chart = $scope.chartConfigOutside.getHighcharts();
              var point = chart.series[0].points[0];
              point.update($scope.temperatureOutside.value);
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
