package me.zhengjie.modules.security.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class BufferedImageUtil {
  public static String BufferedImageToBase64(BufferedImage image) throws IOException{
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
      ImageIO.write(image, "png", baos);//写入流中
      byte[] bytes = baos.toByteArray();//转换成字节
      String png_base64 =  Base64.encodeBase64String(bytes).trim();//转换成base64串
      png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
      
      
      //System.out.println("值为："+"data:image/jpg;base64,"+png_base64);
      return "data:image/jpg;base64,"+png_base64;
  }
}
