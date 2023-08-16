import React, { useRef, useState } from 'react';
import '../Modal.css';
import './HelpModal.css';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Pagination, Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import 'swiper/css/scrollbar';
import './HelpModal.css';
import '../Modal.css';
// SwiperCore.use([Pagination, Navigation]);
interface HelpModalProps {
  onClose: () => void;
}

const HelpModal: React.FC<HelpModalProps> = ({ onClose }) => {
  return (
    <div className="modal">
      <div className="header">
        도움말
        <div className="close-button">
          <button onClick={onClose}>X</button>
        </div>
      </div>
      <Swiper
        modules={[Pagination, Navigation]}
        pagination={{ type: 'fraction' }}
        navigation={true}
        className="mySwiper"
      >
        <SwiperSlide>
          <div className="box">
            <h2>1. 방 만들기</h2>
            <div className="explain">
              여러 입장 조건을 걸어 원하는 사람들과 미팅을 시작하세요! <br /> 어떤 인연이 기다리고
              있을까요? <br /> 또한 대기방에서 모든 사람들이 모일 때 까지 간단한 이야기를 <br />
              나누어 보세요 !
            </div>
            <div className="mobile_explain">
              여러 입장 조건을 걸어 원하는 사람들과 미팅을 시작하세요! <br /> 어떤 인연이 기다리고
              있을까요?
            </div>
            <div className="content">방 만드는 과정 GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>2. 빠른 매칭</h2>
            <div className="explain">
              빠른 매칭은 들어갈 수 있는 방을 찾을 필요 없이, 현재 내 레이팅과 비슷한 레이팅의
              사람들과 <br /> 자동 매치해줍니다. 모든 사람들이 모이면 자동으로 미팅이 시작됩니다.
              <br />
            </div>
            <div className="mobile_explain">
              빠른 매칭은 들어갈 수 있는 방을 찾을 필요 없이, 현재 내 레이팅과 비슷한 레이팅의
              사람들과 <br /> 자동 매치해줍니다. 모든 사람들이 모이면 자동으로 미팅이 시작됩니다.
              <br />
            </div>
            <div className="content">빠른 매칭 GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>3. 메기 매칭</h2>
            <div className="explain">
              메기는 느슨한 미팅 씬에 긴장을 주는 플레이어입니다. 메기는 미팅 20분 후부터 입장이
              가능합니다. <br /> 늦게 참여한만큼 불리하기도 하겠지만 모두의 관심과 이목을 한 번에
              받을 수 있으며 메기로 참여해 시그널을 받을 경우
              <br /> 더 높은 레이팅 점수를 받게 됩니다! <br /> 이 점을 잘 이용하여 메기로 이성들의
              시그널을 받으세요!
            </div>
            <div className="mobile_explain">
              메기는 미팅 20분 후부터 입장합니다. <br /> 메기로 참여해 시그널을 받을 경우 <br />더
              높은 레이팅 점수를 받게 됩니다.
            </div>
            <div className="content">메기 입장 GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>4. 게임 플레이</h2>
            <div className="explain">
              게임은 총 50분 + 추가 10분으로 이루어집니다. <br />
              처음엔 간단한 자기소개와 함께 첫인상 투표가 진행됩니다.
            </div>
            <div className="mobile_explain">
              게임은 총 50분 + 추가 10분으로 이루어집니다. <br />
              처음엔 간단한 자기소개와 함께 첫인상 투표가 진행됩니다.
            </div>
            <div className="content">게임 시작 + 첫인상 GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>4. 게임 플레이</h2>
            <div className="explain">
              첫인상 투표가 끝난 후, 일정 시간마다 대화 주제들이
              <br /> 총 3번 랜덤으로 주어지며 첫인상 투표가 끝난 후 <br /> 첫 랜덤 주제가 주어지며,
              20분이 됐을 때 <br />
              참가자들의 나이와 직업이 공개됩니다.
            </div>
            <div className="mobile_explain">
              첫인상 투표가 끝난 후, 일정 시간마다 <br />
              대화 주제들이 랜덤으로 총 3번 주어집니다.
            </div>
            <div className="content">직업 + 나이공개 GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>4. 게임 플레이</h2>
            <div className="explain">
              30분 경과시 두번째 랜덤 주제가 주어지며, <br />
              메기 입장이 허용된 방의 경우 30분부터 메기가 입장할 수 있습니다.
            </div>
            <div className="mobile_explain">
              30분 경과시 두번째 랜덤 주제가 주어지며, <br />
              메기 입장이 허용된 방의 경우 30분부터 <br /> 메기가 입장할 수 있습니다.
            </div>
            <div className="content">두번째 주제 + 메기 입장 GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>4. 게임 플레이</h2>
            <div className="explain">
              이때, 10분간 메기가 대화를 주도하게 됩니다. <br />
              ex. 메기가 참가자에게 질문 혹은 참가자가 메기에게 질문
            </div>
            <div className="mobile_explain">
              이때, 10분간 메기가 대화를 주도하게 됩니다. <br />
              ex. 메기가 참가자에게 질문 혹은
              <br /> 참가자가 메기에게 질문
            </div>
            <div className="content">메기 입장 후 상황GIF</div>
          </div>
        </SwiperSlide>
        <SwiperSlide>
          <div className="box">
            <h2>4. 게임 플레이</h2>
            <div className="explain">
              40분 경과시 마지막 랜덤 주제가 주어지며, <br />
              50분 경과시 최종 투표가 진행되며, 시그널을 <br />
              서로 보낼 시, 둘만의 10분간 진행되는 <br />
              개인 방이 생성이 됩니다.
            </div>
            <div className="mobile_explain">
              40분 경과시 마지막 랜덤 주제가 주어지며, <br />
              50분 경과시 최종 투표 진행 후, 서로 시그널을 주고 받은 <br />
              사람들을 위한 개인 방이 생성이 됩니다.
            </div>
            <div className="content">최종투표 + 시그널 GIF</div>
          </div>
        </SwiperSlide>
      </Swiper>
      <div className="modal-background" />
    </div>
  );
};
export default HelpModal;
