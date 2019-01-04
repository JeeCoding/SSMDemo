<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<htm>
    <head>
        <title>指标管理系统</title>
        <link rel="stylesheet" href="${ctx}/static/css/chart/share.css"/>
        <script src="WEB-INF/static/jquery/jquery-3.3.1.min.js"></script>
        <script src="${ctx}/static/comp/echarts/js/echarts.js"></script>
        <style>
            .div-left {
                width: 49%;
                float: left
            }

            .div-right {
                width: 49%;
                float: right;
            }
        </style>
    </head>
    <body>
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'" class="div-left" style="padding: 5px;">
            <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
            <div class="charts-box">
                <div id="chart1" class="chart"></div>
            </div>
        </div>
        <div data-options="region:'center'" class="div-right" style="padding: 5px;">
            <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
            <div class="charts-box">
                <div id="chart2" class="chart"></div>
            </div>
        </div>
    </div>
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'" class="div-left" style="padding: 5px;">
            <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
            <div class="charts-box">
                <div id="chart3" class="chart"></div>
            </div>
        </div>
        <div data-options="region:'center'" class="div-right" style="padding: 5px;">
            <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
            <div class="charts-box">
                <div id="chart4" class="chart"></div>
            </div>
        </div>
    </div>
    <script type="text/javascript">

        //图标模板
        var option = {
            legend: {
                type: "scroll",
                top: '25',
                data: []
            },
            series: [],
            title: {
                text: ""
            },
            tooltip: {
                axisPointer: {
                    type: "shadow"
                },
                trigger: "axis"
            },
            xAxis: [{
                data: [],
                type: "category"
            }],
            yAxis: [{
                type: "value"
            }],
            toolbox: {
                show: true,
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    magicType: {type: ['line', 'bar']},
                    restore: {},
                    saveAsImage: {}
                }
            }
        };

        // 后台四个数据ID
        var chartId = ["97087aa86782f34101678657e47a0188", "97087aa86782f34101678684815102d7",
            "97087aa86782f3410167867602370256", "97087aa86782f341016786b11f7803cc"];

        //遍历数据并为图标赋值
        $.each(chartId, function (index, value) {
            var chartIndex = index + 1;
            console.log(index + 1 + "..." + value);

            // 基于准备好的dom，初始化echarts实例
            console.log('chart' + chartIndex);
            var myChart = echarts.init(document.getElementById('chart' + chartIndex));

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            myChart.showLoading();  //显示加载动画

            var xAxisData = new Array();    //类别数组（实际用来盛放X轴坐标值）
            $(function () {
                $.ajax({
                    url: "../chart/chart/api/charts/" + value,
                    type: "GET",
                    async: true,   //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
                    data: {},
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            for (var i = 0; i < data.attributes.option.xAxis.length; i++) {
                                xAxisData = (data.attributes.option.xAxis)[i].data
                            }
                            myChart.hideLoading();    //隐藏加载动画
                            myChart.setOption({        //加载数据图表
                                legend: {
                                    type: "scroll",
                                    top: '25',
                                    data: data.attributes.option.legend.data
                                },
                                series: data.attributes.option.series,
                                title: {
                                    text: data.attributes.option.title.text
                                },
                                xAxis: [{
                                    data: xAxisData,
                                    type: "category"
                                }],
                            });

                        } else {
                            alert("加载错误")
                        }
                    },
                    error: function (data) {
                        //请求失败时执行该函数
                        alert("图表请求数据失败!");
                        myChart.hideLoading();
                    }
                });
            });
        });
    </script>
    <script type="text/javascript">
    </script>
    </body>
</htm>