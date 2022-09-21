<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>

    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

    <meta charset="UTF-8">
    <title>Title</title>

    <style>
        .div-wrap{
            display: block; margin: 10px 0 10px 0;
        }
        .div-wrap input{
            width: 250px;
        }

        .div-wrap .d-tit{
            display: inline-block;
            width: 100px;
        }
    </style>
</head>
<body>
    <h1>[입력]</h1>
    <div class="div-wrap">
        <span class="d-tit">
            URL
        </span>
        <input type="text" id="url" name="url" value="<%--https://finance.naver.com/sise/sise_market_sum.nhn?&page=1--%>">
    </div>
    <div class="div-wrap">
        <span class="d-tit">
            Type
        </span>
        <select name="type">
            <option value="HTML">HTML 태그전체</option>
            <option value="ALL">Text 전체</option>
        </select>
    </div>
    <div class="div-wrap">
        <span class="d-tit">
            출력단위묶음
        </span>
        <input type="number" id="outputCount" name="outputCount" value="">
    </div>
    <input type="button" value="출력" onclick="dataSend();">

    <h1>[출력]</h1>
    <div class="div-wrap">
        <span class="d-tit">
            몫
        </span>
        <input type="text" id="quotient" value="">
    </div>
    <div class="div-wrap">
        <span class="d-tit">
            나머지
        </span>
        <input type="text" id="remainder" value="">
    </div>
</body>
    <script>
        function dataSend() {

			if(!isValid()) return;

			var params = {};
			params.url = $('#url').val();
			params.type = $('select[name="type"]').val();
			params.outputCount = $('#outputCount').val();

            $.ajax({
                url: "/codingTest",
                type:"post",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function(data) {
					if(!data.errorCode){
                        $('#quotient').val(data.quotient);
						$('#remainder').val(data.remainder);
                    }else{
						if(data.errorMessage){
							alert(data.errorMessage);
                        }
                    }
                    console.log('data',data);
                },
                error: function() {
                    alert("error");
                }
            })
        }

		//validation
		function isValid(){
			var url = $('#url').val();
			var outputCount = $('#outputCount').val();

			//check
			if(url === ''){
				alert('url은 필수값입니다.');
				$('#url').focus();
				return false;
            } else if(outputCount === ''){
				alert('출력단위묶음은 필수값입니다.');
				$('#outputCount').focus();
				return false;
			}
			return true;
        }
    </script>
</html>