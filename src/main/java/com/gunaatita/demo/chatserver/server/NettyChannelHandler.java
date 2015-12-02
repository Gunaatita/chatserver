package com.gunaatita.demo.chatserver.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.gunaatita.demo.chatserver.controller.ChatController;


@Sharable
public class NettyChannelHandler implements ChannelInboundHandler {
	
	private static Logger logger = LoggerFactory.getLogger(NettyChannelHandler.class);

	private ApplicationContext applicationContext;
	
	private WebSocketServerHandshaker handshaker;

	public NettyChannelHandler(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelActive(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelInactive(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		if (obj instanceof HttpRequest) {
			HttpRequest httpRequest = (HttpRequest) obj;
			String uri = httpRequest.getUri();
			if(uri.equals("/chat")){
				String upgradeHeader = httpRequest.headers().get("Upgrade");
				if (upgradeHeader != null && "websocket".equalsIgnoreCase(upgradeHeader)) {
					String url = "ws://" + httpRequest.headers().get("Host") + "/chat";
					WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(url, null, false);
					handshaker = wsFactory.newHandshaker(httpRequest);
					if (handshaker == null) {
						WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
					} else {
						handshaker.handshake(ctx.channel(), httpRequest);
					}
				} 
			}else{
				HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.NOT_FOUND);
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
				return;
			}
		}else if( obj instanceof WebSocketFrame){
			WebSocketFrame wsFrame = (WebSocketFrame) obj;
			handleWebSocketFrame(ctx, wsFrame);
		}
	}
	

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
        	String request = ((TextWebSocketFrame) frame).text();
        	String response = handleRequest(request);
        	ctx.channel().write(new TextWebSocketFrame(response));
        }

    }

	

	private String handleRequest(String request) {
		ChatController chatController = applicationContext.getBean(ChatController.class);
		String response = chatController.handleMessage(request);
		return response;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext arg0)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext arg0)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage(), cause);
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext arg0, Object arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
