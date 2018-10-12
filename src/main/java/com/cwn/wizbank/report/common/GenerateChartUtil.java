package com.cwn.wizbank.report.common;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.abel533.echarts.Option;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 生成统计图表工具类
 * @author bill.lai 2018/5/9.
 */
@Component
public class GenerateChartUtil {

    public static String port;

    @Value("${server.port}")
    public void setPort(String ports) {
        port = ports;
    }

    /**
     * 非公开构造方法
     */
    GenerateChartUtil(){}

    /**
     * 根据eChart option生成相应的图表
     * @param option
     * @return
     */
    public static String generateChart(Option option){
        File pngDirectory = new File("temp");
        if (!pngDirectory.exists()) {
            pngDirectory.mkdir();
        }
        String pngFilePath = pngDirectory.getAbsolutePath() + "/reportChart.png";
        //创建一个Web客户端
        try(WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            //开启支持https
            webClient.getOptions().setUseInsecureSSL(true);
            //启用JavaScript支持
            webClient.getOptions().setJavaScriptEnabled(true);

            //将URL地址的页面加载到当前的Web客户端中
            HtmlPage page = webClient.getPage("http://127.0.0.1:"+ port +"/report-generate-template/generateReportChart.html");

            //获取id为option的dom节点
            DomElement optionField = page.getElementById("option");
            //将option值放进optionField的value中
            optionField.setAttribute("value", option.toString());

            //获取到id等于generate的a标签
            HtmlAnchor anchor = page.getHtmlElementById("generate");
            //模拟点击事件，点击a标签并返回新的HtmlPage
            page = anchor.click();

            //获取所有svg标签dom
            List<DomElement> svgDomElementList = page.getElementsByTagName("svg");

            //得到第一个svg dom对象，一般都只有一个
            DomElement svgDom = svgDomElementList.get(0);

            //如果svgDom不为空
            if(svgDom != null){
                //使用batik trans coder将svg代码转换成png图片时，有一些节点会不受支持，现在将不支持的节点去掉
                if(svgDom.getLastChild() != null && "defs".equals(svgDom.getLastChild().getNodeName())){
                    svgDom.removeChild(svgDom.getLastChild());
                }

                //获得svgCode
                String svgCode = svgDom.asXml()
                        //batik trans coder 不支持rgba(192,192,192,0.0001)格式的样式，替换掉
                        .replaceAll("rgba\\(0,0,0,0\\)","none")
                        //batik trans coder不支持url(#zr2321-clip-0)格式，替换掉
                        .replaceAll("\"url([\\s\\S]{0,20})\"", "\"none\"")
                        //batik trans coder 不支持 stroke-dasharray=""，替换掉
                        .replaceAll("stroke-dasharray=\"\"","stroke-dasharray=\"8,3\"")
                        //batik trans coder 不支持undefined，替换掉
                        .replaceAll("undefined","1");

                //将svgCode转换成png图片
                svgCodeConvertToPng(svgCode, pngFilePath);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return pngFilePath;
    }

    /**
     * 将svgCode转换为png
     * @param svgCode svg代码
     * @param pngFilePath 保存的路径
     */
    public static void svgCodeConvertToPng(String svgCode, String pngFilePath){
        //创建文件输出流
        try(FileOutputStream outputStream = new FileOutputStream(new File(pngFilePath))){
            //获取svgCode的字节数组
            byte[] bytes = svgCode.getBytes("utf-8");
            //创建png代码转换对象
            PNGTranscoder t = new PNGTranscoder();
            //创建图片输入流
            TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
            //创建图片输出流
            TranscoderOutput output = new TranscoderOutput(outputStream);
            //转换并保存图片
            t.transcode(input, output);
            //刷新此输出流并强制将所有缓冲的输出字节被写出
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
