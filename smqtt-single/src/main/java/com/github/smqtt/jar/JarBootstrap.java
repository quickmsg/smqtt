package com.github.smqtt.jar;

/**
 * @author luxurong
 * @date 2021/4/14 20:39
 * @description
 */
public class JarBootstrap {


    public static void main(String[] args) {

        Integer port = Integer.parseInt(System.getProperty(""));
//        System.out.println("env:"+System.getenv());

        System.out.println("env:"+System.getenv("test"));


//        System.out.println("Properties:"+System.getProperties());

        System.out.println("Properties:"+System.getProperty("workThreadSize"));



    }


}
