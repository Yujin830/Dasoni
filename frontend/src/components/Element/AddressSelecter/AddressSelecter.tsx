import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AddressSelecter.css';

type AddSelecterProps = {
  style?: object;
  modifySido: string;
  modifyGugun: string;
  setModifySido: (value: string) => void;
  setModifyGugun: (value: string) => void;
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

let mountCount = 1;

function AddressSelecter({
  style,
  modifySido,
  modifyGugun,
  setModifySido,
  setModifyGugun,
}: AddSelecterProps) {
  const [sidoList, setSidoList] = useState(sidos);
  const [gugunList, setGugnList] = useState(guguns);
  const [didMount, setDidMount] = useState(false);

  const handleSidoChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setModifySido(e.target.value);
  };

  const handleGugunChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    console.log(e.target.value);
    setModifyGugun(e.target.value);
  };

  const getSido = async () => {
    const res = await axios.get(
      `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00000000`,
    );

    const data = res.data.regcodes;
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
    setModifyGugun(data[1].code);
  };

  // mount 완료 설정
  useEffect(() => {
    console.log(`mount ${mountCount}`);
    mountCount++;
    setDidMount(true);
    return () => console.log('unmount');
  }, []);

  // mount 됐을 때만 api 불러와서 시군구 설정
  useEffect(() => {
    console.log('didMount', didMount);
    if (didMount) {
      getSido();
      getGugun(modifySido);
    }
  }, [didMount, modifySido]);

  return (
    <div className="AddressSelecter">
      <span>사는 곳</span>
      <div>
        <select id="sido" value={modifySido} onChange={handleSidoChange}>
          {sidoList.map((sido) => (
            <option key={sido.sidoId} value={sido.sidoCode}>
              {sido.sidoName}
            </option>
          ))}
        </select>
        <select id="gugun" value={modifyGugun} onChange={handleGugunChange}>
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
