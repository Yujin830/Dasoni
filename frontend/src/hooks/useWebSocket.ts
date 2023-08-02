import { Client, Frame } from '@stomp/stompjs';
import { useEffect, useState } from 'react';

interface Param {
  onConnect: (frame: Frame, client: Client) => void;
  beforeDisconnected: (frame: Frame, client: Client) => void;
  reconnectDelay?: number;
}

export const useWebSocket = ({ onConnect, beforeDisconnected, reconnectDelay }: Param) => {
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: `ws://localhost:8080/ws/chat`,
      // connectHeaders: {
      //   Authorization: `Bearer ${localStorage.getItem('jwtToken')}`,
      // },
      logRawCommunication: false,
    });

    stompClient.onConnect = (frame) => {
      console.log('소켓 연결 성공', frame);
      setConnected(true);
      onConnect(frame, stompClient!);
    };

    stompClient.onDisconnect = (frame) => {
      console.log('소켓 연결 끊음', frame);
      setConnected(false);
      beforeDisconnected(frame, stompClient!);
    };

    stompClient.onStompError = (frame) => {
      console.log(frame);
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };

    stompClient.activate();

    return () => {
      stompClient?.deactivate();
    };
  }, []);

  return connected;
};
