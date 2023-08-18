const WHITE_BASE = 0;
const YELLOW_BASE = 300;
const GREEN_BASE = 500;
const PURPLE_BASE = 1000;
const BLUE_BASE = 1500;
const RED_BASE = 2000;
const RAINBOW_BASE = 2500;

export default function convertScoreToPercent(score: number) {
  let percent = 0;
  let diff = 0;

  if (0 <= score && score < 300) diff = Math.abs(score - WHITE_BASE);
  else if (score < 500) diff = Math.abs(score - YELLOW_BASE);
  else if (score < 1000) diff = Math.abs(score - GREEN_BASE);
  else if (score < 1500) diff = Math.abs(score - PURPLE_BASE);
  else if (score < 2000) diff = Math.abs(score - BLUE_BASE);
  else if (score < 2500) diff = Math.abs(score - RED_BASE);
  else diff = Math.abs(score - RAINBOW_BASE);
  percent = (diff / 500) * 100;
  return percent;
}
