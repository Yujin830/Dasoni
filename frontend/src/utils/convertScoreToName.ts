export default function convertScoreToName(score: number) {
  let name = '';

  if (0 <= score && score < 300) name = '하얀';
  else if (score < 500) name = '노랑';
  else if (score < 1000) name = '초록';
  else if (score < 1500) name = '보라';
  else if (score < 2000) name = '파랑';
  else if (score < 2500) name = '빨강';
  else name = '무지개';

  return name;
}
