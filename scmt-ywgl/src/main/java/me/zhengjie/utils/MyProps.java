package me.zhengjie.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//自己配置的属性
@Component
@ConfigurationProperties(prefix="myprops") //接收application.yml中的myProps下面的属性
public class MyProps {
    private String luzhoufangguanjuUrl;
    private String naxiwangqianUrl;
    private Boolean isSms;

    public Boolean getIsSms() {
		return isSms;
	}

	public void setIsSms(Boolean isSms) {
		this.isSms = isSms;
	}

	public String getLuzhoufangguanjuUrl() {
        return luzhoufangguanjuUrl;
    }

    public void setLuzhoufangguanjuUrl(String luzhoufangguanjuUrl) {
        this.luzhoufangguanjuUrl = luzhoufangguanjuUrl;
    }

    public String getNaxiwangqianUrl() {
        return naxiwangqianUrl;
    }

    public void setNaxiwangqianUrl(String naxiwangqianUrl) {
        this.naxiwangqianUrl = naxiwangqianUrl;
    }

}
