///*
// *  Copyright 1999-2018 Alibaba Group Holding Ltd.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//
//package com.alibaba.fescar.core.rpc.netty;
//
//import java.net.InetSocketAddress;
//
//import com.alibaba.fescar.common.exception.FrameworkException;
//import com.alibaba.fescar.common.util.NetUtil;
//import com.alibaba.fescar.core.protocol.RegisterRMResponse;
//import com.alibaba.fescar.core.protocol.RegisterTMResponse;
//import com.alibaba.fescar.core.rpc.netty.NettyPoolKey.TransactionRole;
//
//import io.netty.channel.Channel;
//import org.apache.commons.pool.KeyedPoolableObjectFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * The type Netty key poolable factory.
// *
// * @author jimin.jm @alibaba-inc.com
// * @date 2018 /11/19
// */
//public class NettyPoolableFactory implements KeyedPoolableObjectFactory<NettyPoolKey, Channel> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(NettyPoolableFactory.class);
//    private final AbstractRpcRemotingClient rpcRemotingClient;
//
//    /**
//     * Instantiates a new Netty key poolable factory.
//     *
//     * @param rpcRemotingClient the rpc remoting client
//     */
//    public NettyPoolableFactory(AbstractRpcRemotingClient rpcRemotingClient) {
//        this.rpcRemotingClient = rpcRemotingClient;
//        this.rpcRemotingClient.setChannelHandlers(rpcRemotingClient);
//        this.rpcRemotingClient.start();
//    }
//
//    @Override
//    public Channel makeObject(NettyPoolKey key) throws Exception {
//        if (key != null && key.getAddress().contains("172.26.91.186")) {
//            System.out.println("==>makeObject key.getAddress: " + key.getAddress());
//        }
//        InetSocketAddress address = NetUtil.toInetSocketAddress(key.getAddress());
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info("NettyPool create channel to " + key);
//        }
//        Channel tmpChannel = rpcRemotingClient.getNewChannel(address);
//        long start = System.currentTimeMillis();
//        Object response = null;
//        Channel channelToServer = null;
//        if (null == key.getMessage()) {
//            throw new FrameworkException(
//                    "register msg is null, role:" + key.getTransactionRole().name());
//        }
//        try {
//            response = rpcRemotingClient.sendAsyncRequestWithResponse(null, tmpChannel, key.getMessage());
//            if (!isResponseSuccess(response, key.getTransactionRole())) {
//                rpcRemotingClient.onRegisterMsgFail(key.getAddress(), tmpChannel, response, key.getMessage());
//            } else {
//                channelToServer = tmpChannel;
//                rpcRemotingClient.onRegisterMsgSuccess(key.getAddress(), tmpChannel, response,
//                        key.getMessage());
//            }
//        } catch (Exception exx) {
//            if (tmpChannel != null) {
//                tmpChannel.close();
//            }
//            throw new FrameworkException(
//                    "register error,role:" + key.getTransactionRole().name() + ",err:" + exx.getMessage());
//        }
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info(
//                    "register success, cost " + (System.currentTimeMillis() - start) + " ms, version:"
//                            + getVersion(response, key.getTransactionRole()) + ",role:" + key.getTransactionRole().name()
//                            + ",channel:" + channelToServer);
//        }
//        return channelToServer;
//    }
//
//    private boolean isResponseSuccess(Object response, TransactionRole transactionRole) {
//        if (null == response) {
//            return false;
//        }
//        if (transactionRole.equals(TransactionRole.TMROLE)) {
//            if (!(response instanceof RegisterTMResponse)) {
//                return false;
//            }
//            if (((RegisterTMResponse) response).isIdentified()) {
//                return true;
//            }
//        } else if (transactionRole.equals(TransactionRole.RMROLE)) {
//            if (!(response instanceof RegisterRMResponse)) {
//                return false;
//            }
//            if (((RegisterRMResponse) response).isIdentified()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private String getVersion(Object response, TransactionRole transactionRole) {
//        if (transactionRole.equals(TransactionRole.TMROLE)) {
//            return ((RegisterTMResponse) response).getVersion();
//        } else {
//            return ((RegisterRMResponse) response).getVersion();
//        }
//    }
//
//    @Override
//    public void destroyObject(NettyPoolKey key, Channel channel) throws Exception {
//
//        if (null != channel) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("will destroy channel:" + channel);
//            }
//            channel.disconnect();
//            channel.close();
//        }
//    }
//
//    @Override
//    public boolean validateObject(NettyPoolKey key, Channel obj) {
//        if (null != obj && obj.isActive()) {
//            return true;
//        }
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info("channel valid false,channel:" + obj);
//        }
//        return false;
//    }
//
//    @Override
//    public void activateObject(NettyPoolKey key, Channel obj) throws Exception {
//
//    }
//
//    @Override
//    public void passivateObject(NettyPoolKey key, Channel obj) throws Exception {
//
//    }
//}
