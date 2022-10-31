package signaling.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import signaling.server.config.NettyConfig;

/**
 * @author Zhang_Xiang
 * @since 2022/7/5 14:29:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketServer {
    private final NettyConfig nettyConfig;
    private final WSServerInitializer wsServerInitializer;


    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(wsServerInitializer);
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(nettyConfig.getPort()).sync();
            log.info("服务器启动成功，端口：{}", nettyConfig.getPort());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
