package com.github.smqtt.jar;

import com.github.smqtt.AbstractBootstrap;

/**
 * @author luxurong
 * @date 2021/4/14 20:39
 * @description
 */
public class JarBootstrap extends AbstractBootstrap {

    public static void main(String[] args) {
        bootstrap(System::getProperty);
    }

}
