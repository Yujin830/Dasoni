import React from 'react';
import styled from 'styled-components';

// 하트 커서 이미지 파일을 가져옵니다.
import heartCursorImg from '../assets/cursor.png';

// 스타일드 컴포넌트를 사용하여 커서 스타일을 정의합니다.
const HeartCursorContainer = styled.div`
  width: 100%;
  height: 20rem;
  cursor: url(${heartCursorImg}), auto;
`;

function HeartCursor() {
  return (
    <HeartCursorContainer>
      <h1>마우스 커서를 하트로 변경해 보세요!</h1>
    </HeartCursorContainer>
  );
}

export default HeartCursor;
