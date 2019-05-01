<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

	<div class="px-content">	
		<div class="row">
			<div class="col-md-6">
				<div class="panel">
					<div class="panel-title">매출 현황</div>
					 <hr>
				
					<div class="panel-body">
					  	<div id="flot-graph" style="height: 250px"></div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel">
					<div class="panel-title">당일 주문량</div>
					<hr>
					
					<div class="panel-body">
						<div id="chartist-bars" style="height: 250px"></div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel">
					<div class="panel-title">업체 판매 비율</div>
					<hr>
					
					<div class="panel-body">
						<div id="flot-pie" style="height: 250px"></div>
					</div>
				</div>
			</div>
		</div>	
	</div>


<script type="text/javascript">
$(function() {
    var data = [
      {
        label: '항공대학교',
        data: [
          [6, 152000], [7, 123000], [8, 142000], [9, 154000], [10, 178000], [11, 185000], [12, 124000], [13, 179000], [14, 233000], [15, 205000]
        ],
      },
      {
        label: '군산대학교',
        data: [
          [6, 13000], [7, 22000], [8, 46000], [9, 45000], [10, 74000], [11, 56000], [12, 34000], [13, 76000], [14, 54000], [15, 100000]
        ],
      }
    ];

    $.plot($('#flot-graph'), data, {
      series: {
        shadowSize: 0,
        lines: {
          show: true,
        },
        points: {
          show:   true,
          radius: 4,
        },
      },

      grid: {
        color:       '#999',
        borderColor: 'rgba(255, 255, 255, 0)',
        borderWidth: 1,
        hoverable:   true,
        clickable:   true,
      },

      xaxis: { tickColor: 'rgba(255, 255, 255, 0)', },
      tooltip: { show: true },
      colors: ['red', 'orange']
    });
  });
	
	
	
	$(function() {
	      var data = {
	        labels: ['12:00', '13:00', '14:00', '17:00', '18:00', '19:00'],
	        series: [
	          [27, 43, 14, 17, 31, 29],
	          [17, 16, 5, 3, 20, 15]
	        //  [5, 2, 0, 9, 14, 17],
	        ]
	      };

	      new Chartist.Bar('#chartist-bars', data, {
	        seriesBarDistance: 10,
	        axisX: { offset: 60 },
	        axisY: {
	          offset:        80,
	          scaleMinSpace: 15,

	          labelInterpolationFnc: function(value) {
	            return value + ' CHF';
	          },
	        },
	      });
	    });
	
	
	
	$(function() {
	      var data = [
	        { label: '신전떡볶이', data: 22 },
	        { label: '가마로닭강정', data: 12 },
	        { label: '맘스터치', data: 37 },
	        { label: '한솥도시락', data: 45 },
	        { label: '미소야', data: 12 },
	        { label: '피자스쿨', data: 33 },
	        { label: '빽다방', data: 7 },
	      ];

	      $.plot($('#flot-pie'), data, {
	        series: {
	          shadowSize: 0,
	          pie: {
	            show:        true,
	            radius:      1,
	            innerRadius: 0.5,

	            label: {
	              show:       true,
	              radius:     3 / 4,
	              background: { opacity: 0 },

	              formatter: function(label, series) {
	                return '<div style="font-size:11px;text-align:center;color:white;">' + Math.round(series.percent) + '%</div>';
	              },
	            },
	          },
	        },

	        grid: {
	          color:       '#999',
	          borderColor: 'rgba(255, 255, 255, 0)',
	          borderWidth: 1,
	          hoverable:   true,
	          clickable:   true,
	        },
 
	        xaxis: { tickColor: 'rgba(255, 255, 255, 0)' },
	        colors: ["#0288D1", "#CDDC39", "#D32F2F", "#00BCD4", "#607D8B", "#FFC107", "#E040FB", "#673AB7", "#E91E63", "#4CAF50", "#795548", "#FF5722", "#9E9E9E", "#FF4081", "#009688"],
	      });
	    });
</script>