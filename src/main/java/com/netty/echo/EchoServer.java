package com.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 마이데이토 제공자 프로젝트를 진행중에 새로운 요건이 추가되었다.
 * 기존에는 하나카드의 데이터 제공범위가 카드업권 하나였는데, 이번에 전자금융업권과 할부금융업권이 추가되었다.
 * 할부금융업권은은 기존 레가시DB인 NCS스키마를 활용하여 SQL만 추가적으로 작성하면 되었지만, 문제는 전자금융업권이었다.
 * 전자금융업권에서 선불지급수단정보로 하나멤버스의 하나머니정보를 불러와서 마이데이터 사업자에게 제공해야했다.
 * 제공자와 멤버스의 통신은 기존통신방식인 HTTP통신이나, EAI통신이 불가능했다. 따라서, 대외통신인 FEP통신을 해야하만했다.
 * FEP는 TCP/IP소켓통신기반이고, 우리는 스프링프레임워크에서 제공하는 Netty를 이용하여 클라이언트소켓과 서버소켓을 구현하기로 결정하였다.
 */
@Component
@Slf4j
@NoArgsConstructor
public class EchoServer{

    private final static int PORT = 8888;

//    @PostConstruct
    public void init() {
        log.info("+++++++++++<EchoServer INIT>++++++++++++");
        // 연결스레드
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        // 워커스레드
        EventLoopGroup childGroup  = new NioEventLoopGroup();

        try{
            // bootStrap클래스 : 서버설정을 도와주는 일종의 헬퍼클래스, Channel을 직접셋팅할 수 있다.
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100) // 소켓 커넥션 수
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline cp = ch.pipeline();
                            cp.addLast(new EchoServerHandler()); // 사용자가 생성한 클래스
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
