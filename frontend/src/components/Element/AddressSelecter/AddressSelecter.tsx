import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AddressSelecter.css';

type AddSelecterProps = {
  style?: object;
};

type Sido = {
  sidoId: number;
  sidoName: string;
  sidoCode: string;
};
type Gugun = {
  gugunId: number;
  gugunName: string;
  gugunCode: string;
};

const sidos: Sido[] = [];
const guguns: Gugun[] = [];

function AddressSelecter({ style }: AddSelecterProps) {
  const [sidoList, setSidoList] = useState(sidos);
  const [gugunList, setGugnList] = useState(guguns);
  const [sido, setSido] = useState('11');
  const [gugun, setGugun] = useState('');

  const handleSidoChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSido(e.target.value);
  };

  const handleGugunChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setGugun(e.target.value);
  };

  const getSido = async () => {
    const res = await axios.get(
      `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00000000`,
    );

    const data = res.data.regcodes;
    // console.log(data);
    for (let i = 0; i < data.length; i++) {
      setSidoList((prev) =>
        prev.concat({
          sidoId: i,
          sidoName: data[i].name,
          sidoCode: data[i].code.substring(0, 2),
        }),
      );
    }
  };

  const getGugun = async (sidoCode: string) => {
    setGugnList([]);
    const res = await axios.get(
      `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=${sidoCode}*000000`,
    );

    const data = res.data.regcodes;
    for (let i = 1; i < data.length; i++) {
      setGugnList((prev) =>
        prev.concat({
          gugunId: i,
          gugunName: data[i].name.split(' ')[1],
          gugunCode: data[i].code,
        }),
      );
    }
  };

  useEffect(() => {
    getSido();
    getGugun(sido);
  }, [sido]);

  return (
    <div className="AddressSelecter">
      <span>사는 곳</span>
      <div>
        <select id="sido" value={sido} onChange={handleSidoChange}>
          {sidoList.map((sido) => (
            <option key={sido.sidoId} value={sido.sidoCode}>
              {sido.sidoName}
            </option>
          ))}
        </select>
        <select id="gugun" value={gugun} onChange={handleGugunChange}>
          {gugunList.map((gugun) => (
            <option key={gugun.gugunId} value={gugun.gugunCode}>
              {gugun.gugunName}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
}

export default AddressSelecter;
