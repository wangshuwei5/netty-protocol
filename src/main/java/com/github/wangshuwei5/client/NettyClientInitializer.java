/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.wangshuwei5.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import javax.net.ssl.SSLEngine;

import com.github.wangshuwei5.util.NettySslContextFactory;
import com.github.wangshuwei5.util.SSLMODE;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class NettyClientInitializer extends
	ChannelInitializer<SocketChannel> {

    private String tlsMode;

    public NettyClientInitializer(String tlsMode) {
	this.tlsMode = tlsMode;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();

	SSLEngine engine = null;
	if (SSLMODE.CA.toString().equals(tlsMode)) {
	    engine = NettySslContextFactory
		    .getClientContext(
			    tlsMode,
			    null,
			    System.getProperty("user.dir")
				    + "/src/main/resources/cChat.jks")
		    .createSSLEngine();
	} else if (SSLMODE.CSA.toString().equals(tlsMode)) {
	    engine = NettySslContextFactory
		    .getClientContext(
			    tlsMode,
			    System.getProperty("user.dir")
				    + "/src/main/resources/cChat.jks",
			    System.getProperty("user.dir")
				    + "/src/main/resources/cChat.jks")
		    .createSSLEngine();

	} else {
	    System.err.println("ERROR : " + tlsMode);
	    System.exit(-1);
	}
	engine.setUseClientMode(true);
	pipeline.addLast("ssl", new SslHandler(engine));

	pipeline.addLast("decoder", new StringDecoder());
	pipeline.addLast("encoder", new StringEncoder());

	ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
	ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
	ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
    }
}
