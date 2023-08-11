import React, { useEffect, useState } from 'react';
import Header from '../../components/Header/Header';
import Banner from '../../components/Banner/Banner';
import axios from 'axios';
import { useAppSelector } from '../../app/hooks';

function ResultPage() {
  const [result, setResult] = useState();
  const { roomId } = useAppSelector((state) => state.waitingRoom);
  const { memberId } = useAppSelector((state) => state.user);

  useEffect(() => {
    requestResult();
  }, []);

  const requestResult = async () => {
    const res = await axios.get(`/api/rooms/${roomId}/members/${memberId}`);
    console.log(res.data);

    if (res.status === 200) {
      setResult(res.data.content);
    }
  };
  return (
    <div id="result">
      <Header />
      <Banner />
      <main>
        <h2>미팅 결과</h2>
      </main>
    </div>
  );
}

export default ResultPage;
