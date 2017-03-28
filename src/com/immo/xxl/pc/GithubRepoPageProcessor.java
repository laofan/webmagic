package com.immo.xxl.pc;  
  
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
  
public class GithubRepoPageProcessor implements PageProcessor {  
  
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);  
    private List<String> idlist = null;  
    public static Map<String,String> processmap = new HashMap<String,String>();  
    public static int q = 0;  
    @Override  
    public void process(Page page) {  
        if (page.getUrl()  
                .regex("http://p\\.3\\.cn/prices/mgets\\?type=1&skuIds=J_\\d+")  
                .match()  
                ) {  
            System.err  
                    .println("jjjjjjjjjjjjjjjjjiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");  
            String price = page.getJson().jsonPath("$..p").toString();  
            System.out.println("price------------"+price);  
            processmap.put("pricew"+q, price);  
            //page.putField("pricew"+q, price);  
            q++;  
        } else {  
            List<String> list = page.getHtml().links()  
                    .regex("(http://channel\\.jd\\.com/1713-3261.html)").all();  
            System.out.println("list" + list.toString());
            int j=1;
            for (String string : list) {  
                System.err.println("ff------" + j + "-----" + string);
                j++;
            }  
            // page.addTargetRequests(list);  
            // page.putField("author",  
            // page.getUrl().regex("https://p4psearch\\.1688\\.com\\/p4p114/p4psearch/offer2\\.htm?*").toString());  
            List<String> jlist = page.getHtml()  
                    .xpath("//div[@class=\'p-img']/html()").all();  
            idlist = page.getHtml().xpath("//div/strong/@data-price-id").all();  
  
            for (int i = 0; i < idlist.size(); i++) {
				System.out.println("idList-----"+idlist.get(i));
			}
            // http://www.cnblogs.com/momoka/articles/5417426.html  
  
//            List<String> names = new ArrayList<>();  
            int i = 0;  
//            int j = 0;  
            for (String string : jlist) {  
  
                System.err.println("String----------"+string);  
                page.putField("name" + i, string);  
                i++;  
            }  
  
            System.out  
                    .println("jlist---------------------size!" + jlist.size());  
            if (page.getResultItems().get("name") == null) {  
                // skip this page  
                page.setSkip(true);  
            }  
  
              
  
            // page.addTargetRequests(list);  
  
            // page.putField("readme",  
            // page.getHtml().xpath("//div[@id='readme']/tidyText()"));  
            String nametrue = page.getResultItems().get("name" + 1);  
            String pricetrue = page.getResultItems().get("price" + 1);  
  
            System.out.println("true" + nametrue);  
            System.out.println("true" + pricetrue);  
  
            int k = 0;  
            for (String string : idlist) {  
                //System.err.println(string);  
                 String url="http://p.3.cn/prices/mgets?type=1&skuIds=J_"+string;  
                 //String price = page.getJson().$("p").toString();  
                   
                 Spider.create(new GithubRepoPageProcessor())  
                 .addUrl(url)  
                 .thread(3).run();  
                 //System.err.println(price);  
                page.putField("id" + k, string);  
                System.out.println("idone"+string);  
                k++;  
              
            }  
            //System.out.println(page.getResultItems().get("pricew"+1));  
            System.err.println(processmap.get("pricew"+1));  
        }  
    }  
  
    @Override  
    public Site getSite() {  
        return site;  
    }  
  
    public static void main(String[] args) {  
        Spider.create(new GithubRepoPageProcessor())  
                .addUrl("http://channel.jd.com/1713-3261.html").thread(5).run();  
    }  
}  