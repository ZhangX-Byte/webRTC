package signaling.server;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Zhang_Xiang
 * @since 2022/7/5 14:38:52
 */
@Component
@RequiredArgsConstructor
public class NettyRunner implements ApplicationRunner {

    private final SocketServer socketServer;

    @Override
    public void run(ApplicationArguments args) {
        socketServer.start();
    }
}
