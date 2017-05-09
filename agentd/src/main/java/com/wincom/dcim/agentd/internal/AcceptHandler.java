package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.internal.tests.AcceptState;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import static java.lang.System.out;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class AcceptHandler implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    private final Channel channel;
    private final AgentdService service;

    public AcceptHandler(Channel channel, AgentdService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof Accept) {
            createAcceptorMachine(ctx);

            Accept a = (Accept) m;
            log.info(String.format("creating acceptor on %s:%d", a.getHost(), a.getPort()));
            ServerBootstrap boot = new ServerBootstrap();
            boot
                    .group(service.getEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            StreamHandlerContextImpl impl = (StreamHandlerContextImpl) ctx;
                            impl.setChannel(ch);
                            ctx.fire(new Accepted(ch));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            boot.bind(a.getHost(), a.getPort());
        }
    }

    private void createAcceptorMachine(HandlerContext ctx) {
        StateBuilder server = StateBuilder
                .initial().state(new AcceptState(service, ctx));

        server.fail().state(new State.Adapter() {
            @Override
            public State on(HandlerContext ctx, Message m) {
                out.println("Create server failed.");
                return success();
            }
        });

        ctx.getStateMachine()
                .buildWith(server)
                .enter();
    }
}
