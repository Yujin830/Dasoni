import SockJS from 'sockjs-client';
import { Client, CompatClient, Frame, Stomp } from '@stomp/stompjs';
import { useEffect, useRef, useState } from 'react';
import { WS_BASE_URL } from '../apis/url';

interface Param {
  subscribe: (clinet: Client) => void;
  beforeDisconnected: (client: CompatClient) => void;
  reconnectDelay?: number;
}

export const useWebSocket = ({ subscribe, beforeDisconnected, reconnectDelay }: Param) => {
  const [connected, setConnected] = useState(false);

  // const client = useRef<CompatClient>();

  useEffect(() => {
    const client = new Client();
    client.brokerURL = WS_BASE_URL;
    // client.brokerURL = 'ws://localhost:8080/ws/chat';
    console.log(client.brokerURL);

    client.onConnect = function (frame: Frame) {
      console.log('연결 설정');
      subscribe(client);
    };

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  // useEffect(() => {
  //   client.current = Stomp.over(() => {
  //     const sock = new SockJS('/ws/chat');
  //     return sock;
  //   });
  //   console.log('client ', client.current);
  //   client.current.connect({}, () => {
  //     console.log('연결 성공');
  //     setConnected(true);

  //     subscribe(client.current!);
  //   });
  //   // client.current!.onConnect(frame: Frame ) = function {
  //   //   console.log('연결 성공');
  //   //   setConnected(true);

  //   //   // 웹 소켓 연결 성공 시 구독할 함수
  //   //   // subscribe(client.current!);
  //   // });
  // }, []);

  // client.current.disconnect(() => {
  //   console.log('연결 해제');
  //   setConnected(false);

  //   // 웹 소켓 연결 해제 시 실행할 함수
  //   beforeDisconnected(client.current!);
  // });

  return connected;
};
