/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.irh.material.basics.netty.chapter14_1.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Created by iritchie on 17-3-10.
 */
public class MarshallingDecoder{

    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException{
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception{
        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ByteInput input = new ChannelBufferByteInput(buf);
        try{
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        } finally{
            unmarshaller.close();
        }
    }
}
