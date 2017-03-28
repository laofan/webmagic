package com.immo.xxl.pc;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 获取http://www.36dm.com/1.html
 * 	  列表  与   详细信息 
 * @author xiao.xl
 * time    2017-03-27
 */
public class WebMagicLearn_02 implements PageProcessor{
	
	private Site site = Site.me().setSleepTime(1000).setRetryTimes(3);
	
	//列表页面的正则表达式
	public static final String string_url_list = "http://www\\.36dm\\.com/\\d+\\.html";
	
	//详细页面的正则表达式
	public static final String string_url_post = "http://www\\.36dm\\.com/show-\\w+\\.html";
	
	@Override
	public Site getSite() {
		return site;
	}
	
	@Override
	public void process(Page page) {
		 if(page.getUrl().regex(string_url_list).match()){
			 List<String> l_post = page.getHtml().xpath("//div[@class='clear']").links().regex(string_url_post).all();
			 List<String> l_url = page.getHtml().links().regex(string_url_list).all();
			 page.addTargetRequests(l_post);
			 page.addTargetRequests(l_url);
		 }else{
			 String title = page.getHtml().xpath("//div[@class='location']").regex("\\[[\\S|\\s]+\\<").toString();
			 page.putField("title", title.substring(0,title.length()-1).trim());
			 String torrent = page.getHtml().xpath("//p[@class='original download']").links().toString().trim();
			 page.putField("torrent", torrent);
			 System.out.println();
		 }
	}
	
	public static void main(String[] args) {
		Spider.create(new WebMagicLearn_02())
			.addUrl("http://www.36dm.com/1.html")
			.addPipeline(new ConsolePipeline())
			.addPipeline(new FilePipeline("D://test/webMagic"))
			.thread(1)
			.run();
	}
}
	