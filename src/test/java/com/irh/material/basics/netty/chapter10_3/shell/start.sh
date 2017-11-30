#!/usr/bin/env bash
java -jar  jibxtools.jar -f bind.xml Customer

java -jar jibx-bind.jar binding.xml

java -cp bin;E:\jar\lib\jibx-tools.jar org.jibx.binding.generator.BindGen -b binding.xml nettyinprotocol.xml.pojo.Order