package com.ijse.gdse.cw.hasitha.userService.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;

@Configuration
public class Util {
    public ByteBuffer convertBinaryToByteBuffer(byte[] binary) {
        return ByteBuffer.wrap(binary);
    }

    public byte[] convertByteBufferToBinary(ByteBuffer byteBuffer) {

        return byteBuffer.array();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
