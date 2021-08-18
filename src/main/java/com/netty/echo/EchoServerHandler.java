package com.netty.echo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EchoServerHandler extends ChannelHandlerAdapter {
    // 채널을 읽을 때 동작할 코드를 정의 합니다.
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        // 입력받은 메세지를 그대로 다시 write합니다.
        log.info("msg={}", msg);
        ctx.write(msg);
    }

    // 채널을 다 읽었을때 동작할 함수
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    // 예외처리
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
