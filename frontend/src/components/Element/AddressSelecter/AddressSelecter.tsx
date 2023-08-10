import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import './AddressSelecter.css';
import { useDispatch } from 'react-redux';
import { setGugun, setSido } from '../../../app/slices/user';

type AddSelecterProps = {
  style?: object;
  modifySido: number;
  modifyGugun: number;
  setModifySido: (value: number) => void;
  setModifyGugun: (value: number) => void;
};

type Sido = {
  sidoId: number;
  sidoName: string;
  sidoCode: number;
};
type Gugun = {
  gugunId: number;
  gugunName: string;
  gugunCode: number;
};

type RegCode = {
  name: string;
  code: string;
};

const sidos: Sido[] = [
  {
    sidoId: -1,
    sidoName: '선택해주세요',
    sidoCode: -1,
  },
];

const guguns: Gugun[] = [];

const mountCount = 1;

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

  useEffect(() => {
    console.log('sidoList:', sidoList);
    console.log('gugunList:', gugunList);
  }, [sidoList, gugunList]);

  const dispatch = useDispatch();

  const handleSidoChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const sidoString = e.target.options[e.target.selectedIndex].getAttribute('data-sido');
    console.log(sidoString);
    dispatch(setSido(sidoString));
    setModifySido(Number(e.target.value));
  };

  const handleGugunChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    // console.log(e.target.value);
    const gugunString = e.target.options[e.target.selectedIndex].getAttribute('data-gugun');
    console.log(gugunString);
    dispatch(setGugun(gugunString));
    setModifyGugun(Number(e.target.value));
  };

  const getSido = async () => {
    const res = await axios.get(
      `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00000000`,
    );

    const data: RegCode[] = res.data.regcodes;
    let sidoId = 0; // Start with 0 and increment for each entry
    const newSidoList: Sido[] = data.map((item: RegCode) => ({
      sidoId: sidoId++,
      sidoName: item.name,
      sidoCode: Number(item.code.substring(0, 2)),
    }));
    setSidoList((prev) => sidoList.concat(newSidoList));
  };

  const getGugun = async (sidoCode: number) => {
    setGugnList([]);
    if (sidoCode !== -1) {
      const res = await axios.get(
        `https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=${sidoCode}*000000`,
      );

      const data: RegCode[] = res.data.regcodes;
      let gugunId = 1; // Start with 0 and increment for each entry
      const newGugunList: Gugun[] = data.slice(1).map((item: RegCode) => ({
        gugunId: gugunId++,
        gugunName: item.name.split(' ')[1],
        gugunCode: Number(item.code),
      }));

      const firstData = {
        gugunId: 0,
        gugunName: '선택해주세요',
        gugunCode: 0,
      };
      newGugunList.unshift(firstData);
      setGugnList(newGugunList);
      setModifyGugun(0);
    }
  };

  const mountCountRef = useRef(1);

  // mount 완료 설정
  useEffect(() => {
    console.log(`mount ${mountCountRef.current}`);
    mountCountRef.current++;
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
      <div className="AddressContainer">
        <select id="sido" value={modifySido} onChange={handleSidoChange}>
          {sidoList.map((sido) => (
            <option key={sido.sidoId} value={sido.sidoCode} data-sido={sido.sidoName}>
              {sido.sidoName}
            </option>
          ))}
        </select>
        <select id="gugun" value={modifyGugun} onChange={handleGugunChange}>
          {gugunList.map((gugun) => (
            <option key={gugun.gugunId} value={gugun.gugunCode} data-gugun={gugun.gugunName}>
              {gugun.gugunName}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
}

export default AddressSelecter;
