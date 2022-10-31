package signaling.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import signaling.server.handler.WsHandler;

/**
 * @author Zhang_Xiang
 * @since 2022/7/6 09:06:51
 */
@Component
@RequiredArgsConstructor
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    private final WsHandler wsHandler;

    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", true));
        pipeline.addLast(wsHandler);
    }
}
