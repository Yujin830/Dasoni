import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useRef, useState } from 'react';

interface Param {
  subscribe: (clinet: CompatClient) => void;
  beforeDisconnected: (client: CompatClient) => void;
  reconnectDelay?: number;
}

export const useWebSocket = ({ subscribe, beforeDisconnected, reconnectDelay }: Param) => {
  const [connected, setConnected] = useState(false);

  const client = useRef<CompatClient>();
  client.current = Stomp.over(() => {
    const sock = new SockJS('/ws/chat');
    return sock;
  });

  client.current.connect({}, () => {
    console.log('연결 성공');
    setConnected(true);

    // 웹 소켓 연결 성공 시 구독할 함수
    // subscribe(client.current!);
  });

  // client.current.disconnect(() => {
  //   console.log('연결 해제');
  //   setConnected(false);

  //   // 웹 소켓 연결 해제 시 실행할 함수
  //   beforeDisconnected(client.current!);
  // });

  return connected;
};
