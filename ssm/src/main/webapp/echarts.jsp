<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 2020/2/19
  Time: 10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <script src="resources/js/echarts.js"></script>
    <script src="resources/js/jquery.min.js"></script>
</head>
<body>
    <div id="main" style="width: 600px;height: 400px;"></div>
</body>
<script>
    $(function () {
        var myChart = echarts.init(document.getElementById("main"));
        var option = {
            title:{
                text:"Echarts 案例"
            },
            tooltip:{},
            legend:{
                data:["销量"]
            },
            xAxis:{
                data:["衬衫","羊毛衫","雪纺衫","裤子"]
            },
            yAxis:{},
            series:[{
                name:"销量",
                type:"bar",
                data:[5,20,36,10]
            }]
        };
        myChart.setOption(option);
    })
</script>
</html>
