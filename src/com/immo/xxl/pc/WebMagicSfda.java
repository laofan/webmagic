package com.immo.xxl.pc;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class WebMagicSfda implements PageProcessor{
	//总页数
    private int totalPage = 100;
    
    //当前页数
    private int currentPage = 1;
    
    //抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(20000).setCharset("utf-8");

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		dealPage(page);
	}

	//执行测试
    public static void main(String[] args) {
        Spider.create(new WebMagicPC())
                .addUrl("http://www.lookmw.cn/yc/list_64477_1.html")
                //开启1个线程抓取
                .thread(10)
                //启动爬虫
                .run();
    }
	
	private void dealPage(Page page) {
		if(isListPage(page)){
			if(currentPage<totalPage){
				String urlStr = "http://www.lookmw.cn/yc/list_64477_"+currentPage+".html";
				Request request = new Request(urlStr);
				page.addTargetRequest(request);
				currentPage++;
				List<String> lists = page.getHtml().xpath("//div[@class='picAtc pr']/li/div[@class='info']/p").links().all();
				for(String list :lists){
					System.out.println(list);
				}
			}
		}
		
	}

	private boolean isListPage(Page page) {
		String url = page.getUrl().toString();
		boolean flag = url.contains("tableName=TABLE25")?true:false;
		return flag;
	}
	
}
