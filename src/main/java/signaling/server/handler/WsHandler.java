package signaling.server.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import signaling.server.event.WebRtcEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhang_Xiang
 * @since 2022/7/12 17:11:50
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WsHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ConcurrentHashMap<String, ChannelHandlerContext> USER_MAP = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        log.info("receive msg: {}", msg.text());
        WebRtcEvent event = JSONObject.parseObject(msg.text(), WebRtcEvent.class);
        switch (event.getType()) {
            case "connect": {
                USER_MAP.put(event.getFrom(), ctx);
                break;
            }
            case "watch": {
                WebRtcEvent watchRequest = new WebRtcEvent();
                if (USER_MAP.containsKey(event.getTo())) {
                    watchRequest.setType("watch");
                    watchRequest.setFrom(event.getFrom());
                    watchRequest.setTo(event.getTo());
                    USER_MAP.get(event.getTo()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(watchRequest)));
                } else {
                    watchRequest.setType("offline");
                    USER_MAP.get(event.getFrom()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(watchRequest)));
                }
                break;
            }
            case "offer": {
                WebRtcEvent offerRequest = new WebRtcEvent();
                offerRequest.setType("offer");
                offerRequest.setFrom(event.getFrom());
                offerRequest.setTo(event.getTo());
                offerRequest.setData(event.getData());
                USER_MAP.get(event.getTo()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(offerRequest)));
                break;
            }
            case "answer": {
                WebRtcEvent answerRequest = new WebRtcEvent();
                answerRequest.setType("answer");
                answerRequest.setFrom(event.getFrom());
                answerRequest.setData(event.getData());
                USER_MAP.get(event.getTo()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(answerRequest)));
                break;
            }
            case "candidate": {
                WebRtcEvent candidateRequest = new WebRtcEvent();
                candidateRequest.setType("candidate");
                candidateRequest.setFrom(event.getFrom());
                candidateRequest.setData(event.getData());
                USER_MAP.get(event.getTo()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(candidateRequest)));
                break;
            }
            case "hangup": {
                WebRtcEvent hangupRequest = new WebRtcEvent();
                hangupRequest.setType("hangup");
                hangupRequest.setFrom(event.getFrom());
                hangupRequest.setTo(event.getTo());
                USER_MAP.get(event.getTo()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(hangupRequest)));
                break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("新的客户端链接：" + ctx.channel().id().asShortText());
        log.info("连接的客户端 IP 地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端断开链接：" + ctx.channel().id().asShortText());
        log.info("断开的客户端 IP 地址：" + ctx.channel().remoteAddress());

        for (String key : USER_MAP.keySet()) {
            if (USER_MAP.get(key).equals(ctx)) {
                USER_MAP.remove(key);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端异常:" + ctx.channel().remoteAddress(), cause);
        cause.printStackTrace();
        ctx.close();
    }
}
