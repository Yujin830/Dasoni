import React from 'react';
import SkeletonElement from '../../components/SkeletonElement/SkeletonElement';
import SkeletonHeader from '../../components/Header/SkeletonHeader';

const SkeletonMainPage = () => {
  return (
    <div id="main" className="modal-visible">
      <SkeletonHeader />

      {/* Banner 부분 */}
      <div className="banner">{/* Banner 내용 */}</div>

      {/* Main 내용 */}
      <main>
        {/* Main 상단 부분 */}
        <div id="main-top">
          {/* Refresh 버튼 */}
          {/* Filter */}
          {/* Search */}
        </div>

        {/* Room Container */}
        <div className="room-container">
          <SkeletonElement type="title" />
          <SkeletonElement type="thumbnail" />
          <SkeletonElement type="title" />
          <SkeletonElement type="thumbnail" />
          {/* 여러 개의 스켈레톤 요소를 반복해서 렌더링 */}
        </div>

        {/* Room Footer */}
        <div id="room-footer">
          {/* 이전 버튼 */}
          {/* 다음 버튼 */}
          {/* 버튼 박스 */}
        </div>
      </main>

      {/* HelpModal */}
      {/* OpenRoomModal */}
    </div>
  );
};

export default SkeletonMainPage;
