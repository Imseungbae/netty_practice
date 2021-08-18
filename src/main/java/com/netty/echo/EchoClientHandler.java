package com.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public class EchoClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf message;

    public EchoClientHandler() {
        message = Unpooled.buffer(EchoClient.MESSAGE_SIZE);
        byte[] str = "helloWorld".getBytes();
        message.writeBytes(str);
    }

    // 채널이 활성화된후 동작
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ctx.writeAndFlush(message); // 메세지를 작성하고 flush합니다.
    }

    // 서버에서 보낸 응답 메세지 read
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 받은 메세지를 byteBuf형으로 캐스팅
        ByteBuf byteBufMsg = (ByteBuf) msg;
        int size = byteBufMsg.readableBytes();

        // 읽을 수 있을만큼 바이트배열 초기화
        byte []  byteMessage = new byte[size];
        for(int i=0; i<size; i++){
            byteMessage[i] = byteBufMsg.getByte(i);
        }

        // 바이트배열 -> String
        String str = new String(byteMessage);
        System.out.println("str = " + str);
        ctx.close();
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    // 예외처리
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
