import * as Stomp from '@stomp/stompjs';
import { useEffect, useState } from 'react';

interface Param {
  onConnect: (frame: Stomp.Frame, client: Stomp.Client) => void;
  beforeDisconnected: (frame: Stomp.Frame, client: Stomp.Client) => void;
  reconnectDelay?: number;
}

export const useWebSocket = ({ onConnect, beforeDisconnected, reconnectDelay }: Param) => {
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    let stompClient: Stomp.Client | undefined = undefined;
    const config: Stomp.StompConfig = {
      connectHeaders: {
        Authorization: `Bearer ${localStorage.getItem('jwtToken')}`,
      },
      brokerURL: `ws://localhost:8080/ws`,
      reconnectDelay: reconnectDelay ? reconnectDelay : 5000,
      onConnect: (frame) => {
        console.log('소켓 연결 성공', frame);
        setConnected(true);
        onConnect(frame, stompClient!);
      },
      onDisconnect: (frame) => {
        console.log('소켓 연결 끊음', frame);
        setConnected(false);
        beforeDisconnected(frame, stompClient!);
      },
      logRawCommunication: false,
    };

    stompClient = new Stomp.Client(config);
    stompClient.activate();

    return () => {
      stompClient?.deactivate();
    };
  }, []);

  return connected;
};
