<!DOCTYPE html>
<html>
<head>
</head>
<body>
<script>
    function sendDynamicpwd() {
        // $("#verifactionCode_validate_message").text("");
        if (validateMsisdn()) {
            var msisdn=$("#resetPwdMsisdn").val();
            $.ajax({
                url: "$!basePath/findPwd/sendDynamicpwd",
                type: "post",
                dataType: "text",
                data: {
                    msisdn: msisdn
                },
                success: function (data) {
                    $("#sendDynamicpwd_btn").removeClass("normal");
                    $("#sendDynamicpwd_btn").addClass("not");
                    $("#sendDynamicpwd_btn").attr("onclick", "");
                    verifyInterval = setInterval(verifyBtnStateChange, 1000);
                },
                error: function (data) {
                    // $("#verifactionCode_validate_message").text(data.responseText);
                    alert(data.responseText);
                }
            });
        }
    }

    {"attributes":{"option":{"legend":{"data":["地区生产总值（万元）","第一产业（万元）","第三产业（万元）","第二产业（万元）"]},"series":[{"data":[4451351,5000291,5895846,6905445,7762977,8235805,9108240,1.0035661E7,1.0896657E7,1.1304432E7],"name":"地区生产总值（万元）","type":"bar"},{"data":[62563,62876,71627,84940,92840,96506,92124,93407,94981,88039],"name":"第一产业（万元）","type":"bar"},{"data":[2185369,2576449,2899732,3510476,3801348,4151914,4924954,5519046,6008156,6131516],"name":"第三产业（万元）","type":"bar"},{"data":[2203419,2360966,2924487,3310029,3868789,3987385,4091162,4423208,4793520,5084877],"name":"第二产业（万元）","type":"bar"}],"title":{"text":"地区生产总值"},"tooltip":{"axisPointer":{"type":"shadow"},"trigger":"axis"},"xAxis":[{"data":["2008年","2009年","2010年","2011年","2012年","2013年","2014年","2015年","2016年","2017年"],"type":"category"}],"yAxis":[{"type":"value"}]},"type":"BAR"},"msg":"操作成功","success":true}
</script>
</body>
</html >
