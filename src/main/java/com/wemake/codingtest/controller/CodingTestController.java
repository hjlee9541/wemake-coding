package com.wemake.codingtest.controller;

import com.wemake.codingtest.model.Input;
import com.wemake.codingtest.model.Output;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class CodingTestController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/codingTest", method = RequestMethod.POST)
    @ResponseBody
    public Output ajaxTest(@RequestBody Input input) throws Exception {

        //output
        Output output = new Output();

        //parameter
        String url = input.getUrl();
        String type = input.getType();
        int outputCount = input.getOutputCount();

        //null check
        if("".equals(url) || "".equals(type) || outputCount == 0){
            output.setErrorCode("BZ701");
            output.setErrorMessage("ERROR : Parameter is null");
            return output;
        }

        //Connection
        Connection conn = null;

        try {
            conn = Jsoup.connect(url);
            Document document = conn.get();
            String htmlStr = document.toString();

            //HTML 태그 제거 ( 10: HTML 제거, 20 : 전체 )
            String removedHtmlStr = "";
            if("HTML".equals(type)){
                removedHtmlStr = convertHtml(htmlStr, "10");
            }else{
                removedHtmlStr = htmlStr;
            }

            //영어 숫자외 문자제거
            String numberEngStr = convertHtml(removedHtmlStr, "20");
            //숫자
            String numberStr = convertHtml(numberEngStr, "30");
            //영어
            String engStr = convertHtml(numberEngStr, "40");

            //string to array
            String[] numberStrArr = numberStr.split("");
            String[] engStrArr = engStr.split("");

            //sort
            Arrays.sort(numberStrArr);
            Arrays.sort(engStrArr);

            //merge ( 교차 )
            String[] mergedArr = mergeArr(numberStrArr, engStrArr);

            //결과
            int arrSize = mergedArr.length;
            int quotient = arrSize / outputCount;
            int remainder = arrSize % outputCount;

            //output
            output.setQuotient(quotient);
            output.setRemainder(remainder);

            /*String mergedStr = Arrays
                    .stream(mergedArr)
                    .collect(Collectors.joining());*/

        } catch (Exception e) {
            //log : e.getMessage();
            output.setErrorCode("BZ703");
            output.setErrorMessage("Error : " + e.getMessage());
            return output;
        }
        return output;
    }

    //merge ( array , array )
    public String[] mergeArr(String[] arr1, String[] arr2){

        //array -> arrayList
        List<String> list1 = Arrays.asList(arr1);
        List<String> list2 = Arrays.asList(arr2);

        List<String> resultArr = new ArrayList<String>();
        int index = arr1.length + arr2.length;
        for (int i = 0; i < index; i++) {
            if(list1.size() > i){
                resultArr.add(list1.get(i));
            }
            if(list2.size() > i){
                resultArr.add(list2.get(i));
            }
        }

        return resultArr.stream()
                .toArray(String[]::new);
    }

    //convert
    public String convertHtml(String str, String type){

        switch(type) {
            case "10" :
                //html 태그제거
                return str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");
            case "20":
                //영어, 숫자
                return str.replaceAll("[^A-Za-z0-9]","");
            case "30":
                //숫자
                return str.replaceAll("[^0-9]","");
            case "40":
                //영어(대,소)
                return str.replaceAll("[^a-zA-Z]","");
            default:
                return "";
        }
    }
}
