package com.immo.xxl.pc;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;


public class MonitorExample {
    
    public static void main(String[] args) throws Exception {
    	Spider oschinaSpider = Spider.create(new WebMagicLearn_01())
                .addUrl("http://my.oschina.net/flashsword/blog");
        Spider githubSpider = Spider.create(new WebMagicLearn_01())
                .addUrl("https://github.com/code4craft");
        SpiderMonitor.instance().register(oschinaSpider);
        SpiderMonitor.instance().register(githubSpider);
        oschinaSpider.start();
        githubSpider.start();
	}
}