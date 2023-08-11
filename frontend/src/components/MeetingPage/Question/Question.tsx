import React from 'react';
import './Question.css';

interface QuestionProps {
  content: string;
}

function Question({ content }: QuestionProps) {
  return <div id="meeting-question">{content}</div>;
}

export default Question;
