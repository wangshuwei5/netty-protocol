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
package com.github.wangshuwei5.server;

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
public class NettyServerInitializer extends
	ChannelInitializer<SocketChannel> {

    private String tlsMode;

    public NettyServerInitializer(String TLS_MODE) {
	tlsMode = TLS_MODE;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();

	// Add SSL handler first to encrypt and decrypt everything.
	// In this example, we use a bogus certificate in the server side
	// and accept any invalid certificates in the client side.
	// You will need something more complicated to identify both
	// and server in the real world.
	//
	// Read SecureChatSslContextFactory
	// if you need client certificate authentication.

	SSLEngine engine = null;
	if (SSLMODE.CA.toString().equals(tlsMode)) {
	    engine = NettySslContextFactory
		    .getServerContext(
			    tlsMode,
			    System.getProperty("user.dir")
				    + "/src/main/resources/sChat.jks",
			    null).createSSLEngine();
	} else if (SSLMODE.CSA.toString().equals(tlsMode)) {
	    engine = NettySslContextFactory
		    .getServerContext(
			    tlsMode,
			    System.getProperty("user.dir")
				    + "/src/main/resources/sChat.jks",
			    System.getProperty("user.dir")
				    + "/src/main/resources/sChat.jks")
		    .createSSLEngine();

	} else {
	    System.err.println("ERROR : " + tlsMode);
	    System.exit(-1);
	}
	engine.setUseClientMode(false);

	// Client auth
	if (SSLMODE.CSA.toString().equals(tlsMode))
	    engine.setNeedClientAuth(true);
	pipeline.addLast("ssl", new SslHandler(engine));

	pipeline.addLast("decoder", new StringDecoder());
	pipeline.addLast("encoder", new StringEncoder());
	
	ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
	ch.pipeline().addLast(new LoginAuthRespHandler());
	ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
    }
}
