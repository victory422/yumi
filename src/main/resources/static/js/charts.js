
/*line_chart01*/
/* Operation 횟수*/
function getOperation(){
	$.ajax({
		type: "get", //데이터 전송 타입,
		url : "/dashboard/operation",
		success: function(data){
			//작업이 성공적으로 발생했을 경우
			operation(data)
		},
		error:function(){  
            //에러가 났을 경우 실행시킬 코드
		}
	})
}

function operation(data){
	"use strict";
Highcharts.chart('line_chart02', {
    chart: {
        type: 'line',
        spacingBottom: 30
    },
    colors: ['#ffb1c1', '#fbbe81', '#a5dfdf'],
    title: {
        text: ''
    },
    subtitle: {
        text: '',
        floating: true,
        align: 'right',
        verticalAlign: 'bottom',
        y: 0
    },
    xAxis: {
        categories: data.sDate
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value;
            }
        }
    },
    tooltip: {
        formatter: function () {
            return '<b>' + this.series.name + '</b><br/>' +
                this.x + ': ' + this.y;
        }
    },
    plotOptions: {
        area: {fillOpacity: 0.3},
		series: {
                marker: { radius: 3}
            }

    },
	marker: {
                    radius: 10
                },
    credits: {
        enabled: false
    },
    series: data.sData
});
};

 
$(document).ready(function(){
});
	

function getMajorOperation(){
	$.ajax({
		type: "get", //데이터 전송 타입,
		url : "/dashboard/major_operation",
		success: function(data){
			//작업이 성공적으로 발생했을 경우
			major_operation(data)
		},
		error:function(){  
            //에러가 났을 경우 실행시킬 코드
		}
	})
}

function major_operation(data){
"use strict";
Highcharts.chart('line_chart', {
    chart: {
        type: 'line',
        spacingBottom: 30
    },
	colors: ['#ffb1c1', '#fbbe81', '#a5dfdf', '#9ad0f5', '#ccb2ff', '#a5a5a5'],
    title: {
        text: ''
    },
    subtitle: {
        text: '',
        floating: true,
        align: 'right',
        verticalAlign: 'bottom',
        y: 0
    },
    xAxis: {
        categories: data.sDate
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value;
            }
        }
    },
    tooltip: {
        formatter: function () {
            return '<b>' + this.series.name + '</b><br/>' +
                this.x + ': ' + this.y;
        }
    },
    plotOptions: {
        area: {fillOpacity: 0.3},
		series: {
                marker: { radius: 3}
            }

    },
	marker: {
                    radius: 10
                },
    credits: {
        enabled: false
    },
    series: [
		{
			name: 'Count',
			data: data.sData
		}
	]
});
};

 
$(document).ready(function(){
});



function key_status_counter(data){
"use strict";
Highcharts.chart('circle_chart', {
  chart: {
      plotBackgroundColor: null,
      plotBorderWidth: null,
      plotShadow: false,
      type: 'pie'
  },
	colors: ['#ffb1c1', '#fbbe81', '#a5dfdf', '#9ad0f5', '#ccb2ff', '#a5a5a5'],
    title: {
        text: ''
    },
    subtitle: {
        text: '',
        floating: true,
        align: 'right',
        verticalAlign: 'bottom',
        y: 0
    },
    accessibility: {
        point: {
          valueSuffix: '%'
        }
      },
      plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',
        dataLabels: {
          enabled: false
        },
        showInLegend: true
      }
    },

    credits: {
        enabled: false
    },
    series: [{
    name: '',
    colorByPoint: true,
    data: data.yData
  }]
});
};






/*CPU % 이용률*/
$(document).ready(function(){
var gaugeOptions = {
    chart: {
        type: 'solidgauge'
    },
    title: null,
    pane: {
        center: ['50%', '85%'],
        size: '120%',
        startAngle: -90,
        endAngle: 90,
        background: {
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
            innerRadius: '60%',
            outerRadius: '100%',
            shape: 'arc'
        }
    },

    tooltip: {
        enabled: false
    },

    // the value axis
    yAxis: {
        stops: [
            [0.1, '#a5dfdf'], // green
            [0.5, '#fbbe81'], // yellow
            [0.9, '#ffb1c1'] // red
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
            y: -70
        },
        labels: {
            y: 16
        }
    },

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

var chartSpeed1 = Highcharts.chart('semi_donut_chart', Highcharts.merge(gaugeOptions, {
    yAxis: {
        min: 0,
        max: 100,
        title: {
            text: ''
        }
    },

    credits: {
        enabled: false
    },

    series: [{
        name: 'CPU % 이용률',
        data: [0],
        dataLabels: {
            format: '<div style="text-align:center"><span style="font-size:25px;color:' +
                ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span>&nbsp' +
                   '<span style="font-size:12px;color:silver">%</span></div>'
        },
        tooltip: {
            valueSuffix: '%'
        }
    }]

}));

var cpuVal;

setInterval(function () {
	
    // Speed
    var point
        
    $.ajax({
		type: "get", //데이터 전송 타입,
		url : "/dashboard/cpu",
		success: function(data){
			//작업이 성공적으로 발생했을 경우
			cpuVal = data.sData;
		},
		error:function(){  
            //에러가 났을 경우 실행시킬 코드
		}
	})    

    if (chartSpeed1) {
		
        point = chartSpeed1.series[0].points[0];
        /*
        inc = Math.round((Math.random() - 0.5) * 100);
        newVal = point.y + inc;

        if (newVal < 0 || newVal > 100) {
            newVal = point.y - inc;
        }*/
        point.update(cpuVal);
    }
}, 2000);
});


/*메모리 % 이용률*/
$(document).ready(function(){
var gaugeOptions = {
    chart: {
        type: 'solidgauge'
    },
    title: null,
    pane: {
        center: ['50%', '85%'],
        size: '120%',
        startAngle: -90,
        endAngle: 90,
        background: {
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
            innerRadius: '60%',
            outerRadius: '100%',
            shape: 'arc'
        }
    },

    tooltip: {
        enabled: false
    },

    // the value axis
    yAxis: {
        stops: [
          [0.1, '#a5dfdf'], // green
          [0.5, '#fbbe81'], // yellow
          [0.9, '#ffb1c1'] // red
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
            y: -70
        },
        labels: {
            y: 16
        }
    },

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

var chartSpeed2 = Highcharts.chart('semi_donut_chart02', Highcharts.merge(gaugeOptions, {
    yAxis: {
        min: 0,
        max: 100,
        title: {
            text: ''
        }
    },

    credits: {
        enabled: false
    },

    series: [{
        name: '디스크 사용율',
        data: [0],
        dataLabels: {
            format: '<div style="text-align:center"><span style="font-size:25px;color:' +
                ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span>&nbsp' +
                   '<span style="font-size:12px;color:silver">%</span></div>'
        },
        tooltip: {
            valueSuffix: '%'
        }
    }]

}));

var memVal;
setInterval(function () {
    // Speed
    var point
	
	$.ajax({
		type: "get", //데이터 전송 타입,
		url : "/dashboard/memory",
		success: function(data){
			//작업이 성공적으로 발생했을 경우
			memVal = data.sData;
		},
		error:function(){  
            //에러가 났을 경우 실행시킬 코드
		}
	})
	
    if (chartSpeed2) {
       
        point = chartSpeed2.series[0].points[0];
        /*
        inc = Math.round((Math.random() - 0.5) * 100);
        newVal = point.y + inc;

        if (newVal < 0 || newVal > 100) {
            newVal = point.y - inc;
        }*/
        point.update(memVal);
    }
}, 2000);
});

/*Driver % 이용률*/
$(document).ready(function(){
var gaugeOptions = {
    chart: {
        type: 'solidgauge'
    },
    title: null,
    pane: {
        center: ['50%', '85%'],
        size: '120%',
        startAngle: -90,
        endAngle: 90,
        background: {
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
            innerRadius: '60%',
            outerRadius: '100%',
            shape: 'arc'
        }
    },

    tooltip: {
        enabled: false
    },

    // the value axis
    yAxis: {
        stops: [
          [0.1, '#a5dfdf'], // green
          [0.5, '#fbbe81'], // yellow
          [0.9, '#ffb1c1'] // red
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
            y: -70
        },
        labels: {
            y: 16
        }
    },

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

var driverVal;
var chartSpeed3 = Highcharts.chart('semi_donut_chart03', Highcharts.merge(gaugeOptions, {
    yAxis: {
        min: 0,
        max: 100,
        title: {
            text: ''
        }
    },

    credits: {
        enabled: false
    },

    series: [{
        name: '디스크 사용율',
        data: [0],
        dataLabels: {
            format: '<div style="text-align:center"><span style="font-size:25px;color:' +
                ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span>&nbsp' +
                   '<span style="font-size:12px;color:silver">%</span></div>'
        },
        tooltip: {
            valueSuffix: '%'
        }
    }]

}));

setInterval(function () {
    // Speed
    var point
        
    $.ajax({
		type: "get", //데이터 전송 타입,
		url : "/dashboard/driver",
		success: function(data){
			//작업이 성공적으로 발생했을 경우
			driverVal = data.sData;
		},
		error:function(){  
            //에러가 났을 경우 실행시킬 코드
		}
	})    

    if (chartSpeed3) {
        
        point = chartSpeed3.series[0].points[0];
        /*
        inc = Math.round((Math.random() - 0.5) * 100);
        newVal = point.y + inc;

        if (newVal < 0 || newVal > 100) {
            newVal = point.y - inc;
        }*/
        point.update(driverVal);
    }
}, 2000);
});