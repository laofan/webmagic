package com.immo.xxl.pc;

import java.util.List;

import org.apache.http.HttpHost;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author xiao.xl
 * time	2017-03-27
 */
public class webMagicLearn_03 implements PageProcessor{
	private Site site = Site.me().setHttpProxy(new HttpHost("123.207.143.51", 8080)).setSleepTime(1000).setRetryTimes(3).setTimeOut(20000);
	
	public static String url_post ="http://app1.sfda.gov.cn/datasearch/face3/search.jsp?tableId=36&bcId=124356651564146415214";
	
	private int totalPage = 262;
	
	private int currentPage = 1;
	
	@Override
	public void process(Page page) {
		System.out.println("爬取进口药品第"+currentPage+"页数据");
		if(currentPage <= totalPage){
			String nextUrl = "http://app1.sfda.gov.cn/datasearch/face3/search.jsp?tableId=36&curstart="+currentPage;
			Request request = new Request(nextUrl);
			page.addTargetRequest(request);
			currentPage++;
			List<String> url_id = page.getHtml().xpath("//tbody/tr/td/p[@align='left']/a/@href/outerHtml()").regex(";Id=(\\d+)',").all();
			page.putField("url_id", url_id);
			List<String> url_drugInfo = page.getHtml().xpath("//tbody/tr/td/p[@align='left']/a/text()").all();
			page.putField("url_drugInfo", url_drugInfo);
			
			
		}
	}
		
	public static void main(String[] args) {
		Spider.create(new webMagicLearn_03())
		.addUrl("http://app1.sfda.gov.cn/datasearch/face3/search.jsp?tableId=36&curstart=1")
		.addPipeline(new ConsolePipeline())
		.addPipeline(new FilePipeline("D://test/webMagic"))
		.thread(1)
		.run();
	}

	@Override
	public Site getSite() {
		return site;
	}
	
}
