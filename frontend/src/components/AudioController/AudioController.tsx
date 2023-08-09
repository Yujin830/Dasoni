import React, { useEffect, useRef, useState } from 'react';
import './AudioController.css';

interface audioControllerProps {
  volume: number;
  muted: boolean;
  songName: string;
  handleVolumeChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleMuteToggle: () => void;
}

function AudioController({
  volume,
  muted,
  songName,
  handleMuteToggle,
  handleVolumeChange,
}: audioControllerProps) {
  const audioRef = useRef<HTMLAudioElement | null>(null);
  const [song, setSong] = useState('');

  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume;
      audioRef.current.muted = muted;
    }
  }, [volume, muted]);

  const loadSong = (songName: string) => {
    import(`../../assets/music/${songName}.mp3`).then((music) => {
      setSong(music.default);
    });
  };

  useEffect(() => {
    loadSong(songName);
  }, [songName]);

  return (
    <div id="audio-controls">
      <button onClick={handleMuteToggle}>
        {muted ? (
          <span className="material-symbols-outlined">no_sound</span>
        ) : (
          <span className="material-symbols-outlined">brand_awareness</span>
        )}
      </button>
      <input
        type="range"
        min="0"
        max="1"
        step="0.01"
        value={volume}
        onChange={handleVolumeChange}
      />
      <audio ref={audioRef} src={song} loop autoPlay={true}>
        <track kind="captions"></track>
      </audio>
    </div>
  );
}

export default AudioController;
