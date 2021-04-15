package com.github.smqtt.container;

import com.github.smqtt.AbstractBootstrap;

/**
 * @author luxurong
 * @date 2021/4/14 20:40
 * @description
 */
public class DockerBootstrap extends AbstractBootstrap {


    public static void main(String[] args) {
        bootstrap(System::getenv);
    }



}
